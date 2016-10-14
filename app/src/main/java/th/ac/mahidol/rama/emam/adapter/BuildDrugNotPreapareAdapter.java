package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.BuildDrugNotPrepareListView;

public class BuildDrugNotPreapareAdapter extends BaseAdapter{


    public void setDao(){

    }
    @Override
    public int getCount() {

        return 0;
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
        BuildDrugNotPrepareListView buildDrugNotPrepareListView;
        buildDrugNotPrepareListView = new BuildDrugNotPrepareListView(viewGroup.getContext());
        buildDrugNotPrepareListView.setDrug();

        return buildDrugNotPrepareListView;
    }
}
