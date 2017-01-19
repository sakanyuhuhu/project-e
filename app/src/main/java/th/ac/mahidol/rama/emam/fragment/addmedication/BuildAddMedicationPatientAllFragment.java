package th.ac.mahidol.rama.emam.fragment.addmedication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.addmedication.AddMedicationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAddPatientAllAdapter;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationFragment;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAddMedicationPatientAllFragment extends Fragment{
    private String wardID, nfcUID, sdlocID, wardName;
    private ListView listView;
    private BuildAddPatientAllAdapter buildPatientAllAdapter;
    private ListPatientDataDao dao;
    private ProgressDialog progressDialog;


    public BuildAddMedicationPatientAllFragment() {
        super();
    }

    public static BuildAddMedicationPatientAllFragment newInstance(String wardID, String nfcUID, String sdlocID, String wardName) {
        BuildAddMedicationPatientAllFragment fragment = new BuildAddMedicationPatientAllFragment();
        Bundle args = new Bundle();
        args.putString("wardId", wardID);
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patientall_addmedication, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        wardID = getArguments().getString("wardId");
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPatientAllAdapter = new BuildAddPatientAllAdapter();
//        progressDialog = ProgressDialog.show(getContext(), "", "Loading", true);
        loadPatientMRN(sdlocID);
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

    private void saveCachePatientData(ListPatientDataDao patientDataDao){
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("addmedpatientalldata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("addmedpatientalldata",json);
        editor.apply();
    }

    private void loadPatientMRN(String sdlocID){
        Call<MrnTimelineDao> call = HttpManager.getInstance().getService().getPatientPRN(sdlocID);
        call.enqueue(new PatientMRNAllLoadCallback());
    }

    private void loadPatientData(MrnTimelineDao mrnTimelineDao){
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientData(mrnTimelineDao);
        call.enqueue(new AddMedPatientAllDataLoadCallback());
    }



    class PatientMRNAllLoadCallback implements Callback<MrnTimelineDao> {

        @Override
        public void onResponse(Call<MrnTimelineDao> call, Response<MrnTimelineDao> response) {
            MrnTimelineDao dao = response.body();
            loadPatientData(dao);
        }

        @Override
        public void onFailure(Call<MrnTimelineDao> call, Throwable t) {

        }
    }


    class AddMedPatientAllDataLoadCallback implements Callback<ListPatientDataDao>{

        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            dao = response.body();
            saveCachePatientData(dao);
            List<PatientDataDao> patientDao = new ArrayList<>();
            if(dao.getPatientDao().size() != 0) {
                for (PatientDataDao p : dao.getPatientDao()) {
                    p.setLink(BuildPreparationFragment.getPhotoForPatient.getCheckPhotoLinkDao(p.getIdCardNo()));
                    patientDao.add(p);
                }
                dao.setPatientDao(patientDao);
                saveCachePatientData(dao);
                buildPatientAllAdapter.setDao(dao);
                listView.setAdapter(buildPatientAllAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), AddMedicationForPatientActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("wardId", wardID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("position", position);
                        intent.putExtra("patient", dao.getPatientDao().get(position));
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
            else
                Toast.makeText(getActivity(), "ไม่มีผู้ป่วย", Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "PatientAll AddMedPatientAllDataLoadCallback Failure " + t);
        }
    }



}
