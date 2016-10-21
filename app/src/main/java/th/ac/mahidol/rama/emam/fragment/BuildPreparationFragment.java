package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.LoginPreparationActivity;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListStatusPreparationDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.StatusPreparationDao;
import th.ac.mahidol.rama.emam.manager.BuildAddPRNPatientManager;
import th.ac.mahidol.rama.emam.manager.BuildStatusPraparationManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildPreparationFragment extends Fragment implements View.OnClickListener{
    private String sdlocID, nfcUID, wardName, time, firstName, lastName, RFID, toDayDate, checkType, tomorrowDate, namePrepare, prn, toDayDateCheck, tomorrowDateCheck;
    private int timeposition;
    private ListView listView;
    private TextView tvUserName, tvTime, tvDoublecheck, tvAdministration;
    private BuildPreparationAdapter buildPreparationAdapter;
    private Button btnLogin;
    private Date datetoDay;
    private ListPatientDataDao dao, daoPrn;
    private ListStatusPreparationDao daoStatus;
    private BuildStatusPraparationManager buildStatusPraparationManager = new BuildStatusPraparationManager();
    private BuildAddPRNPatientManager buildAddPRNPatientManager = new BuildAddPRNPatientManager();

    public BuildPreparationFragment() {
        super();
    }



    public static BuildPreparationFragment newInstance(String nfcUId, String sdlocId, String wardName, int timeposition, String time, String namePrepare, String prn) {
        BuildPreparationFragment fragment = new BuildPreparationFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("wardname", wardName);
        args.putInt("timeposition", timeposition);
        args.putString("time", time);
        args.putString("namePrepare", namePrepare);
        args.putString("prn", prn);
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
        timeposition = getArguments().getInt("timeposition");
        time = getArguments().getString("time");
        namePrepare = getArguments().getString("namePrepare");
        prn = getArguments().getString("prn");

        Log.d("check", "BuildPreparationFragment nfcUId = "+ nfcUID +" /sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+timeposition+" /time = "+time+" /namePrepare = "+namePrepare+" /prn = "+prn);

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);

        tvTime.setText(time);
        btnLogin.setOnClickListener(this);
        tvDoublecheck.setOnClickListener(this);
        tvAdministration.setOnClickListener(this);

        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        buildPreparationAdapter = new BuildPreparationAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdfForCheckDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        toDayDateCheck = sdfForCheckDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());
        tomorrowDateCheck = sdfForCheckDate.format(c.getTime());

        checkType = "First Check";

        if(nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
            loadCacheDao();
        }
        else {
            if(namePrepare != null) {
                loadCacheDao();
                tvUserName.setText("จัดเตรียมยาโดย  " + namePrepare);
                tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            else {
                if (timeposition <= 23) {
                    loadPatientData(sdlocID, time, checkType, toDayDate);
                    StatusPreparationDao statusPreparationDao = new StatusPreparationDao();
                    statusPreparationDao.setDate(toDayDateCheck);
                    buildStatusPraparationManager.checkPatientinDate(statusPreparationDao);

//                    PatientDataDao patientDataDao = new PatientDataDao();
//                    patientDataDao.setTime(time);
//                    patientDataDao.setDate(toDayDateCheck);
//                    buildAddPRNPatientManager.checkPRNPatientinDate(patientDataDao);
                }
                else {
                    loadPatientData(sdlocID, time, checkType, tomorrowDate);
                    StatusPreparationDao statusPreparationDao = new StatusPreparationDao();
                    statusPreparationDao.setDate(tomorrowDateCheck);
                    buildStatusPraparationManager.checkPatientinDate(statusPreparationDao);

//                    PatientDataDao patientDataDao = new PatientDataDao();
//                    patientDataDao.setTime(time);
//                    patientDataDao.setDate(tomorrowDateCheck);
//                    buildAddPRNPatientManager.checkPRNPatientinDate(patientDataDao);
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
        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            Log.d("check", "*************************************************timeposition = "+timeposition);
            ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
            listPatientDataDao = loadCacheStatusCheckDao(dao);

            buildPreparationAdapter.setDao(listPatientDataDao);
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
                    intent.putExtra("namePrepare", namePrepare);
                    intent.putExtra("prn", prn);
                    getActivity().startActivity(intent);
                }
            });
        }
    }

    private ListPatientDataDao loadCacheStatusCheckDao(ListPatientDataDao dao){
//      check status complete in patientData
        SharedPreferences prefStatus = getContext().getSharedPreferences("statuspreparation", Context.MODE_PRIVATE);
        String dataStatus = prefStatus.getString("statuspreparation", null);

        ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
        List<PatientDataDao> patientDataDaoList = new ArrayList<>();
        PatientDataDao patientDataDao = new PatientDataDao();

        if(dataStatus != null) {
            daoStatus = new Gson().fromJson(dataStatus, ListStatusPreparationDao.class);
            if(daoStatus.getStatusPreparationDaoList() != null) {
                for (PatientDataDao p : dao.getPatientDao()) {
                    for (StatusPreparationDao s : daoStatus.getStatusPreparationDaoList()) {
                        if (p.getMRN().equals(s.getHn()) & time.equals(s.getTime()) & (toDayDateCheck.equals(s.getDate()) || tomorrowDateCheck.equals(s.getDate()))) {
                            p.setComplete(s.getComplete());
                            patientDataDao = p;
                            patientDataDaoList.add(patientDataDao);
                        }
                    }
                }
            }
            listPatientDataDao.setPatientDao(patientDataDaoList);

            List<String> checkUniqeHN = new ArrayList<>();
            List<PatientDataDao> patientDaoHN = new ArrayList<>();
            for (PatientDataDao p : listPatientDataDao.getPatientDao()) {
                checkUniqeHN.add(p.getMRN());
                patientDaoHN.add(p);
            }

            for (PatientDataDao p : dao.getPatientDao()) {
                if (!checkUniqeHN.contains(p.getMRN())) {
                    checkUniqeHN.add(p.getMRN());
                    patientDaoHN.add(p);
                }
            }
            Collections.sort(patientDaoHN, new Comparator<PatientDataDao>() {
                @Override
                public int compare(PatientDataDao object1, PatientDataDao object2) {
                    return object1.getBedID().compareTo(object2.getBedID());
                }

                @Override
                public boolean equals(Object object) {
                    return false;
                }
            });

            dao.setPatientDao(patientDaoHN);
            for(PatientDataDao p : dao.getPatientDao()){
                Log.d("check", "loadCacheStatusCheckDao ***** "+p.getMRN()+" ***** Complete = "+p.getComplete());
            }
        }
        return dao;
    }

    private void loadPatientData(String sdlocID, String time, String checkType, String dayDate){
        Call<ListPatientDataDao> call = HttpManager.getInstance().getService().getPatientInfo(sdlocID, time, checkType, dayDate);
        call.enqueue(new PatientLoadCallback());
    }

    private void loadPersonWard(String nfcUID, String sdlocID){
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
            getActivity().finish();
        }
        else if(view.getId() == R.id.tvAdministration){
            Intent intent = new Intent(getContext(), AdministrationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else if(view.getId() == R.id.btnLogin){
            Intent intent = new Intent(getContext(), LoginPreparationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
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
                    Intent intent = new Intent(getContext(), TimelineActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    class PatientLoadCallback implements Callback<ListPatientDataDao>{
        @Override
        public void onResponse(Call<ListPatientDataDao> call, Response<ListPatientDataDao> response) {
            dao = response.body();
            Log.d("check", "dao patient = "+dao.getPatientDao().size());

            SharedPreferences prefs = getContext().getSharedPreferences("patientprn", Context.MODE_PRIVATE);
            String data = prefs.getString("patientprn",null);

            if(dao.getPatientDao().size() != 0){
//      add prn patient not success!
//                dao = new ListPatientDataDao();
//                List<PatientDataDao> patientDataDao = new ArrayList<>();
//                for(PatientDataDao p : dao.getPatientDao()){
//                    p.setTime(time);
//                    if(timeposition <= 23){
//                        p.setDate(toDayDateCheck);
//                    }
//                    else{
//                        p.setDate(tomorrowDateCheck);
//                    }
//                    patientDataDao.add(p);
//                }
//                dao.setPatientDao(patientDataDao);
//                buildAddPRNPatientManager.appendPatientDao(dao);

                if(data != null){
                    daoPrn = new Gson().fromJson(data,ListPatientDataDao.class);
                    if(daoPrn.getPatientDao().size() != 0) {
                        if (daoPrn.getPatientDao().get(0).getTime().equals(time)) {
                            List<String> checkUniqe = new ArrayList<>();
                            List<PatientDataDao> patientDao = new ArrayList<>();
                            for (PatientDataDao p : dao.getPatientDao()) {
                                checkUniqe.add(p.getMRN());
                                patientDao.add(p);
                            }

                            for (PatientDataDao p : daoPrn.getPatientDao()) {
                                if(!checkUniqe.contains(p.getMRN())){
                                    checkUniqe.add(p.getMRN());
                                    patientDao.add(p);
                                }
                            }
                            Collections.sort(patientDao, new Comparator<PatientDataDao>() {
                                @Override
                                public int compare(PatientDataDao object1, PatientDataDao object2) {
                                    return object1.getBedID().compareTo(object2.getBedID());
                                }

                                @Override
                                public boolean equals(Object object) {
                                    return false;
                                }
                            });

                            dao.setPatientDao(patientDao);
                            ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
                            listPatientDataDao = loadCacheStatusCheckDao(dao);
                            saveCachePatientData(listPatientDataDao);
                            buildPreparationAdapter.setDao(listPatientDataDao);
                            listView.setAdapter(buildPreparationAdapter);
                        }
                        else{
                            List<PatientDataDao> patientDao = new ArrayList<>();
                            for (PatientDataDao p : dao.getPatientDao()) {
                                patientDao.add(p);
                            }
                            dao.setPatientDao(patientDao);
                            ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
                            listPatientDataDao = loadCacheStatusCheckDao(dao);
                            saveCachePatientData(listPatientDataDao);
                            buildPreparationAdapter.setDao(listPatientDataDao);
                            listView.setAdapter(buildPreparationAdapter);
                        }
                    }
                }
                else{
                    List<PatientDataDao> patientDao = new ArrayList<>();
                    for (PatientDataDao p : dao.getPatientDao()) {
                        patientDao.add(p);
                    }
                    dao.setPatientDao(patientDao);
                    ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
                    listPatientDataDao = loadCacheStatusCheckDao(dao);
                    saveCachePatientData(listPatientDataDao);
                    buildPreparationAdapter.setDao(listPatientDataDao);
                    listView.setAdapter(buildPreparationAdapter);
                }
            }
            else if(dao.getPatientDao().size() == 0){
                if(data != null){
                    daoPrn = new Gson().fromJson(data,ListPatientDataDao.class);
                    if(daoPrn.getPatientDao().size() != 0) {
                        if (daoPrn.getPatientDao().get(0).getTime().equals(time)) {
                            List<PatientDataDao> patientDao = new ArrayList<>();
                            for (PatientDataDao p : daoPrn.getPatientDao()) {
                                patientDao.add(p);
                            }
                            dao.setPatientDao(patientDao);
                            ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
                            listPatientDataDao = loadCacheStatusCheckDao(dao);
                            saveCachePatientData(listPatientDataDao);
                            buildPreparationAdapter.setDao(listPatientDataDao);
                            listView.setAdapter(buildPreparationAdapter);
                        }
                    }
                }
            }
            else
                Toast.makeText(getActivity(), "ไม่มีผู้ป่วย", Toast.LENGTH_LONG).show();

//            before edit
//            saveCachePatientData(dao);
//            if(dao.getPatientDao().size() != 0) {
//                buildPreparationAdapter.setDao(dao);
//                listView.setAdapter(buildPreparationAdapter);
//            }
//            else
//                Toast.makeText(getActivity(), "ไม่มีผู้ป่วย", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<ListPatientDataDao> call, Throwable t) {
            Log.d("check", "Prepare PatientLoadCallback Failure " + t);
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
            tvUserName.setTextColor(getResources().getColor(R.color.colorBlack));
            Toast.makeText(getActivity(), ""+firstName+" "+lastName, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Prepare PersonWardLoadCallback Failure " + t);
        }
    }

}
