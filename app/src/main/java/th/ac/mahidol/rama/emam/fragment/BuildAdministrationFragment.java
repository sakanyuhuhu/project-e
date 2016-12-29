package th.ac.mahidol.rama.emam.fragment;

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
import th.ac.mahidol.rama.emam.activity.AdministrationForPatientActivity;
import th.ac.mahidol.rama.emam.activity.CameraScanActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.LoginCenterActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAdministrationAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAdministrationFragment extends Fragment implements View.OnClickListener {
    private String wardID, sdlocID, nfcUID, wardName, time, firstName, lastName, RFID, toDayDate, checkType, tomorrowDate, message, tricker;
    private int timeposition, num;
    private ListView listView;
    private TextView tvUserName, tvTime, tvDoublecheck, tvPreparation, tvNoPatient;
    private Button btnLogin;
    private BuildAdministrationAdapter buildAdministrationAdapter;
    private Date datetoDay;
    private boolean found = false;
    private ListPatientDataDao dao;

    public BuildAdministrationFragment() {
        super();
    }

    public static BuildAdministrationFragment newInstance(String wardID, String nfcUId, String sdlocId, String wardName, int timeposition, String time, String tricker) {
        BuildAdministrationFragment fragment = new BuildAdministrationFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocId);
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
        View rootView = inflater.inflate(R.layout.fragment_administration, container, false);
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

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvNoPatient = (TextView) rootView.findViewById(R.id.tvNoPatient);

        tvTime.setText(getArguments().getString("time"));
        tvDoublecheck.setOnClickListener(this);
        tvPreparation.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvAdministrationAdapter);
        buildAdministrationAdapter = new BuildAdministrationAdapter();
        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE, 1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        checkType = "Administration";

        if (nfcUID != null & tricker == null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        } else {
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

    private void loadCacheDao() {
        SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
        String data = prefs.getString("patientadministration", null);

        if (data != null) {
            final ListPatientDataDao dao = new Gson().fromJson(data, ListPatientDataDao.class);
            if(dao.getPatientDao().size() != 0) {
                buildAdministrationAdapter.setDao(dao);
                listView.setAdapter(buildAdministrationAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        num = position;
                        Intent intent = new Intent(getContext(), CameraScanActivity.class);
                        startActivityForResult(intent, 1);
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

    private void saveCachePatientAdminData(ListPatientDataDao listPatientDataDao) {
        String json = new Gson().toJson(listPatientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientadministration", json);
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                message = data.getStringExtra("MESSAGE");
                SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
                String patient = prefs.getString("patientadministration", null);
                if (patient != null) {
                    ListPatientDataDao dao = new Gson().fromJson(patient, ListPatientDataDao.class);
                    if (dao.getPatientDao().get(num).getMRN().equals(message)) {
                        found = true;
                        Intent intent = new Intent(getContext(), AdministrationForPatientActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("wardId", wardID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("timeposition", timeposition);
                        intent.putExtra("position", num);
                        intent.putExtra("patientAdmin", dao.getPatientDao().get(num));
                        intent.putExtra("time", time);
                        getActivity().startActivity(intent);
                    } else {
                        found = false;
                        Toast.makeText(getActivity(), "Not found!", Toast.LENGTH_LONG).show();
                    }
                }
            }
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
        } else if (view.getId() == R.id.tvPreparation) {
            Intent intent = new Intent(getContext(), PreparationActivity.class);
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
            intent.putExtra("login", "Administration");
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }


    class PatientLoadCallback implements Callback<ListPatientDataDao> {
        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            dao = response.body();
            saveCachePatientAdminData(dao);
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
                    saveCachePatientAdminData(dao);
                    buildAdministrationAdapter.setDao(dao);
                    listView.setAdapter(buildAdministrationAdapter);
                    if(tricker != null) {
                        if(tricker.equals("save"))
                            loadCacheDao();
                    }
                    else{
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getActivity(), "แตะ Smart Card ก่อนการบริหารยา", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } else {
                    tvNoPatient.setText("ไม่มีผู้ป่วย");
                    tvNoPatient.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "Admin PatientLoadCallback Failure " + t);
        }
    }

    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();

            if(dao != null) {
                if(Integer.parseInt(wardID) == Integer.parseInt(String.valueOf(dao.getWardId()))) {
                    RFID = dao.getRFID();
                    firstName = dao.getFirstName();
                    lastName = dao.getLastName();
                    tvUserName.setText("บริหารยาโดย  " + firstName + " " + lastName);
                    tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
                }
                else{
                    Toast.makeText(getActivity(), "ผู้ใช้ไม่ได้อยู่ใน Ward นี้", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Admin PersonWardLoadCallback Failure " + t);
        }
    }
}
