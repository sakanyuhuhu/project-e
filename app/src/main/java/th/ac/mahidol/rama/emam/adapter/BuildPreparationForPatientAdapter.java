package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.PreparationForPatientListView;

public class BuildPreparationForPatientAdapter extends BaseAdapter {
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
    public View getView(final int position, final View convertView, ViewGroup viewGroup) {
        PreparationForPatientListView preparationForPatientListView = new PreparationForPatientListView(viewGroup.getContext());
//        preparationForPatientListView.setDrugName(strDrugName.get(position), strDosage.get(position), strType.get(position), strRoute.get(position),
//                strFrequency.get(position), strUnit.get(position), strAdminTime.get(position), strSite.get(position));

        return preparationForPatientListView;
    }
}
