package th.ac.mahidol.rama.emam.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.LoginCenterActivity;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.adapter.BuildPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardBedDao;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.ListCheckPersonWardBedDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;

public class BuildPreparationFragment extends Fragment implements View.OnClickListener {
    private String wardID, sdlocID, nfcUID, wardName, time, firstName, lastName, RFID, toDayDate, checkType, tomorrowDate, prn, toDayDateCheck, tomorrowDateCheck, tricker;
    private int timeposition;
    private ListView listView;
    private TextView tvUserName, tvTime, tvDoublecheck, tvAdministration, tvNoPatient;
    private BuildPatientAdapter buildPatientAdapter;
    private Button btnLogin;
    private Date datetoDay;
    private ListPatientDataDao dao;
    private ProgressDialog progressDialog;

    public BuildPreparationFragment() {
        super();
    }


    public static BuildPreparationFragment newInstance(String wardID, String nfcUId, String sdlocId, String wardName, int timeposition, String time, String prn, String tricker) {
        BuildPreparationFragment fragment = new BuildPreparationFragment();
        Bundle args = new Bundle();
        args.putString("wardId", wardID);
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("wardname", wardName);
        args.putInt("timeposition", timeposition);
        args.putString("time", time);
        args.putString("prn", prn);
        args.putString("save", tricker);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        wardID = getArguments().getString("wardId");
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("timeposition");
        time = getArguments().getString("time");
        prn = getArguments().getString("prn");
        tricker = getArguments().getString("save");

//        Log.d("check", "BuildPreparationFragment wardID = "+wardID+" /nfcUID = "+nfcUID+" /sdlocID = "+sdlocID+" /wardName = "+wardName+" /timeposition = "+timeposition+" /time = "+time+" /prn = "+prn+" /tricker = "+tricker);

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);
        tvNoPatient = (TextView) rootView.findViewById(R.id.tvNoPatient);

