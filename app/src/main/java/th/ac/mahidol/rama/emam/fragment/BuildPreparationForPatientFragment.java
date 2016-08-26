package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildstatusCheckDAO.ListStatusCheckDao;
import th.ac.mahidol.rama.emam.manager.BuildDrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildPreparationForPatientFragment extends Fragment implements View.OnClickListener{
    private String nfcUID, sdlocID, wardName, toDayDate, time;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAllergy;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();;
    private Spinner spinner1;
    private ListDrugCardDao dao;
    private SQLiteManager dbHelper;
    private ListStatusCheckDao daoCheck;
    private Date datetoDay;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
    Date currentLocalTime = cal.getTime();
    DateFormat dateTimer = new SimpleDateFormat("HH:mm");

    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, int timeposition, int position, String time) {
        BuildPreparationForPatientFragment fragment = new BuildPreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("timeposition", timeposition);
        args.putInt("position", position);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    public void init(Bundle savedInstanceState){

    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("timeposition");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAllergy = (TextView) rootView.findViewById(R.id.tvDrugAllergy);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        datetoDay = new Date();
        toDayDate = simpleDateFormat.format(datetoDay);
        dateTimer.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
        tvTime.setText(time);
        tvDrugAllergy.setText("   การแพ้ยา:แตะเพื่อดูข้อมูล");

        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            Log.d("check", "dao size = "+listPatientDataDao.getPatientDao().size()+ " position = "+position);
            buildHeaderPatientDataView.setData(listPatientDataDao, position);

            if(timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Firstcheck");

                String json = new Gson().toJson(drugCardDao);
                Log.d("check", "loadMedicalData = "+json);

                Log.d("check", "AdminTimeHour = " + drugCardDao.getAdminTimeHour() + " /DrugUseDate = " + drugCardDao.getDrugUseDate() + " /MRN = " + drugCardDao.getMRN() + " /Check Type = " + drugCardDao.getCheckType());
                loadMedicalData(drugCardDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate+1);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Firstcheck");

                String json = new Gson().toJson(drugCardDao);
                Log.d("check", "loadMedicalData = "+json);
                
                Log.d("check", "AdminTimeHour = " + drugCardDao.getAdminTimeHour() + " /DrugUseDate = " + drugCardDao.getDrugUseDate() + " /MRN = " + drugCardDao.getMRN() + " /Check Type = " + drugCardDao.getCheckType());
                loadMedicalData(drugCardDao);
            }
        }

        getOnClick();

        btnSave.setOnClickListener(this);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void getOnClick(){
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                if(selectedItem.equals("ทั้งหมด")) {
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+dao.getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), dao);
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("กิน")) {
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+ buildDrugCardListManager.getDaoPO().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("ฉีด")) {
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+ buildDrugCardListManager.getDaoIV().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else{
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+ buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void saveStatusCheckDrug(){
//        dbHelper = new SQLiteManager(getContext());
//        for(StatusCheckDao d : daoCheck.getStatusCheckDaoList()){
//            dbHelper.addStatusCheck(d.getDrugID(), d.isCheckDrug(), d.isCheckDrugNote());
//        }
    }

    private void loadMedicalData(DrugCardDao drugCardDao){

        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData2(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave){
//            buildDrugCardListManager.updateCache();
        }
    }


    class DrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            Log.d("check", "DrugCardDao.size = " + dao.getListDrugCardDao().size());
            buildDrugCardListManager.setDao(dao);

            tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+dao.getListDrugCardDao().size()+")");
            buildPreparationForPatientAdapter.setDao(getContext(), dao);
            listView.setAdapter(buildPreparationForPatientAdapter);

        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "BuildPreparationForPatientFragment Failure " + t);
        }
    }

}
