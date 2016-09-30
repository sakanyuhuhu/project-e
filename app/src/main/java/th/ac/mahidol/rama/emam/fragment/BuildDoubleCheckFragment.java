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
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckForPatientActivity;
import th.ac.mahidol.rama.emam.activity.LoginDoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildDoubleCheckAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildDoubleCheckFragment extends Fragment implements View.OnClickListener{

    private String nfcUID, sdlocID, wardName, time, RFID, firstName, lastName, toDayDate, checkType, tomorrowDate, nameDouble;
    private ListView listView;
    private TextView tvPreparation, tvAdministration, tvUserName, tvTime;
    private BuildDoubleCheckAdapter buildDoubleCheckAdapter;
    private int timeposition;
    private Button btnLogin;
    private Date datetoDay;


    public BuildDoubleCheckFragment() {
        super();
    }

    public static BuildDoubleCheckFragment newInstance(String nfcUID, String sdlocID, String wardName, int timeposition, String time, String nameDouble) {
        BuildDoubleCheckFragment fragment = new BuildDoubleCheckFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("nameDouble", nameDouble);
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
        nameDouble = getArguments().getString("nameDouble");


        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        tvTime.setText(getArguments().getString("time"));
        tvPreparation.setOnClickListener(this);
        tvAdministration.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvDoubleCheckAdapter);
        buildDoubleCheckAdapter = new BuildDoubleCheckAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        checkType = "Second Check";

        if(nfcUID != null) {
            SharedPreferences prefs = getContext().getSharedPreferences("checkperson", Context.MODE_PRIVATE);
            String data = prefs.getString("checkperson",null);
            if(data != null){
                CheckPersonWardDao dao = new Gson().fromJson(data,CheckPersonWardDao.class);
                if(dao.getNfcUId().equals(nfcUID)){
                    Toast.makeText(getActivity(), "ผู้ตรวจสอบยาไม่ควรเป็นคนเดียวกับผู้จัดเตรียมยา", Toast.LENGTH_LONG).show();
                    loadCheckPersonBynfcUID();
                }
                else{
                    loadPersonWard(nfcUID, sdlocID);
                    loadCacheDao();
                }
            }
        }
        else {
            if(nameDouble != null){
                tvUserName.setText("ตรวจสอบยาโดย  " + nameDouble);
                tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
                loadCacheDao();
            }
            else {
                if (timeposition <= 23)
                    loadPatientData(sdlocID, time, checkType, toDayDate);
                else {
                    loadPatientData(sdlocID, time, checkType, tomorrowDate);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadCheckPersonBynfcUID(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        String data = prefs.getString("patientdoublecheck",null);
        if(data != null){
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildDoubleCheckAdapter.setDao(dao);
            listView.setAdapter(buildDoubleCheckAdapter);
        }
    }

    private void loadCacheDao(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        String data = prefs.getString("patientdoublecheck",null);

        if(data != null){
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildDoubleCheckAdapter.setDao(dao);
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
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, toDayDate);
        call.enqueue(new PatientLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID){
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
        if(view.getId() == R.id.tvPreparation){
            Intent intent = new Intent(getContext(), PreparationActivity.class);
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
        else if(view.getId() == R.id.btnLogin){
            Intent intent = new Intent(getContext(), LoginDoubleCheckActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }




    class PatientLoadCallback implements Callback<ListPatientDataDao>{
        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            ListPatientDataDao dao = response.body();
            saveCacheDoubleCheckData(dao);
            buildDoubleCheckAdapter.setDao(dao);
            listView.setAdapter(buildDoubleCheckAdapter);
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "Double PatientLoadCallback Failure " + t);
        }
    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("ตรวจสอบยาโดย  " + firstName + " " + lastName);
            tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
            Toast.makeText(getActivity(), ""+firstName+" "+lastName, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Double PersonWardLoadCallback Failure " + t);
        }
    }
}
