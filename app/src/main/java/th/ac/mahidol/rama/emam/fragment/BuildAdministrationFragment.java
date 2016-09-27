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
import th.ac.mahidol.rama.emam.activity.AdministrationForPatientActivity;
import th.ac.mahidol.rama.emam.activity.CameraScanActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.LoginAdministrationActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAdministrationAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAdministrationFragment extends Fragment implements View.OnClickListener{
    private String sdlocID, nfcUID, wardName, time, firstName, lastName, RFID, toDayDate, checkType, tomorrowDate, message, nameAdmin;
    private int timeposition, num = 0;
    private ListView listView;
    private TextView tvUserName, tvTime, tvDoublecheck, tvPreparation;
    private Button btnLogin, btnScan;
    private BuildAdministrationAdapter buildAdministrationAdapter;
    private Date datetoDay;
    private boolean found;

    public BuildAdministrationFragment() {
        super();
    }

    public static BuildAdministrationFragment newInstance(String nfcUId, String sdlocId, String wardName, int timeposition, String time, String nameAdmin) {
        BuildAdministrationFragment fragment = new BuildAdministrationFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("nameAdmin", nameAdmin);
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
        View rootView = inflater.inflate(R.layout.fragment_administration, container, false);
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
        nameAdmin = getArguments().getString("nameAdmin");

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        btnScan = (Button) rootView.findViewById(R.id.btnScan);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);

        tvTime.setText(getArguments().getString("time"));
        tvDoublecheck.setOnClickListener(this);
        tvPreparation.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvAdministrationAdapter);
        buildAdministrationAdapter = new BuildAdministrationAdapter();
        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        checkType = "Administration";

        if(nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        }
        else {
            if(nameAdmin != null){
                tvUserName.setText("ตรวจสอบยาโดย  " + nameAdmin);
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

    private void loadCacheDao(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
        String data = prefs.getString("patientadministration",null);

        if(data != null){
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildAdministrationAdapter.setDao(dao);
            listView.setAdapter(buildAdministrationAdapter);
            btnScan.setVisibility(getView().VISIBLE);
            btnScan.setOnClickListener(this);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    Intent intent = new Intent(getContext(), AdministrationForPatientActivity.class);
//                    intent.putExtra("nfcUId", nfcUID);
//                    intent.putExtra("sdlocId", sdlocID);
//                    intent.putExtra("wardname", wardName);
//                    intent.putExtra("RFID", RFID);
//                    intent.putExtra("firstname", firstName);
//                    intent.putExtra("lastname", lastName);
//                    intent.putExtra("timeposition", timeposition);
//                    intent.putExtra("position", position);
//                    intent.putExtra("time", time);
//                    getActivity().startActivity(intent);
//                }
//            });
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

    private void saveCachePatientAdminData(ListPatientDataDao listPatientDataDao){
        String json = new Gson().toJson(listPatientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientadministration",json);
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(data != null) {
                message = data.getStringExtra("MESSAGE");
                Log.d("check", "message = " + message);
                SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
                String patient = prefs.getString("patientadministration", null);
                if (patient != null) {
                    ListPatientDataDao dao = new Gson().fromJson(patient, ListPatientDataDao.class);
                    for (int i=0; i< dao.getPatientDao().size(); i++) {
                        if (dao.getPatientDao().get(i).getMRN().equals(message)) {
                            Log.d("check", i+" MRN = " + dao.getPatientDao().get(i).getMRN());
                            found = true;
                            num = i;
                            break;
                        } else
                            found = false;
                    }
                    if (found) {
                        Intent intent = new Intent(getContext(), AdministrationForPatientActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("timeposition", timeposition);
                        intent.putExtra("position", num);
                        intent.putExtra("time", time);
                        getActivity().startActivity(intent);
                    } else
                        Toast.makeText(getActivity(), "Not found!", Toast.LENGTH_LONG).show();
                }
            }
        }
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
        else if (view.getId() == R.id.tvPreparation){
            Intent intent = new Intent(getContext(), PreparationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
        }
        else if (view.getId() == R.id.btnScan){
            Intent intent = new Intent(getContext(), CameraScanActivity.class);
            startActivityForResult(intent, 1);
        }
        else if (view.getId() == R.id.btnLogin){
            Intent intent = new Intent(getContext(), LoginAdministrationActivity.class);
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
            saveCachePatientAdminData(dao);
            buildAdministrationAdapter.setDao(dao);
            listView.setAdapter(buildAdministrationAdapter);
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "Admin PatientLoadCallback Failure " + t);
        }
    }

    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("บริหารยาโดย  " + firstName + " " + lastName);
            tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
            Toast.makeText(getActivity(), ""+firstName+" "+lastName, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Admin PersonWardLoadCallback Failure " + t);
        }
    }
}
