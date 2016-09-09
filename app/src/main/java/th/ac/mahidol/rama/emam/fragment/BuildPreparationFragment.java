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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPreparationFragment extends Fragment implements View.OnClickListener{
    private String sdlocID, nfcUID, wardName, time, firstName, lastName, RFID, toDayDate, checkType, tomorrowDate;
    private int timeposition, tricker = 0;
    private ListView listView;
    private TextView tvUserName, tvTime, tvDoublecheck, tvAdministration;
    private BuildPreparationAdapter buildPreparationAdapter;
    private Button btnLogin;
    private Date datetoDay;

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
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);

        tvTime.setText(getArguments().getString("time"));
        tvDoublecheck.setOnClickListener(this);
        tvAdministration.setOnClickListener(this);


        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPreparationAdapter = new BuildPreparationAdapter();

        Log.d("check", "BuildPreparationFragment initInstances = "+ nfcUID + " / " + sdlocID + " /timeposition = "+timeposition);
        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        checkType = "First Check";

        if(nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        }
        else {
            if(timeposition <= 23)
                loadPatientData(sdlocID, time, checkType, toDayDate);
            else{
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

    private void loadCacheDao(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);

        if(data != null){
            tricker = 1;
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildPreparationAdapter.setDao(dao, tricker);
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
                    getActivity().startActivity(intent);
                }
            });
        }
    }

    private void loadPatientData(String sdlocID, String time, String checkType, String toDayDate){
        Log.d("check", "loadPatientData = "+ sdlocID+" "+time+" " +checkType+" "+toDayDate);
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, toDayDate);
        call.enqueue(new PatientLoadCallback());
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

    private void saveCachePersonWard(CheckPersonWardDao checkPersonWardDao){
        String json = new Gson().toJson(checkPersonWardDao);
        SharedPreferences prefs = getContext().getSharedPreferences("checkperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("checkperson",json);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tvDoublecheck){
            Intent intent = new Intent(getContext(), DoubleCheckActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
        }
        else if(view.getId() == R.id.tvAdministration){
            Intent intent = new Intent(getContext(), AdministrationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
        }
    }

    class PatientLoadCallback implements Callback<ListPatientDataDao>{
        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            ListPatientDataDao dao = response.body();
            saveCachePatientData(dao);
            Log.d("check", "PP dao.size = "+dao.getPatientDao().size());
            buildPreparationAdapter.setDao(dao, tricker);
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
            saveCachePersonWard(dao);
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("จัดเตรียมยาโดย  " + firstName + " " + lastName);
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "PersonWardLoadCallback Failure " + t);
        }
    }

}
