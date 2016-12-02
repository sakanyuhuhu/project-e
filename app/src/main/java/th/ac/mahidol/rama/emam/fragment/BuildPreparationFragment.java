package th.ac.mahidol.rama.emam.fragment;

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
import th.ac.mahidol.rama.emam.activity.LoginPreparationActivity;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPreparationFragment extends Fragment implements View.OnClickListener {
    private String sdlocID, nfcUID, wardName, time, firstName, lastName, RFID, toDayDate, checkType, tomorrowDate, prn, toDayDateCheck, tomorrowDateCheck, tricker;
    private int timeposition;
    private ListView listView;
    private TextView tvUserName, tvTime, tvDoublecheck, tvAdministration, tvNoPatient;
    private BuildPreparationAdapter buildPreparationAdapter;
    private Button btnLogin;
    private Date datetoDay;
    private ListPatientDataDao dao;

    public BuildPreparationFragment() {
        super();
    }


    public static BuildPreparationFragment newInstance(String nfcUId, String sdlocId, String wardName, int timeposition, String time, String prn, String tricker) {
        BuildPreparationFragment fragment = new BuildPreparationFragment();
        Bundle args = new Bundle();
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
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("timeposition");
        time = getArguments().getString("time");
        prn = getArguments().getString("prn");
        tricker = getArguments().getString("save");

        Log.d("check", "BuildPreparationFragment nfcUId = " + nfcUID + " /sdlocId = " + sdlocID + " /wardName = " + wardName + " /position = " + timeposition + " /time = " +
                time + " /prn = " + prn + " /tricker = " + tricker);

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);
        tvNoPatient = (TextView) rootView.findViewById(R.id.tvNoPatient);

        tvTime.setText(time);
        btnLogin.setOnClickListener(this);
        tvDoublecheck.setOnClickListener(this);
        tvAdministration.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPreparationAdapter = new BuildPreparationAdapter();

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
        } else {
            if (nfcUID != null & tricker != null) {
                if (tricker.equals("save")) {
                    loadPersonWard(nfcUID, sdlocID);
                    if (timeposition <= 23) {
                        Log.d("check", "tricker.equals(save) timeposition <=23");
                        loadPatientData(sdlocID, time, checkType, toDayDate);
                        loadCacheDao();
                    } else {
                        Log.d("check", "tricker.equals(save) timeposition");
                        loadPatientData(sdlocID, time, checkType, tomorrowDate);
                        loadCacheDao();
                    }
                }
            } else {
                if (timeposition <= 23) {
                    Log.d("check", "tricker timeposition <= 23");
                    loadPatientData(sdlocID, time, checkType, toDayDate);
                } else {
                    Log.d("check", "tricker timeposition");
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
            Log.d("check", "************************************************* timeposition = " + timeposition + " ********* dao = " + dao.getPatientDao().size());
            buildPreparationAdapter.setDao(dao);
            listView.setAdapter(buildPreparationAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
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
        }
    }

    private void loadPatientData(String sdlocID, String time, String checkType, String dayDate) {
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, dayDate);
        call.enqueue(new PatientLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID) {
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

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
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (view.getId() == R.id.tvAdministration) {
            Intent intent = new Intent(getContext(), AdministrationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (view.getId() == R.id.btnLogin) {
            Intent intent = new Intent(getContext(), LoginPreparationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
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
            List<String> listMrn = new ArrayList<>();
            List<PatientDataDao> patientDao = new ArrayList<>();
            if (dao.getPatientDao() != null) {
                if (dao.getPatientDao().size() != 0) {
                    for (PatientDataDao p : dao.getPatientDao()) {
                        if (!listMrn.contains(p.getMRN())) {
                            listMrn.add(p.getMRN());
                            patientDao.add(p);
                        }
                    }
                    dao.setPatientDao(patientDao);
                    saveCachePatientData(dao);
                    buildPreparationAdapter.setDao(dao);
                    listView.setAdapter(buildPreparationAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getActivity(), "แตะ Smart Card ก่อนการเตรียมยา", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    tvNoPatient.setText("ไม่มีผู้ป่วย");
                    tvNoPatient.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }
            } else {
//                Toast.makeText(getActivity(), "ไม่มีผู้ป่วย", Toast.LENGTH_LONG).show();
                tvNoPatient.setText("ไม่มีผู้ป่วย");
                tvNoPatient.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "Prepare PatientLoadCallback Failure " + t);
        }
    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            saveCachePersonWard(dao);
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("จัดเตรียมยาโดย  " + firstName + " " + lastName);
            tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
            Toast.makeText(getActivity(), "" + firstName + " " + lastName, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Prepare PersonWardLoadCallback Failure " + t);
        }
    }

}
