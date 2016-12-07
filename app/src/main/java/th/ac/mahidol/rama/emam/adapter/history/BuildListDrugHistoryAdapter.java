package th.ac.mahidol.rama.emam.adapter.history;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildDrugHistoryListView;

public class BuildListDrugHistoryAdapter extends BaseAdapter {
    private ListDrugCardDao dao;

    public void setDao(ListDrugCardDao dao){
        this.dao = dao;
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        BuildDrugHistoryListView buildDrugHistoryListView = new BuildDrugHistoryListView(viewGroup.getContext());
        buildDrugHistoryListView.setDrugAdr(dao.getListDrugCardDao().get(position));

        return buildDrugHistoryListView;
    }
}
