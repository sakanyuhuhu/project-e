package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildDrugHistoryListView;

public class BuildDrugHistoryAdapter extends BaseAdapter {
    private DrugCardDao dao;

    public void setDao(DrugCardDao dao){
        this.dao = dao;
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
        buildDrugHistoryListView.setDrugAdr(dao);

        return buildDrugHistoryListView;
    }
}
