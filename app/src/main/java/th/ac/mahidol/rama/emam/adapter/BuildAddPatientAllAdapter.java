package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.view.BuildAddPatientAllListView;

public class BuildAddPatientAllAdapter extends BaseAdapter {
    private ListPatientDataDao dao;

    public void setDao(ListPatientDataDao dao){
        this.dao = dao;

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

        BuildAddPatientAllListView patientListView;
        patientListView = new BuildAddPatientAllListView(viewGroup.getContext());
        patientListView.setPatient(dao.getPatientDao().get(position).getBedID(), dao.getPatientDao().get(position).getInitialName()+
                dao.getPatientDao().get(position).getFirstName()+" "+dao.getPatientDao().get(position).getLastName(), dao.getPatientDao().get(position).getMRN());

        return patientListView;
    }
}
