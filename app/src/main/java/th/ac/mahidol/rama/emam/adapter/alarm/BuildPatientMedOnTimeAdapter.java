package th.ac.mahidol.rama.emam.adapter.alarm;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.alarm.BuildPatientMedOnTimeListView;

public class BuildPatientMedOnTimeAdapter extends BaseAdapter {
    private ListDrugCardDao dao;

    public void setDao(ListDrugCardDao dao) {
        this.dao = dao;

    }

    @Override
    public int getCount() {
        if (dao == null)
            return 0;
        if (dao.getListDrugCardDao() == null)
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
        BuildPatientMedOnTimeListView buildPatientMedOnTimeListView = new BuildPatientMedOnTimeListView(viewGroup.getContext());
        buildPatientMedOnTimeListView.setDrugName(dao.getListDrugCardDao().get(position));
        return buildPatientMedOnTimeListView;
    }
}
