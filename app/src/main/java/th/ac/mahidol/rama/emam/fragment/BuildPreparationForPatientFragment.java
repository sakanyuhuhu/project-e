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
import th.ac.mahidol.rama.emam.manager.DrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildPreparationForPatientFragment extends Fragment {
    private String nfcUID, sdlocID, toDayDate, time;
    private int position;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAllergy;
    private Date datetoDay;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;
    private DrugCardListManager drugCardListManager = new DrugCardListManager();;
    private Spinner spinner1;
    private ListDrugCardDao dao;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
    Date currentLocalTime = cal.getTime();
    DateFormat dateTimer = new SimpleDateFormat("HH:mm");

    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUId, String sdlocId, int position, String time) {
        BuildPreparationForPatientFragment fragment = new BuildPreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putInt("position", position);
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
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAllergy = (TextView) rootView.findViewById(R.id.tvDrugAllergy);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);

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

            DrugCardDao drugCardDao = new DrugCardDao();
            drugCardDao.setAdminTimeHour(time);
            drugCardDao.setDrugUseDate(toDayDate);
            drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
            Log.d("check", "drugCardDao AdminTimeHour = "+drugCardDao.getAdminTimeHour()+" DrugUseDate = "+drugCardDao.getDrugUseDate()+" MRN = "+drugCardDao.getMRN());

            loadMedicalData(drugCardDao);
        }
        getSpinner();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void getSpinner(){
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                if(selectedItem.equals("ทั้งหมด")) {
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+dao.getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(dao);
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("กิน")) {
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+drugCardListManager.getDaoPO().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(drugCardListManager.getDaoPO());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("ฉีด")) {
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+drugCardListManager.getDaoIV().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(drugCardListManager.getDaoIV());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else{
                    tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+drugCardListManager.getDaoOTHER().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(drugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadMedicalData(DrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }




    class DrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            Log.d("check", "DrugCardDao.size = " + dao.getListDrugCardDao().size());
            drugCardListManager.setDao(dao);

            tvDate.setText("  " + toDayDate+" "+dateTimer.format(currentLocalTime)+" (จำนวนยา "+dao.getListDrugCardDao().size()+")");
            buildPreparationForPatientAdapter.setDao(dao);
            listView.setAdapter(buildPreparationForPatientAdapter);

        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "BuildPreparationForPatientFragment Failure " + t);
        }
    }

}
