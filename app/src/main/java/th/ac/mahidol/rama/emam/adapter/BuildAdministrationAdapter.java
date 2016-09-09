package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.view.BuildAdministrationListView;

public class BuildAdministrationAdapter extends BaseAdapter {
    private ListPatientDataDao dao;
    private int tricker;

    public void setDao(ListPatientDataDao dao, int tricker){
        this.dao = dao;
        this.tricker = tricker;

    }

    @Override
    public int getCount() {
        if(dao == null)
           return 0;
        if(dao.getPatientDao() == null)
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

        BuildAdministrationListView patientListView;
        patientListView = new BuildAdministrationListView(viewGroup.getContext());
        patientListView.setPatient(dao.getPatientDao().get(position).getBedID(), dao.getPatientDao().get(position).getInitialName()+
                dao.getPatientDao().get(position).getFirstName()+" "+dao.getPatientDao().get(position).getLastName(), dao.getPatientDao().get(position).getMRN(), tricker);

        return patientListView;
    }
}
