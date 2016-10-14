package th.ac.mahidol.rama.emam.adapter.history;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListCurrentMedDao;
import th.ac.mahidol.rama.emam.view.history.BuildCurrentMedListView;
import th.ac.mahidol.rama.emam.view.history.BuildCurrentMedTodayListView;
import th.ac.mahidol.rama.emam.view.history.BuildCurrentMedTomorrowListView;

public class BuildCurrentMedAdapter extends BaseAdapter {
    private ListCurrentMedDao dao;

    public void setDao(ListCurrentMedDao dao){
        this.dao = dao;

    }

    @Override
    public int getCount() {
        if(dao == null)
           return 0;
        if(dao.getCurrentMedDaoList() == null)
            return 0;
        return dao.getCurrentMedDaoList().size();
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
        if(position == 0) {
            BuildCurrentMedTodayListView buildCurrentMedTodayListView;
            buildCurrentMedTodayListView = new BuildCurrentMedTodayListView(viewGroup.getContext());
            buildCurrentMedTodayListView.setDrugName(dao.getCurrentMedDaoList().get(position));
            return buildCurrentMedTodayListView;
        }
        else if (position == 24){
            BuildCurrentMedTomorrowListView buildCurrentMedTomorrowListView;
            buildCurrentMedTomorrowListView = new BuildCurrentMedTomorrowListView(viewGroup.getContext());
            buildCurrentMedTomorrowListView.setDrugName(dao.getCurrentMedDaoList().get(position));
            return buildCurrentMedTomorrowListView;
        }
        else {
            BuildCurrentMedListView buildCurrentMedListView;
            buildCurrentMedListView = new BuildCurrentMedListView(viewGroup.getContext());
            buildCurrentMedListView.setDrugName(dao.getCurrentMedDaoList().get(position));
            return buildCurrentMedListView;
        }
    }
}
