package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.PatientListView;

public class PreparationAdapter extends BaseAdapter {
    private String[] strPatient;
    public PreparationAdapter(String[] listPatient) {
        this.strPatient = listPatient;
    }

    @Override
    public int getCount() {
        return strPatient.length;
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

        PatientListView patientListView = new PatientListView(viewGroup.getContext());
//        Log.d("check", "strTimeline[position] : "+ strPatient[position]);
        patientListView.setPatient(strPatient[position]);

        return patientListView;
    }
}
