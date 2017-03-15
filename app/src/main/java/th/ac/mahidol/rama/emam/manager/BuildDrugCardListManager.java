package th.ac.mahidol.rama.emam.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;


public class BuildDrugCardListManager {

    private Context mContext;
    private ListDrugCardDao dao, daoAll, daoPO, daoIV, daoOTHER;

    public BuildDrugCardListManager( ){
        mContext = Contextor.getInstance().getContext();

        loadCache();
    }


    public void setDao(ListDrugCardDao dao){
        this.dao = dao;
        saveCache();
        if(dao != null & dao.getListDrugCardDao() != null){
            daoAll = new ListDrugCardDao();
            daoPO = new ListDrugCardDao();
            daoIV = new ListDrugCardDao();
            daoOTHER = new ListDrugCardDao();
            List<DrugCardDao> drugCardDaoAll = new ArrayList<DrugCardDao>();
            List<DrugCardDao> drugCardDaoPO = new ArrayList<DrugCardDao>();
            List<DrugCardDao> drugCardDaoIV = new ArrayList<DrugCardDao>();
            List<DrugCardDao> drugCardDaoOTHER = new ArrayList<DrugCardDao>();
            for(DrugCardDao d : dao.getListDrugCardDao()){
                drugCardDaoAll.add(d);
                if(d.getRoute().equals("PO")){
                    drugCardDaoPO.add(d);
                }
                else if(d.getRoute().equals("IV")){
                    drugCardDaoIV.add(d);
                }
                else{
                    drugCardDaoOTHER.add(d);
                }
            }
            daoPO.setListDrugCardDao(drugCardDaoPO);
            daoIV.setListDrugCardDao(drugCardDaoIV);
            daoOTHER.setListDrugCardDao(drugCardDaoOTHER);
            daoAll.setListDrugCardDao(drugCardDaoAll);
        }
    }

    public ListDrugCardDao getDaoAll(){
        return daoAll;
    }

    public ListDrugCardDao getDaoPO(){
        return daoPO;
    }

    public ListDrugCardDao getDaoIV(){
        return daoIV;
    }

    public ListDrugCardDao getDaoOTHER(){
        return daoOTHER;
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
        ListDrugCardDao cacheDao = new ListDrugCardDao();
        if(dao != null & dao.getListDrugCardDao() != null)
            cacheDao.setListDrugCardDao(dao.getListDrugCardDao());
        String json = new Gson().toJson(cacheDao);
        SharedPreferences prefs = mContext.getSharedPreferences("drugdataperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("drugdataperson",json);
        editor.apply();

    }

    private void loadCache(){
        SharedPreferences prefs = mContext.getSharedPreferences("drugdataperson", Context.MODE_PRIVATE);
        String data = prefs.getString("drugdataperson", null);
        if(data == null)
            return;
        dao = new Gson().fromJson(data,ListDrugCardDao.class);
    }

}
