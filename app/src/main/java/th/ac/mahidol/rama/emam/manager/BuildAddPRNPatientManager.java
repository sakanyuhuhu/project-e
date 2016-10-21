package th.ac.mahidol.rama.emam.manager;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;

public class BuildAddPRNPatientManager {
    private Context mContext;
    private ListPatientDataDao dao;
    private Date datetoDay;
    private String toDayDateCheck, tomorrowDateCheck;

    public BuildAddPRNPatientManager(){
        mContext = Contextor.getInstance().getContext();

        loadCache();
    }

    public void appendPatientDao(ListPatientDataDao newDao) {
        if(dao == null)
            dao = new ListPatientDataDao();
        if(dao.getPatientDao() == null)
            dao.setPatientDao(new ArrayList<PatientDataDao>());

        dao.getPatientDao().addAll(dao.getPatientDao().size(), newDao.getPatientDao());

        saveCache();
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", (Parcelable) dao);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        dao = savedInstanceState.getParcelable("dao");
    }

    private void saveCache( ){
        ListPatientDataDao cacheDao = new ListPatientDataDao();
        if(dao != null & dao.getPatientDao() != null)
            cacheDao.setPatientDao(dao.getPatientDao());
        String json = new Gson().toJson(cacheDao);
        SharedPreferences prefs = mContext.getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientintdata", json);
        editor.apply();

    }

    private void loadCache(){
        SharedPreferences prefs = mContext.getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata", null);
        if(data == null)
            return;
        dao = new Gson().fromJson(data,ListPatientDataDao.class);
        if(dao.getPatientDao() != null) {
            for (PatientDataDao p : dao.getPatientDao()) {
                Log.d("check", "loadCache HN = " + p.getMRN()+ "  time = "+p.getTime()+ " Date = "+p.getDate());
            }
        }
    }

    public void checkPRNPatientinDate(PatientDataDao patientDataDao){
        Log.d("check", "checkPRNPatientinDate = "+ patientDataDao.getDate());
        datetoDay = new Date();
        SimpleDateFormat sdfForCheckDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        toDayDateCheck = sdfForCheckDate.format(datetoDay);
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDateCheck = sdfForCheckDate.format(c.getTime());

        ListPatientDataDao listPatientDataDao = new ListPatientDataDao();
        if(patientDataDao.getDate().equals(toDayDateCheck) || patientDataDao.getDate().equals(tomorrowDateCheck)){
            SharedPreferences prefs = mContext.getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
            String data = prefs.getString("patientintdata", null);
            if(data != null){
                dao = new Gson().fromJson(data,ListPatientDataDao.class);
                List<PatientDataDao> patientlist = new ArrayList<>();
                for(PatientDataDao p : dao.getPatientDao()){
                    if(p.getDate().equals(toDayDateCheck) || p.getDate().equals(tomorrowDateCheck)){
                        patientlist.add(p);
                    }
                }
                listPatientDataDao.setPatientDao(patientlist);

                String json = new Gson().toJson(listPatientDataDao);
                SharedPreferences prefsPatient = mContext.getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsPatient.edit();
                editor.putString("patientintdata", json);
                editor.apply();
                Log.d("check", "json = " + json);
            }
        }
    }
}
