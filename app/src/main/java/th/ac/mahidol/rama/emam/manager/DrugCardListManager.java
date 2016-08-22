package th.ac.mahidol.rama.emam.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;

/**
 * Created by mi- on 22/8/2559.
 */
public class DrugCardListManager {

    private Context mContext;
    private ListDrugCardDao dao, daoPO, daoIV, daoOTHER;


    public DrugCardListManager( ){
        mContext = Contextor.getInstance().getContext();

        loadCache();
    }

    public ListDrugCardDao getDao(){
        return dao;
    }

    public void setDao(ListDrugCardDao dao){
        this.dao = dao;
        saveCache();
        if(dao != null & dao.getListDrugCardDao() != null){
            daoPO = new ListDrugCardDao();
            daoIV = new ListDrugCardDao();
            daoOTHER = new ListDrugCardDao();
            List<DrugCardDao> drugCardDaoPO = new ArrayList<DrugCardDao>();
            List<DrugCardDao> drugCardDaoIV = new ArrayList<DrugCardDao>();
            List<DrugCardDao> drugCardDaoOTHER = new ArrayList<DrugCardDao>();
            for(DrugCardDao d : dao.getListDrugCardDao()){
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
        }
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
        if(dao != null & dao.getListDrugCardDao() !=null)
            cacheDao.setListDrugCardDao(dao.getListDrugCardDao());
        String json = new Gson().toJson(cacheDao);
        Log.d("check", "cacheDao = "+json);
        SharedPreferences prefs = mContext.getSharedPreferences("drugdataperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("drugdataperson",json);
        editor.apply();

    }

    private void loadCache(){
        Log.d("check", "1234");
        SharedPreferences prefs = mContext.getSharedPreferences("drugdataperson", Context.MODE_PRIVATE);
        String data = prefs.getString("drugdataperson",null);
        Log.d("check", "data = "+data);
        if(data == null)
            return;
        dao = new Gson().fromJson(data,ListDrugCardDao.class);
    }
}
