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

import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListStatusPreparationDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.StatusPreparationDao;

public class BuildStatusPraparationManager {
    private Context mContext;
    private ListStatusPreparationDao dao;
    private Date datetoDay;
    private String toDayDateCheck, tomorrowDateCheck;

    public BuildStatusPraparationManager(){
        mContext = Contextor.getInstance().getContext();

        loadCache();
    }

    public void appendDao(ListStatusPreparationDao newDao) {
        if(dao == null)
            dao = new ListStatusPreparationDao();
        if(dao.getStatusPreparationDaoList() == null)
            dao.setStatusPreparationDaoList(new ArrayList<StatusPreparationDao>());

        dao.getStatusPreparationDaoList().addAll(dao.getStatusPreparationDaoList().size(), newDao.getStatusPreparationDaoList());

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
        ListStatusPreparationDao cacheDao = new ListStatusPreparationDao();
        if(dao != null & dao.getStatusPreparationDaoList() != null)
            cacheDao.setStatusPreparationDaoList(dao.getStatusPreparationDaoList());
        String json = new Gson().toJson(cacheDao);
        SharedPreferences prefs = mContext.getSharedPreferences("statuspreparation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("statuspreparation", json);
        editor.apply();

    }

    private void loadCache(){
        SharedPreferences prefs = mContext.getSharedPreferences("statuspreparation", Context.MODE_PRIVATE);
        String data = prefs.getString("statuspreparation", null);
        if(data == null)
            return;
        dao = new Gson().fromJson(data,ListStatusPreparationDao.class);
        if(dao.getStatusPreparationDaoList() != null) {
            for (StatusPreparationDao s : dao.getStatusPreparationDaoList()) {
                Log.d("check", "loadCache HN = " + s.getHn() + " time = " + s.getTime() + "  complete = " + s.getComplete() + " Date = " + s.getDate());
            }
        }
    }

    public void checkPatientinDate(StatusPreparationDao statusDao){
        Log.d("check", "date manager = "+ statusDao.getDate());
        datetoDay = new Date();
        SimpleDateFormat sdfForCheckDate = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        toDayDateCheck = sdfForCheckDate.format(datetoDay);
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDateCheck = sdfForCheckDate.format(c.getTime());

        ListStatusPreparationDao listStatusDao = new ListStatusPreparationDao();
        if (statusDao.getDate().equals(toDayDateCheck) || statusDao.getDate().equals(tomorrowDateCheck)) {
            SharedPreferences prefsLoad = mContext.getSharedPreferences("statuspreparation", Context.MODE_PRIVATE);
            String data = prefsLoad.getString("statuspreparation", null);
            if(data != null){
                dao = new Gson().fromJson(data,ListStatusPreparationDao.class);
                List<StatusPreparationDao> statusPreparationDaosDao = new ArrayList<>();
                for(StatusPreparationDao s : dao.getStatusPreparationDaoList()){
                    if(s.getDate().equals(toDayDateCheck) || s.getDate().equals(tomorrowDateCheck)){
                        statusPreparationDaosDao.add(s);
                    }
                }
                listStatusDao.setStatusPreparationDaoList(statusPreparationDaosDao);

                String json = new Gson().toJson(listStatusDao);
                SharedPreferences prefs = mContext.getSharedPreferences("statuspreparation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("statuspreparation", json);
                editor.apply();
                Log.d("check", "json = " + json);
            }
        }
    }
}
