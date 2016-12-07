package th.ac.mahidol.rama.emam.fragment.alarm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.alarm.PatientMedOnTimeActivity;
import th.ac.mahidol.rama.emam.adapter.alarm.BuildPatientNextTimeAdapter;
import th.ac.mahidol.rama.emam.adapter.alarm.BuildPatientPreviousTimeAdapter;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPatientOnTimeFragment extends Fragment {
    private String sdlocID, wardName, toDayDate, checkType, adminTimeNext, adminTimePre;
    private int currentTime;
    private TextView tvPreTime, tvNextTime;
    private ListView lvPatienNext, lvPatientPrevious;
    private BuildPatientNextTimeAdapter buildPatientNextTimeAdapter;
    private BuildPatientPreviousTimeAdapter buildPatientPreviousTimeAdapter;
    private ListPatientDataDao daoNext, daoPrevious;
    private Date datetoDay;
    private ProgressDialog progressDialog;


    public BuildPatientOnTimeFragment() {
        super();
    }

    public static BuildPatientOnTimeFragment newInstance(String sdlocID, String wardName) {
        BuildPatientOnTimeFragment fragment = new BuildPatientOnTimeFragment();
        Bundle args = new Bundle();
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_on_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        Log.d("check", "BuildPatientOnTimeFragment sdlocID = " + sdlocID + " /wardName = " + wardName);

        progressDialog = ProgressDialog.show(getContext(), "", "Loading", true);
        tvPreTime = (TextView) rootView.findViewById(R.id.tvPreTime);
        tvNextTime = (TextView) rootView.findViewById(R.id.tvNextTime);
        lvPatienNext = (ListView) rootView.findViewById(R.id.lvPatienNext);
        lvPatientPrevious = (ListView) rootView.findViewById(R.id.lvPatientPrevious);
        buildPatientNextTimeAdapter = new BuildPatientNextTimeAdapter();
        buildPatientPreviousTimeAdapter = new BuildPatientPreviousTimeAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfForCheckDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = sdfForDrugUseDate.format(datetoDay);

        Calendar c = Calendar.getInstance();
        currentTime = c.get(Calendar.HOUR_OF_DAY);
        adminTimeNext = String.valueOf(currentTime + 1);
        adminTimePre = String.valueOf(currentTime - 1);
        checkType = "First Check";
        tvNextTime.setText("กรุณาบริหารยาภายใน " + (currentTime + 1) + ".00 น.");
        tvPreTime.setText("กรุณาตรวจสอบการบริหารยา " + (currentTime - 1) + ".00 น.");

        loadPatientNextData(sdlocID, adminTimeNext, checkType, toDayDate);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadPatientNextData(String sdlocID, String time, String checkType, String dayDate) {
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, dayDate);
        call.enqueue(new PatientNextLoadCallback());
    }

    private void loadPatientPreData(String sdlocID, String time, String checkType, String dayDate) {
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, dayDate);
        call.enqueue(new PatientPreLoadCallback());
    }


    class PatientNextLoadCallback implements Callback<ListPatientDataDao> {

        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            daoNext = response.body();
            if (daoNext.getPatientDao() != null) {
                if (daoNext.getPatientDao().size() != 0) {
                    buildPatientNextTimeAdapter.setDao(daoNext);
                    lvPatienNext.setAdapter(buildPatientNextTimeAdapter);
                    lvPatienNext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), PatientMedOnTimeActivity.class);
                            intent.putExtra("sdlocId", sdlocID);
                            intent.putExtra("wardname", wardName);
                            intent.putExtra("patient", daoNext.getPatientDao().get(position));
                            intent.putExtra("position", position);
                            intent.putExtra("time", adminTimeNext);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
            }
            loadPatientPreData(sdlocID, adminTimePre, checkType, toDayDate);
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "lvPatienNext PatientAllDataLoadCallback Failure " + t);
        }
    }

    class PatientPreLoadCallback implements Callback<ListPatientDataDao> {

        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            daoPrevious = response.body();
            if (daoPrevious.getPatientDao() != null) {
                if (daoPrevious.getPatientDao().size() != 0) {
                    List<PatientDataDao> patient = new ArrayList<>();
                    for (PatientDataDao p : daoPrevious.getPatientDao()) {
                        if (p.getStatus() == null) {
                            patient.add(p);
                        }
                    }
                    daoPrevious.setPatientDao(patient);
                    buildPatientPreviousTimeAdapter.setDao(daoPrevious);
                    lvPatientPrevious.setAdapter(buildPatientPreviousTimeAdapter);
                    lvPatientPrevious.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), PatientMedOnTimeActivity.class);
                            intent.putExtra("sdlocId", sdlocID);
                            intent.putExtra("wardname", wardName);
                            intent.putExtra("patient", daoPrevious.getPatientDao().get(position));
                            intent.putExtra("position", position);
                            intent.putExtra("time", adminTimePre);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "lvPatientPrevious PatientAllDataLoadCallback Failure " + t);
        }
    }


}
