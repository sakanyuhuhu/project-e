package th.ac.mahidol.rama.emam.adapter.alarm;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.view.alarm.BuildPatientOnTimeListView;

public class BuildPatientNextTimeAdapter extends BaseAdapter {
    private ListPatientDataDao dao;

    public void setDao(ListPatientDataDao dao) {
        this.dao = dao;

    }

    @Override
    public int getCount() {
        if (dao == null)
            return 0;
        if (dao.getPatientDao() == null)
            return 0;
        return dao.getPatientDao().size();
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

        BuildPatientOnTimeListView patientListView;
        patientListView = new BuildPatientOnTimeListView(viewGroup.getContext());
        patientListView.setPatient(dao.getPatientDao().get(position));

        return patientListView;
    }
}
