package th.ac.mahidol.rama.emam.fragment;

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
import th.ac.mahidol.rama.emam.activity.DoubleCheckForPatientActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildDoubleCheckAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildDoubleCheckFragment extends Fragment implements View.OnClickListener{

    private String nfcUID, sdlocID, wardName, time, RFID, firstName, lastName, toDayDate, checkType;
    private ListView listView;
    private TextView tvPreparation, tvUserName, tvTime;
    private BuildDoubleCheckAdapter buildDoubleCheckAdapter;
    private int timeposition, tricker = 0;
    private Button btnLogin;
    private Date datetoDay;


    public BuildDoubleCheckFragment() {
        super();
    }

    public static BuildDoubleCheckFragment newInstance(String nfcUID, String sdlocID, String wardName, int timeposition, String time) {
        BuildDoubleCheckFragment fragment = new BuildDoubleCheckFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doublecheck, container, false);
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

        Log.d("check", "BuildDoubleCheckFragment initInstances = " + nfcUID +" / "+ sdlocID + " / " + wardName + " / " + time);

        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        tvTime.setText(getArguments().getString("time"));
        tvPreparation.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvDoubleCheckAdapter);
        buildDoubleCheckAdapter = new BuildDoubleCheckAdapter();

        Log.d("check", "BuildPreparationFragment initInstances = "+ nfcUID + " / " + sdlocID + " /timeposition = "+timeposition);
        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        checkType = "Second Check";
        if(nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        }
        else {
            if(timeposition <= 23)
                loadPatientData(sdlocID, time, checkType, toDayDate);
            else{
                Calendar c = Calendar.getInstance();
                c.setTime(datetoDay);
                c.add(Calendar.DATE,1);
                String tomorrowDate = sdfForDrugUseDate.format(c.getTime());
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
        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        String data = prefs.getString("patientdoublecheck",null);

        if(data != null){
            Log.d("check", "loadCacheDao "+ data);
            tricker = 1;
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildDoubleCheckAdapter.setDao(dao, tricker);
            listView.setAdapter(buildDoubleCheckAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(getContext(), DoubleCheckForPatientActivity.class);
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
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientDataPrepare(sdlocID, time, checkType, toDayDate);
        call.enqueue(new PatientLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID){
        Log.d("check", "BuildDoubleCheckFragment loadPersonWard = "+ nfcUID + " / " + sdlocID);
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }

    private void saveCacheDoubleCheckData(ListPatientDataDao patientDataDao){
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientdoublecheck",json);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        Log.d("check", "onClick = " + view.getId());
        if(view.getId() == R.id.tvPreparation){
            Intent intent = new Intent(getContext(), PreparationActivity.class);
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
            saveCacheDoubleCheckData(dao);
            Log.d("check", "DbC dao.size = "+dao.getPatientDao().size());
            buildDoubleCheckAdapter.setDao(dao, tricker);
            listView.setAdapter(buildDoubleCheckAdapter);
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "PatientLoadCallback Failure " + t);
        }
    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            Log.d("check", "CheckPersonWardDao = "+dao.getRFID()+" "+dao.getFirstName()+" "+dao.getLastName());
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("ตรวจสอบยาโดย  " + firstName + " " + lastName);
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "BuildDoubleCheckFragment PersonWardLoadCallback Failure " + t);
        }
    }
}
