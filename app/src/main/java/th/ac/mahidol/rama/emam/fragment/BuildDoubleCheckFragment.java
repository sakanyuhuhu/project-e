package th.ac.mahidol.rama.emam.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import th.ac.mahidol.rama.emam.activity.DoubleCheckForPatientActivity;
import th.ac.mahidol.rama.emam.activity.LoginCenterActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.adapter.BuildDoubleCheckAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildDoubleCheckFragment extends Fragment implements View.OnClickListener {

    private String wardID, nfcUID, sdlocID, wardName, time, RFIDouble, RFIDPrepare, firstName, lastName, toDayDate, checkType, tomorrowDate, tricker, todayDateDrug, tomorrowDateDrug;
    private ListView listView;
    private TextView tvPreparation, tvAdministration, tvUserName, tvTime, tvNoPatient;
    private BuildDoubleCheckAdapter buildDoubleCheckAdapter;
    private int timeposition, positionPatient;
    private Button btnLogin;
    private Date datetoDay;
    private ListPatientDataDao dao;
    private ListDrugCardDao listDrugCardDao;
    private ProgressDialog progressDialog;


    public BuildDoubleCheckFragment() {
        super();
    }

    public static BuildDoubleCheckFragment newInstance(String wardID, String nfcUID, String sdlocID, String wardName, int timeposition, String time, String tricker) {
        BuildDoubleCheckFragment fragment = new BuildDoubleCheckFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("save", tricker);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doublecheck, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");
        tricker = getArguments().getString("save");

        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvNoPatient = (TextView) rootView.findViewById(R.id.tvNoPatient);

        tvTime.setText(getArguments().getString("time"));
        tvPreparation.setOnClickListener(this);
        tvAdministration.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvDoubleCheckAdapter);
        buildDoubleCheckAdapter = new BuildDoubleCheckAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForPatientUseDate = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = sdfForPatientUseDate.format(datetoDay);
        todayDateDrug = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE, 1);
        tomorrowDate = sdfForPatientUseDate.format(c.getTime());
        tomorrowDateDrug = sdfForDrugUseDate.format(c.getTime());

        checkType = "Second Check";

        if (nfcUID != null & tricker == null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        } else {
            progressDialog = ProgressDialog.show(getContext(), "", "Loading", true);
            if (nfcUID != null & tricker != null) {
                if (tricker.equals("save")) {
                    loadPersonWard(nfcUID, sdlocID);
                    if (timeposition <= 23)
                        loadPatientData(sdlocID, time, checkType, toDayDate);
                    else
                        loadPatientData(sdlocID, time, checkType, tomorrowDate);
                }
            } else {
                if (timeposition <= 23)
                    loadPatientData(sdlocID, time, checkType, toDayDate);
                else
                    loadPatientData(sdlocID, time, checkType, tomorrowDate);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void setMedication(String time, String date, String mrn){
        DrugCardDao drugCardDao = new DrugCardDao();
        drugCardDao.setAdminTimeHour(time);
        drugCardDao.setDrugUseDate(date);
        drugCardDao.setMRN(mrn);
        drugCardDao.setCheckType("First Check");

        loadMedicalData(drugCardDao);
    }

    private void loadCacheDao() {
        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        String data = prefs.getString("patientdoublecheck", null);

        if (data != null) {
            final ListPatientDataDao dao = new Gson().fromJson(data, ListPatientDataDao.class);
            if(dao.getPatientDao().size() != 0) {
                buildDoubleCheckAdapter.setDao(dao);
                listView.setAdapter(buildDoubleCheckAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        positionPatient = position;
                        if (timeposition <= 23) {
                            setMedication(time, todayDateDrug, dao.getPatientDao().get(position).getMRN());
                        } else {
                            setMedication(time, tomorrowDateDrug, dao.getPatientDao().get(position).getMRN());
                        }
                    }
                });

            }
            else{
                tvNoPatient.setText("ไม่มีผู้ป่วย");
                tvNoPatient.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void loadPatientData(String sdlocID, String time, String checkType, String toDayDate) {
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, toDayDate);
        call.enqueue(new PatientLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID) {
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }

    private void loadMedicalData(DrugCardDao drugCardDao) {
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }


    private void saveCacheDoubleCheckData(ListPatientDataDao patientDataDao) {
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientdoublecheck", json);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvPreparation) {
            Intent intent = new Intent(getContext(), PreparationActivity.class);
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
            intent.putExtra("login", "DoubleCheck");
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
                    intent.putExtra("nfcUId", nfcUID);
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
            saveCacheDoubleCheckData(dao);
            List<String> listMrn = new ArrayList<>();
            List<PatientDataDao> patientDao = new ArrayList<>();
            if (dao.getPatientDao() != null) {
                if (dao.getPatientDao().size() != 0) {
                    for (PatientDataDao p : dao.getPatientDao()) {
                        if (!listMrn.contains(p.getMRN())) {
                            listMrn.add(p.getMRN());
                            p.setLink(BuildPreparationFragment.getPhotoForPatient.getCheckPhotoLinkDao(p.getIdCardNo()));
                            patientDao.add(p);
                        }
                    }

                    dao.setPatientDao(patientDao);
                    saveCacheDoubleCheckData(dao);
                    buildDoubleCheckAdapter.setDao(dao);
                    listView.setAdapter(buildDoubleCheckAdapter);
                    if (tricker != null) {
                        if (tricker.equals("save"))
                            loadCacheDao();
                    } else {
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), "แตะ Smart Card ก่อนการตรวจสอบยา", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    tvNoPatient.setText("ไม่มีผู้ป่วย");
                    tvNoPatient.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }
            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
        }
    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            if(dao != null) {
                if(Integer.parseInt(wardID) == Integer.parseInt(String.valueOf(dao.getWardId()))) {
                    RFIDouble = dao.getRFID();
                    firstName = dao.getFirstName();
                    lastName = dao.getLastName();
                    tvUserName.setText("ตรวจสอบยาโดย  " + firstName + " " + lastName);
                    tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else{
                    Toast.makeText(getActivity(), "ผู้ใช้ไม่ได้อยู่ใน Ward นี้", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
        }
    }


    class DrugLoadCallback implements Callback<ListDrugCardDao> {

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            listDrugCardDao = response.body();
            for (DrugCardDao d : listDrugCardDao.getListDrugCardDao()) {
                RFIDPrepare = d.getRFID();
                break;
            }
            SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
            String data = prefs.getString("patientdoublecheck", null);

            if (data != null) {
                final ListPatientDataDao dao = new Gson().fromJson(data, ListPatientDataDao.class);
                if (!RFIDPrepare.equals(RFIDouble)) {
                    Intent intent = new Intent(getContext(), DoubleCheckForPatientActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("wardId", wardID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("RFID", RFIDouble);
                    intent.putExtra("firstname", firstName);
                    intent.putExtra("lastname", lastName);
                    intent.putExtra("timeposition", timeposition);
                    intent.putExtra("position", positionPatient);
                    intent.putExtra("patientDouble", dao.getPatientDao().get(positionPatient));
                    intent.putExtra("time", time);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "ผู้ตรวจสอบยาไม่ควรเป็นคนเดียวกับผู้จัดเตรียมยา", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
        }
    }

    static class NewDrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            ListDrugCardDao listDrug = new ListDrugCardDao();
            listDrug = response.body();
            for (DrugCardDao d : listDrug.getListDrugCardDao()) {
                Log.d("check", "getTradeName = "+d.getTradeName());
            }
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {

        }
    }
}
