package th.ac.mahidol.rama.emam.fragment.history;

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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.MainSelectMenuActivity;
import th.ac.mahidol.rama.emam.activity.history.HistoryActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAddPatientAllAdapter;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPatientAllFragment extends Fragment{
    private String nfcUID, sdlocID, wardName;
    private ListView listView;
    private BuildAddPatientAllAdapter buildPatientAllAdapter;
    private ListPatientDataDao dao;
    private ProgressDialog progressDialog;


    public BuildPatientAllFragment() {
        super();
    }

    public static BuildPatientAllFragment newInstance(String nfcUID, String sdlocID, String wardName) {
        BuildPatientAllFragment fragment = new BuildPatientAllFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
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
        View rootView = inflater.inflate(R.layout.fragment_patientall, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        Log.d("check", "BuildPatientAllFragment nfcUID = "+nfcUID+" /sdlocID = "+sdlocID+" /wardName = "+wardName);

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPatientAllAdapter = new BuildAddPatientAllAdapter();
        progressDialog = ProgressDialog.show(getContext(), "", "Loading", true);
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

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK){
                    Intent intent = new Intent(getContext(), MainSelectMenuActivity.class);
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

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void saveCachePatientData(ListPatientDataDao patientDataDao){
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientalldata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientalldata",json);
        editor.apply();
    }

    private void loadPatientMRN(String sdlocID){
        Log.d("check", "loadPatientMRN sdlocID = "+sdlocID);
        Call<MrnTimelineDao> call = HttpManager.getInstance().getService().getPatientPRN(sdlocID);
        call.enqueue(new PatientMRNAllLoadCallback());
    }

    private void loadPatientData(MrnTimelineDao mrnTimelineDao){
        String json = new Gson().toJson(mrnTimelineDao);
        Log.d("check", "json = "+json);
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientData(mrnTimelineDao);
        call.enqueue(new PatientAllDataLoadCallback());
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


    class PatientAllDataLoadCallback implements Callback<ListPatientDataDao>{

        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            dao = response.body();
            saveCachePatientData(dao);
            if(dao.getPatientDao().size() != 0) {
                buildPatientAllAdapter.setDao(dao);
                listView.setAdapter(buildPatientAllAdapter);
                progressDialog.dismiss();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), HistoryActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
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
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "PatientAll PatientAllDataLoadCallback Failure " + t);
        }
    }



}