        btnLogin.setOnClickListener(this);
        tvTime.setText(time);
        tvDoublecheck.setOnClickListener(this);
        tvAdministration.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPatientAdapter = new BuildPatientAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfForCheckDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        toDayDateCheck = sdfForCheckDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE, 1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());
        tomorrowDateCheck = sdfForCheckDate.format(c.getTime());

        checkType = "First Check";

        if (nfcUID != null & tricker == null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();

            /////////
            //start 12-01-2017 check person ward bed
            /////////
//            loadPersonWard(nfcUID, wardID);
//            loadCacheDao();
            /////////
            //end
            /////////
        } else {
            progressDialog = ProgressDialog.show(getContext(), "", "Loading", true);
            if (nfcUID != null & tricker != null) {
                if (tricker.equals("save")) {
                    loadPersonWard(nfcUID, sdlocID);
                    /////////
                    //start 12-01-2017 check person ward bed
                    /////////
//                    loadPersonWard(nfcUID, wardID);
//                    loadCacheDao();
                    /////////
                    //end
                    /////////

                    if (timeposition <= 23)
                        loadPatientData(sdlocID, time, checkType, toDayDate);
                    else
                        loadPatientData(sdlocID, time, checkType, tomorrowDate);
                }
            } else {
                if (timeposition <= 23) {
                    loadPatientData(sdlocID, time, checkType, toDayDate);
                } else {
                    loadPatientData(sdlocID, time, checkType, tomorrowDate);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadCacheDao() {
        SharedPreferences prefs = getContext().getSharedPreferences("patientindata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientindata", null);
        if (data != null) {
            final ListPatientDataDao dao = new Gson().fromJson(data, ListPatientDataDao.class);
            if (dao.getPatientDao().size() != 0) {
                buildPatientAdapter.setDao(dao);
                listView.setAdapter(buildPatientAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("wardId", wardID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("timeposition", timeposition);
                        intent.putExtra("position", position);
                        intent.putExtra("time", time);
                        intent.putExtra("patientDao", dao.getPatientDao().get(position));
                        intent.putExtra("prn", prn);
                        getActivity().startActivity(intent);
                    }
                });
            } else {
                tvNoPatient.setText("ไม่มีผู้ป่วย");
                tvNoPatient.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void loadPatientData(String sdlocID, String time, String checkType, String dayDate) {
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, dayDate);
        call.enqueue(new PatientLoadCallback());


    }

    private void loadPersonWard(String nfcUID, String wardID) {
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());
        /////////
        //start 12-01-2017 check person ward bed
        /////////
//        Call<ListCheckPersonWardBedDao> call = HttpManager.getInstance().getService().getCheckPersonWardBed(nfcUID, wardID);
//        call.enqueue(new CheckPersonWardLoadCallback());
        /////////
        //end
        /////////

    }

    private void saveCachePatientData(ListPatientDataDao patientDataDao) {
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientindata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientindata", json);
        editor.apply();
    }

    private void saveCachePersonWard(CheckPersonWardDao checkPersonWardDao) {
        String json = new Gson().toJson(checkPersonWardDao);
        SharedPreferences prefs = getContext().getSharedPreferences("checkperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("checkperson", json);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvDoublecheck) {
            Intent intent = new Intent(getContext(), DoubleCheckActivity.class);
            intent.putExtra("wardId", wardID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (view.getId() == R.id.tvAdministration) {
            Intent intent = new Intent(getContext(), AdministrationActivity.class);
            intent.putExtra("wardId", wardID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (view.getId() == R.id.btnLogin) {
            Intent intent = new Intent(getContext(), LoginCenterActivity.class);
            intent.putExtra("wardId", wardID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            intent.putExtra("login", "Preparation");
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(getContext(), TimelineActivity.class);
                    intent.putExtra("wardId", wardID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }


    class PatientLoadCallback implements Callback<ListPatientDataDao> {
        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            dao = response.body();
            saveCachePatientData(dao);
            List<String> listMrn = new ArrayList<>();
            List<PatientDataDao> patientDao = new ArrayList<>();
            if (dao.getPatientDao() != null) {
                if (dao.getPatientDao().size() != 0) {
                    for (PatientDataDao p : dao.getPatientDao()) {
                        if (!listMrn.contains(p.getMRN())) {
                            listMrn.add(p.getMRN());
                            p.setLink(getPhotoForPatient.getCheckPhotoLinkDao(p.getIdCardNo()));
                            patientDao.add(p);
                        }
                    }
                    dao.setPatientDao(patientDao);
                    saveCachePatientData(dao);
                    buildPatientAdapter.setDao(dao);
                    listView.setAdapter(buildPatientAdapter);
                    if (tricker != null) {
                        if (tricker.equals("save"))
                            loadCacheDao();
                    } else {
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), "แตะ Smart Card ก่อนการเตรียมยา", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    tvNoPatient.setText("ไม่มีผู้ป่วย");
                    tvNoPatient.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }
            } else {
                tvNoPatient.setText("ไม่มีผู้ป่วย");
                tvNoPatient.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "onFailure PatientLoadCallback "+call);
        }


    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            if (dao != null) {
                if (Integer.parseInt(wardID) == Integer.parseInt(String.valueOf(dao.getWardId()))) {
                    saveCachePersonWard(dao);
                    RFID = dao.getRFID();
                    firstName = dao.getFirstName();
                    lastName = dao.getLastName();
                    tvUserName.setText("จัดเตรียมยาโดย  " + firstName + " " + lastName);
                    tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
                } else
                    Toast.makeText(getActivity(), "กรุณาตรวจสอบสิทธิ์การใช้งาน", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "onFailure PersonWardLoadCallback "+call);
        }
    }

    ///////////
    //start 12-01-2017 check person ward bed
    //////////
    class CheckPersonWardLoadCallback implements Callback<ListCheckPersonWardBedDao> {

        @Override
        public void onResponse(Call<ListCheckPersonWardBedDao> call, Response<ListCheckPersonWardBedDao> response) {
            ListCheckPersonWardBedDao dao = response.body();
            Log.d("check", "ListCheckPersonWardBedDao = "+dao.getCheckPersonWardBedDaoList().size());
            if (dao != null) {
                for(CheckPersonWardBedDao ch : dao.getCheckPersonWardBedDaoList()) {
                    Log.d("check", "wardId = "+ ch.getBedID() + "   name = "+ch.getFirstName()+" "+ch.getLastName());
                    if (Integer.parseInt(wardID) == ch.getWardID()) {
                        SharedPreferences prefs = getContext().getSharedPreferences("patientindata", Context.MODE_PRIVATE);
                        String data = prefs.getString("patientindata", null);
                        if (data != null) {
                            ListPatientDataDao daoPatient = new Gson().fromJson(data, ListPatientDataDao.class);
                            if (daoPatient.getPatientDao().size() != 0) {
                                for (PatientDataDao p : daoPatient.getPatientDao()) {
                                    if (ch.getBedID().equals(p.getBedID())) {
                                            Log.d("check", "MRN = " + p.getMRN());
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "กรุณา" +
                                "ตรวจสอบสิทธิ์การใช้งาน", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ListCheckPersonWardBedDao> call, Throwable t) {
            Log.d("check", "onFailure CheckPersonWardLoadCallback "+call);
        }
    }

    //////////
    //end
    //////////

    public static class getPhotoForPatient{
        public static String getCheckPhotoLinkDao(String getIdCardNo) {
            SoapManager soapManager = new SoapManager();
            String link = soapManager.getLinkPhoto("GETPATPHOTO", getIdCardNo);
            return link.replace("GW3", "GW3.rama.mahidol.ac.th");
        }
    }


}
