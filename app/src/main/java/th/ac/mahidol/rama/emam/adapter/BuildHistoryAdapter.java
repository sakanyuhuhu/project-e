package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildHistoryListView;

public class BuildHistoryAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;


    public void setDao(Context context, ListDrugCardDao dao){
        this.dao = dao;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        BuildHistoryListView buildHistoryListView = new BuildHistoryListView(viewGroup.getContext());
        buildHistoryListView.setDrugName(dao.getListDrugCardDao().get(position));

        return buildHistoryListView;
    }
}
