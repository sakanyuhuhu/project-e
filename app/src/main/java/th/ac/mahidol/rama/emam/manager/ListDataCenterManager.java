package th.ac.mahidol.rama.emam.manager;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.dao.ListMedicalCardCollectionDao;
import th.ac.mahidol.rama.emam.dao.ListWardCollectionDao;

public class ListDataCenterManager {
    private String[] nameWards;
    private String[] sdlocId;
    private ListWardCollectionDao listWardCollectionDao;
    private String userName;
    private String[] adminTimeHour;
    private ListMedicalCardCollectionDao listMedicalCardCollectionDao;
    private String[] MRN = new String[]{"4881611", "4002533", "4736940", "5098325", "3983069", "4813524", "5121414", "5097313", "5000589", "4750760"};

    public List<String> getListNameWard(){

        final List<String> listNameWard = new ArrayList<String>();
        Call<ListWardCollectionDao> call = HttpManager.getInstance().getService().loadListWard();
        call.enqueue(new Callback<ListWardCollectionDao>() {
            @Override
            public void onResponse(Call<ListWardCollectionDao> call, Response<ListWardCollectionDao> response) {
                listWardCollectionDao = response.body();
                nameWards = new String[listWardCollectionDao.getListwardBean().size()];
                for(int i=0; i<listWardCollectionDao.getListwardBean().size();i++) {
                    listNameWard.add(listWardCollectionDao.getListwardBean().get(i).getWardName());
                }
            }
            @Override
            public void onFailure(Call<ListWardCollectionDao> call, Throwable t) {
                Log.d("check","onFailure "+t);
            }
        });

        return listNameWard;
    }

    public List<String> getListSdlocId(){
        final List<String> listSdlocId = new ArrayList<String>();
        Call<ListWardCollectionDao> call = HttpManager.getInstance().getService().loadListWard();
        call.enqueue(new Callback<ListWardCollectionDao>() {
            @Override
            public void onResponse(Call<ListWardCollectionDao> call, Response<ListWardCollectionDao> response) {
                listWardCollectionDao = response.body();
                sdlocId = new String[listWardCollectionDao.getListwardBean().size()];
                for(int i=0; i<listWardCollectionDao.getListwardBean().size();i++) {
                    listSdlocId.add(listWardCollectionDao.getListwardBean().get(i).getSdlocId());
                }
            }
            @Override
            public void onFailure(Call<ListWardCollectionDao> call, Throwable t) {
                Log.d("check","onFailure "+t);
            }
        });

        return listSdlocId;
    }


    public void getListMedicalCard(List<String> mrn) {
        final List<String> listAdminTimeHour = new ArrayList<String>();
        final List<String> listTradeName = new ArrayList<String>();
        final List<String> listMRN = new ArrayList<String>();
//        for (int i=0; i < mrn.size(); i++) {
//            Log.d("check", i+ " mrn : " + mrn.get(i));
//            Call<ListMedicalCardCollectionDao> call = HttpManager.getInstance().getService().loadListMedicalCard(mrn.get(i));
//            call.enqueue(new Callback<ListMedicalCardCollectionDao>()
//                 {
//                     @Override
//                     public void onResponse (Call<ListMedicalCardCollectionDao> call, Response<ListMedicalCardCollectionDao> response) {
//                         listMedicalCardCollectionDao = response.body();
//                         adminTimeHour = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
//                         for (int i = 0; i < listMedicalCardCollectionDao.getListMedicalCardBean().size(); i++) {
//                             listID.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getId());
//                             listAdminTimeHour.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour());
//                             listTradeName.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName());
//                 Log.d("check", listID.get(i) + " listAdminTimeHour = " + listAdminTimeHour.get(i) + " / listTradeName: " + listTradeName.get(i));
//                         }
//                     }
//                     @Override
//                     public void onFailure(Call<ListMedicalCardCollectionDao> call, Throwable t) {
//                         Log.d("check", "onFailure " + t);
//                     }
//                 }
//            );
//        }
        for (int i=0; i< MRN.length;i++) {
            Call<ListMedicalCardCollectionDao> call = HttpManager.getInstance().getService().loadListMedicalCard(MRN[i]);
            call.enqueue(new Callback<ListMedicalCardCollectionDao>() {
                 @Override
                 public void onResponse(Call<ListMedicalCardCollectionDao> call, Response<ListMedicalCardCollectionDao> response) {
                     listMedicalCardCollectionDao = response.body();
                     adminTimeHour = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                     for (int i = 0; i < listMedicalCardCollectionDao.getListMedicalCardBean().size(); i++) {
                         listMRN.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getMRN());
                         listAdminTimeHour.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour());
                         listTradeName.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName());
                         Log.d("check", i + " / " + listMRN.get(i) + " listAdminTimeHour = " + listAdminTimeHour.get(i) + " / listTradeName: " + listTradeName.get(i));
                     }
                 }

                 @Override
                 public void onFailure(Call<ListMedicalCardCollectionDao> call, Throwable t) {
                     Log.d("check", "onFailure " + t);
                 }
             }
            );

        }
    }


}
