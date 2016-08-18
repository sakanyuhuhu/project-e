package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPreparationForPatientFragment extends Fragment {
    private String nfcUID, sdlocID, strdate;
    private int position, yearNow;
    private ListView listView;
    private TextView tvDrugAllergy, tvDate;
    private Date date;
    private TextView tvTime, tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private Button btnCancel, btnSave;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;

    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUId, String sdlocId, int position) {
        BuildPreparationForPatientFragment fragment = new BuildPreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putInt("position", position);
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
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        position = getArguments().getInt("position");


        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        strdate = simpleDateFormat.format(date);

        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDate.setText("  " + strdate );
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvTime.setText(position);
        tvBedNo = (TextView) rootView.findViewById(R.id.tvBedNo);
        tvPatientName = (TextView) rootView.findViewById(R.id.tvPatientName);
        tvPatientID = (TextView) rootView.findViewById(R.id.tvPatientID);
        tvHN = (TextView) rootView.findViewById(R.id.tvMrn);
        tvBirth = (TextView) rootView.findViewById(R.id.tvBirth);
        tvAge = (TextView) rootView.findViewById(R.id.tvAge);
        tvSex = (TextView) rootView.findViewById(R.id.tvSex);
        tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
        tvDrugAllergy = (TextView) rootView.findViewById(R.id.tvDrugAllergy);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            setPatientData(listPatientDataDao);
            DrugCardDao drugCardDao = new DrugCardDao();
            drugCardDao.setId(0);
            drugCardDao.setAdminTimeHour(String.valueOf(position));
            drugCardDao.setDrugUseDate(strdate);
//            medicalCardDao.setTradeName("");
            drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
//            medicalCardDao.setDose("");
//            medicalCardDao.setUnit("");
//            medicalCardDao.setRoute("");
//            medicalCardDao.setFrequency("");
//            medicalCardDao.setAdminType("");
//            medicalCardDao.setMethod("");
//            medicalCardDao.setStatus("");
//            medicalCardDao.setDrugID("");
//            medicalCardDao.setRegisterDate("");
//            medicalCardDao.setLastVisited("");
//            medicalCardDao.setLastUpdated("");
//            medicalCardDao.setOrderId("");
//            medicalCardDao.setPropHelp("");
//            medicalCardDao.setSite("");
//            medicalCardDao.setRegisterDateOnly("");

            loadMedicalData(drugCardDao);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void setPatientData(ListPatientDataDao listPatientDataDao){
        String sex, strAge, stryear;
        int yearBirth, age = 0;
        stryear = strdate.substring(0,4);
        yearNow = Integer.parseInt(stryear);
        if(listPatientDataDao.getPatientDao().get(position).equals("M"))
            sex = "ชาย";
        else
            sex = "หญิง";
        strAge = listPatientDataDao.getPatientDao().get(position).getDob().trim();
        if(strAge.equals("")){
            stryear = strAge;
        }
        else{
            stryear = strAge.substring(6);
            yearBirth = Integer.parseInt(stryear);
            age = yearNow - yearBirth;
        }
        tvBedNo.setText("เลขที่เตียง/ห้อง: " + listPatientDataDao.getPatientDao().get(position).getBedID());
        tvPatientName.setText(listPatientDataDao.getPatientDao().get(position).getInitialName()
                +listPatientDataDao.getPatientDao().get(position).getFirstName()+" "+listPatientDataDao.getPatientDao().get(position).getLastName());
        tvPatientID.setText(listPatientDataDao.getPatientDao().get(position).getIdCardNo());
        tvHN.setText("HN:" + listPatientDataDao.getPatientDao().get(position).getMRN());
        tvSex.setText("เพศ:"+ sex);
        tvBirth.setText("วันเกิด:"+ listPatientDataDao.getPatientDao().get(position).getDob().trim());
        if(stryear.equals("")){
            tvAge.setText("อายุ:"+stryear+"ปี,"+" เดือน,"+" วัน");
        }else{
            Log.d("check", "AGE : "+String.valueOf(age));
            tvAge.setText("อายุ:"+String.valueOf(age)+"ปี,"+" เดือน,"+" วัน");
        }
        tvStatus.setText("สถานะภาพ:"+ listPatientDataDao.getPatientDao().get(position).getMaritalstatus());
        tvDrugAllergy.setText("   การแพ้ยา:แตะสำหรับดูข้อมูล");
    }

    private void saveCache(ListDrugCardDao listDrugCardDao){
        String json = new Gson().toJson(listDrugCardDao);
        SharedPreferences prefs = getContext().getSharedPreferences("medicaldataperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("medicaldataperson",json);
        editor.apply();
    }

    private void loadMedicalData(DrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }

    class DrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            ListDrugCardDao dao = response.body();
            saveCache(dao);
            Log.d("check", "DAO MED = " + dao);
            buildPreparationForPatientAdapter.setDao(dao);
            listView.setAdapter(buildPreparationForPatientAdapter);
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "BuildPreparationForPatientFragment Failure " + t);
        }
    }

}
