package th.ac.mahidol.rama.emam.adapter.history;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.history.BuildListDrugHistoryListView;

public class BuildListDrugHistoryAdapter extends BaseAdapter {
    private ListDrugCardDao dao;
    private String drugId;

    public void setDao(ListDrugCardDao dao, String drugId){
        this.dao = dao;
        this.drugId = drugId;
    }

    @Override
    public int getCount() {
        if(dao == null)
            return 0;
        if(dao.getListDrugCardDao() == null)
            return 0;
        return dao.getListDrugCardDao().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

//        BuildListDrugHistoryListView buildListDrugHistoryListView = new BuildListDrugHistoryListView(viewGroup.getContext());
//        buildListDrugHistoryListView.setDrug(dao.getListDrugCardDao().get(position), drugId);
//
//        return buildListDrugHistoryListView;

        ///////////////////////////
        //test memory.
        //////////////////////////

        BuildListDrugHistoryListView buildListDrugHistoryListView;

        if(convertView != null)
            buildListDrugHistoryListView = (BuildListDrugHistoryListView) convertView;
        else {
            buildListDrugHistoryListView = new BuildListDrugHistoryListView(viewGroup.getContext());
            buildListDrugHistoryListView.setDrug(dao.getListDrugCardDao().get(position), drugId);
        }

        Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                R.anim.up_from_bottom);
        buildListDrugHistoryListView.startAnimation(anim);

        return buildListDrugHistoryListView;

    }
}
