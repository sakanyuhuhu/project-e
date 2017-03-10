package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.history.BuildDrugHistoryListView;

public class BuildDrugHistoryAdapter extends BaseAdapter {
    private DrugCardDao dao;
    private String time;
    int lastPosition = -1;

    public void setDao(DrugCardDao dao, String time){
        this.dao = dao;
        this.time = time;
    }

    @Override
    public int getCount() {
        if(dao == null)
            return 0;
        return 1;
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        BuildDrugHistoryListView buildDrugHistoryListView = new BuildDrugHistoryListView(viewGroup.getContext());
        buildDrugHistoryListView.setDrug(dao, time);

        if(position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                    R.anim.up_from_bottom);
            buildDrugHistoryListView.startAnimation(anim);
            lastPosition = position;
        }

        return buildDrugHistoryListView;
    }
}
