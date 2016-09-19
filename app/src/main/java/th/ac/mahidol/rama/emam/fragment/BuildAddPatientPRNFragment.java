package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationAdapter;
import th.ac.mahidol.rama.emam.dao.buildPRNPatientDAO.MRNDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAddPatientPRNFragment extends Fragment{
    private String sdlocID, nfcUID, wardName, time;
    private int timeposition;
    private ListView listView;
    private BuildPreparationAdapter buildPatientPRNAdapter;

    public BuildAddPatientPRNFragment() {
        super();
    }

    public static BuildAddPatientPRNFragment newInstance(String sdlocID, String wardName, int timeposition, String time) {
        BuildAddPatientPRNFragment fragment = new BuildAddPatientPRNFragment();
        Bundle args = new Bundle();
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

        Log.d("check", "BuildAddPatientPRNFragment sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+timeposition+" /time = "+time);

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPatientPRNAdapter = new BuildPreparationAdapter();


        loadPatientPRN(sdlocID);
        loadPatientData();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadPatientData(){

    }

    private void saveCacheMRNWard(MRNDao mrnDao){
        String json = new Gson().toJson(mrnDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientprn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientprn",json);
        editor.apply();
    }

    private void loadPatientPRN(String sdlocID){
        Call<MRNDao> call = HttpManager.getInstance().getService().getPatientPRN(sdlocID);
        call.enqueue(new PatientPRNLoadCallback());
    }




    class PatientPRNLoadCallback implements Callback<MRNDao> {

        @Override
        public void onResponse(Call<MRNDao> call, Response<MRNDao> response) {
            MRNDao dao = response.body();
            saveCacheMRNWard(dao);
            String json = new Gson().toJson(dao);
            Log.d("check", "dao = "+json);
        }

        @Override
        public void onFailure(Call<MRNDao> call, Throwable t) {

        }
    }
}
