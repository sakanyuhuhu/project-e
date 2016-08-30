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
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPreparationFragment extends Fragment {
    private String sdlocID, nfcUID, wardName, time, firstName, lastName;
    private int timeposition;
    private ListView listView;
    private TextView tvUserName, tvTime, tvPreparation, tvDoublecheck, tvAdministration;
    private BuildPreparationAdapter buildPreparationAdapter;

    public BuildPreparationFragment() {
        super();
    }



    public static BuildPreparationFragment newInstance(String nfcUId, String sdlocId, String wardName, int timeposition, String time) {
        BuildPreparationFragment fragment = new BuildPreparationFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
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
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);

        tvTime.setText(getArguments().getString("time"));

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPreparationAdapter = new BuildPreparationAdapter();

        Log.d("check", "BuildPreparationFragment initInstances = "+ nfcUID + " / " + sdlocID);
        if(nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        }
        else {
            loadPatientData();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadCacheDao(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);

        if(data != null){
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildPreparationAdapter.setDao(dao);
            listView.setAdapter(buildPreparationAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("firstname", firstName);
                    intent.putExtra("lastname", lastName);
                    intent.putExtra("timeposition", timeposition);
                    intent.putExtra("position", position);
                    intent.putExtra("time", time);
                    getActivity().startActivity(intent);
                }
            });
        }
    }

    private void loadPatientData(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientintime", Context.MODE_PRIVATE);
        String data = prefs.getString("patienttime",null);

        if(data != null){
            TimelineDao timelineDao = new Gson().fromJson(data,TimelineDao.class);
            MrnTimelineDao mrnListBean = new MrnTimelineDao();
            mrnListBean.setMrn(timelineDao.getTimelineDao().get(timeposition).getMrn());

            Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientData(mrnListBean);
            call.enqueue(new PatientLoadCallback());
        }

    }

    private void loadPersonWard(String nfcUID, String sdlocID){
        Log.d("check", "loadPersonWard = "+ nfcUID + " / " + sdlocID);
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }

    private void saveCachePatientData(ListPatientDataDao patientDataDao){
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientintdata",json);
        editor.apply();
    }



    class PatientLoadCallback implements Callback<ListPatientDataDao>{
        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            ListPatientDataDao dao = response.body();
            saveCachePatientData(dao);
            buildPreparationAdapter.setDao(dao);
            listView.setAdapter(buildPreparationAdapter);
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "PatientLoadCallback Failure " + t);
        }
    }



    class PersonWardLoadCallback implements Callback<CheckPersonWardDao>{

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("เตรียมยาโดย  " + firstName + " " + lastName);
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "PersonWardLoadCallback Failure " + t);
        }
    }

}
