package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.BuildDrugAdrListView;

public class BuildListDrugAdrAdapter extends BaseAdapter {
    private String[] drugAdr, sideEffect, naranjo;

    public void setDao(String[] drugAdr, String[] sideEffect, String[] naranjo){
        this.drugAdr = drugAdr;
        this.sideEffect = sideEffect;
        this.naranjo = naranjo;
    }

    @Override
    public int getCount() {
        if(drugAdr == null)
           return 0;
        if(sideEffect == null)
            return 0;
        if(naranjo == null)
            return 0;
        return drugAdr.length;
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
        buildDrugAdrListView.setDrugAdr(drugAdr[position], sideEffect[position], naranjo[position]);

        return buildDrugAdrListView;
    }
}
