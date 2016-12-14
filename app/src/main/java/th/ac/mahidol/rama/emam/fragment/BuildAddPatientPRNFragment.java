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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AddDrugPatientPRNActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAddPatientAllPRNAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAddPatientPRNFragment extends Fragment {
    private String wardID, sdlocID, nfcUID, wardName, time, RFID, firstName, lastName, prn, toDayDateCheck, tomorrowDateCheck;
    private int timeposition;
    private ListView listView;
    private TextView tvUserName, tvNoPatient;
    private Date datetoDay;
    private ListPatientDataDao dao;
    private BuildAddPatientAllPRNAdapter buildPatientAllAdapter;
    private TimelineDao timelineDao;

    public BuildAddPatientPRNFragment() {
        super();
    }

    public static BuildAddPatientPRNFragment newInstance(String nfcUID, String wardID, String sdlocID, String wardName, int timeposition, String time, String prn, TimelineDao timelineDao) {
        BuildAddPatientPRNFragment fragment = new BuildAddPatientPRNFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("prn", prn);
        args.putParcelable("include", timelineDao);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prn_patient_add, container, false);
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
        prn = getArguments().getString("prn");
        timelineDao = getArguments().getParcelable("include");

        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvNoPatient = (TextView) rootView.findViewById(R.id.tvNoPatient);
        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPatientAllAdapter = new BuildAddPatientAllPRNAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForCheckDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDateCheck = sdfForCheckDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE, 1);
        tomorrowDateCheck = sdfForCheckDate.format(c.getTime());

        if (nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        } else {
            loadPatientPRN(sdlocID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadCacheDao() {
        SharedPreferences prefs = getContext().getSharedPreferences("patientprnAll", Context.MODE_PRIVATE);
        String data = prefs.getString("patientprnAll", null);
        if (data != null) {
            final ListPatientDataDao dao = new Gson().fromJson(data, ListPatientDataDao.class);
            if (dao.getPatientDao().size() != 0) {
                buildPatientAllAdapter.setDao(dao, timelineDao);
                listView.setAdapter(buildPatientAllAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), AddDrugPatientPRNActivity.class);
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
                        intent.putExtra("patientPRN", dao.getPatientDao().get(position));
                        intent.putExtra("prn", prn);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
            } else {
                tvNoPatient.setText("ไม่มีผู้ป่วย");
                tvNoPatient.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void loadPatientData(MrnTimelineDao mrnTimelineDao) {
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientData(mrnTimelineDao);
        call.enqueue(new PatientPRNDataLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID) {
        Log.d("check", "loadPersonWard = " + nfcUID + "  " + sdlocID);
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }

    private void loadPatientPRN(String sdlocID) {
        Call<MrnTimelineDao> call = HttpManager.getInstance().getService().getPatientPRN(sdlocID);
        call.enqueue(new PatientPRNLoadCallback());
    }

    private void saveCachePatientData(ListPatientDataDao patientDataDao) {
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientprnAll", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientprnAll", json);
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

    class PatientPRNLoadCallback implements Callback<MrnTimelineDao> {

        @Override
        public void onResponse(Call<MrnTimelineDao> call, Response<MrnTimelineDao> response) {
            MrnTimelineDao dao = response.body();
            loadPatientData(dao);
        }

        @Override
        public void onFailure(Call<MrnTimelineDao> call, Throwable t) {
            Log.d("check", "PatientPRNLoadCallback onFailure = " + t);
        }
    }

    class PatientPRNDataLoadCallback implements Callback<ListPatientDataDao> {

        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            dao = response.body();
            if (dao.getPatientDao().size() != 0) {
                buildPatientAllAdapter.setDao(dao, timelineDao);
                listView.setAdapter(buildPatientAllAdapter);
                saveCachePatientData(dao);
            } else {
                tvNoPatient.setText("ไม่มีผู้ป่วย");
                tvNoPatient.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "PatientPRNDataLoadCallback onFailure = " + t);
        }
    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();

            if (dao != null) {
                if (wardID.equals(dao.getWardId())) {
                    saveCachePersonWard(dao);
                    RFID = dao.getRFID();
                    firstName = dao.getFirstName();
                    lastName = dao.getLastName();
                    tvUserName.setText("จัดเตรียมยาโดย  " + firstName + " " + lastName);
                    tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
                } else {
                    Toast.makeText(getActivity(), "ผู้ใช้ไม่ได้อยู่ใน Ward นี้", Toast.LENGTH_LONG).show();
                }
            }

//            saveCachePersonWard(dao);
//            RFID = dao.getRFID();
//            firstName = dao.getFirstName();
//            lastName = dao.getLastName();
//            tvUserName.setText("จัดเตรียมยาโดย  " + firstName + " " + lastName);
//            tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
//            Toast.makeText(getActivity(), ""+firstName+" "+lastName, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "PatientAll PersonWardLoadCallback Failure " + t);
        }
    }
}
