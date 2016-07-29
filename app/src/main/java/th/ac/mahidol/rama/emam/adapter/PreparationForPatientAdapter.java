package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.PreparationForPatientListView;

public class PreparationForPatientAdapter extends BaseAdapter {
    private String[] strDrugName, strDosage, unit, type, route, strFrequency;

    public PreparationForPatientAdapter(String[] listDrugName, String[] listDosage, String[] type, String[] route, String[] listFrequency, String[] unit) {
        this.strDrugName = listDrugName;
        this.strDosage = listDosage;
        this.unit = unit;
        this.type = type;
        this.route = route;
        this.strFrequency = listFrequency;

    }

    @Override
    public int getCount() {
        return strDrugName.length;
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
        preparationForPatientListView.setDrugName(strDrugName[position], strDosage[position], type[position], route[position], strFrequency[position], unit[position]);

        return preparationForPatientListView;
    }
}
