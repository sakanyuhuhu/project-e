package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.PreparationForPatientListView;

public class PreparationForPatientAdapter extends BaseAdapter {
    private String[] strMedicalName;

    public PreparationForPatientAdapter(String[] listPatient) {
        this.strMedicalName = listPatient;

    }

    @Override
    public int getCount() {
        return strMedicalName.length;
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

        PreparationForPatientListView preparationForPatientListView = new PreparationForPatientListView(viewGroup.getContext());
        preparationForPatientListView.setMedicalName(strMedicalName[position]);

        return preparationForPatientListView;
    }
}
