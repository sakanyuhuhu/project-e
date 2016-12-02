package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugAdrDao;
import th.ac.mahidol.rama.emam.view.BuildDrugAdrListView;

public class BuildListDrugAdrAdapter extends BaseAdapter {
    private ListDrugAdrDao dao;
    private Context context;

    public void setDao(Context context, ListDrugAdrDao dao){
        this.dao = dao;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(dao == null)
            return 0;
        if(dao.getDrugAdrDaoList() == null)
            return 0;
        return dao.getDrugAdrDaoList().size();
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

        BuildDrugAdrListView buildDrugAdrListView;
        buildDrugAdrListView = new BuildDrugAdrListView(viewGroup.getContext());
        buildDrugAdrListView.setDrugAdr(dao.getDrugAdrDaoList().get(position));

        return buildDrugAdrListView;
    }
}
