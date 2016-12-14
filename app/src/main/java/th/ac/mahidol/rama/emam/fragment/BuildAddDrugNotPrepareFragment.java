package th.ac.mahidol.rama.emam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildDrugNotPreapareAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildAddDrugNotPrepareFragment extends Fragment{
    private String nfcUID, wardID, sdlocID, wardName, time, RFID, firstName, lastName, prn, mrn, checkType, date, toDayDate, tomorrowDate;
    private int timeposition;
    private ListView listView;
    private TextView tvTime;
    private BuildDrugNotPreapareAdapter buildDrugNotPreapareAdapter;
    private PatientDataDao patientDao;
    private Date datetoDay;
    private ListDrugCardDao dao;


    public BuildAddDrugNotPrepareFragment() {
        super();
    }

    public static BuildAddDrugNotPrepareFragment newInstance(String nfcUID, String wardID, String sdlocID, String wardName, int timeposition, String time, String RFID, String firstName, String lastName, String prn, String mrn, String checkType, String date, PatientDataDao patientDao) {
        BuildAddDrugNotPrepareFragment fragment = new BuildAddDrugNotPrepareFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("RFID", RFID);
        args.putString("firstname", firstName);
        args.putString("lastname", lastName);
        args.putString("prn", prn);
        args.putString("mrn", mrn);
        args.putString("checkType", checkType);
        args.putString("date", date);
        args.putParcelable("patientDao", patientDao);
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
        View rootView = inflater.inflate(R.layout.fragment_prepare_not_drug_add, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");
        RFID = getArguments().getString("RFID");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        prn = getArguments().getString("prn");
        mrn = getArguments().getString("mrn");
        checkType = getArguments().getString("checkType");
        date = getArguments().getString("date");
        patientDao = getArguments().getParcelable("patientDao");

        listView = (ListView) rootView.findViewById(R.id.lvDrugNotPreapare);
        buildDrugNotPreapareAdapter = new BuildDrugNotPreapareAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvTime.setText("เลือกยาเพื่อนำมาบริหารในมื้อนี้ ("+time+")");

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());
        tvTime.setText(time);

//        loadDrugNotPrepare(mrn, checkType, date);

        if(patientDao != null){
            if(timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(patientDao.getMRN());
                drugCardDao.setCheckType("First Check");

                loadMedicalData(drugCardDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(patientDao.getMRN());
                drugCardDao.setCheckType("First Check");

                loadMedicalData(drugCardDao);
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK){
                    Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("wardId", wardID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("RFID", RFID);
                    intent.putExtra("firstname", firstName);
                    intent.putExtra("lastname", lastName);
                    intent.putExtra("position", timeposition);
                    intent.putExtra("time", time);
                    intent.putExtra("patientDao", patientDao);
                    intent.putExtra("prn", prn);
                    getActivity().startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadDrugNotPrepare(String mrn, String checkType, String date){

    }

    private void loadMedicalData(DrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }




    class DrugLoadCallback implements Callback<ListDrugCardDao> {

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            Log.d("check", "dao = "+dao.getListDrugCardDao().size());
            buildDrugNotPreapareAdapter.setDao(dao);
            listView.setAdapter(buildDrugNotPreapareAdapter);
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "DrugLoadCallback Failure " + t);
        }
    }
}
