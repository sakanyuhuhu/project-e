package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import th.ac.mahidol.rama.emam.activity.AddDrugPatientPRNActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAddPatientPRNAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAddPatientPRNFragment extends Fragment{
    private String sdlocID, nfcUID, wardName, time, RFID, firstName, lastName;
    private int timeposition;
    private ListView listView;
    private BuildAddPatientPRNAdapter buildPatientPRNAdapter;

    public BuildAddPatientPRNFragment() {
        super();
    }

    public static BuildAddPatientPRNFragment newInstance(String nfcUID, String sdlocID, String wardName, int timeposition, String time) {
        BuildAddPatientPRNFragment fragment = new BuildAddPatientPRNFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
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
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");
        Log.d("check", "BuildAddPatientPRNFragment nfcUId = "+nfcUID+" /sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+timeposition+" /time = "+time);

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPatientPRNAdapter = new BuildAddPatientPRNAdapter();

        loadPersonWard(nfcUID, sdlocID);
        loadPatientPRN(sdlocID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadPatientData(MrnTimelineDao mrnTimelineDao){
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientData(mrnTimelineDao);
        call.enqueue(new PatientPRNDataLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID){
        Log.d("check", "loadPersonWard = "+nfcUID+"  "+sdlocID);
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }

    private void loadPatientPRN(String sdlocID){
        Call<MrnTimelineDao> call = HttpManager.getInstance().getService().getPatientPRN(sdlocID);
        call.enqueue(new PatientPRNLoadCallback());
    }

    private void saveCachePatientData(ListPatientDataDao patientDataDao){
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientintdata",json);
        editor.apply();
    }

    private void saveCachePersonWard(CheckPersonWardDao checkPersonWardDao){
        String json = new Gson().toJson(checkPersonWardDao);
        SharedPreferences prefs = getContext().getSharedPreferences("checkperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("checkperson",json);
        editor.apply();
    }




    class PatientPRNLoadCallback implements Callback<MrnTimelineDao> {

        @Override
        public void onResponse(Call<MrnTimelineDao> call, Response<MrnTimelineDao> response) {
            MrnTimelineDao dao = response.body();
//            String json = new Gson().toJson(dao);
//            Log.d("check", "MrnTimelineDao = "+json);
            loadPatientData(dao);
        }

        @Override
        public void onFailure(Call<MrnTimelineDao> call, Throwable t) {

        }
    }

    class PatientPRNDataLoadCallback implements Callback<ListPatientDataDao>{

        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            final ListPatientDataDao dao = response.body();
            saveCachePatientData(dao);
            if(dao.getPatientDao().size() != 0) {
                buildPatientPRNAdapter.setDao(dao);
                listView.setAdapter(buildPatientPRNAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), AddDrugPatientPRNActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("timeposition", timeposition);
                        intent.putExtra("position", position);
                        intent.putExtra("time", time);
                        getActivity().startActivity(intent);
                    }
                });
            }
            else
                Toast.makeText(getActivity(), "ไม่มีผู้ป่วย", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {

        }
    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao>{

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            saveCachePersonWard(dao);
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Prepare PersonWardLoadCallback Failure " + t);
        }
    }
}
