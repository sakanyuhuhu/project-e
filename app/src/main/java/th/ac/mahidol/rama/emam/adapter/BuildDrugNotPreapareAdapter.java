package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildDrugNotPrepareListView;

public class BuildDrugNotPreapareAdapter extends BaseAdapter{
    private ListDrugCardDao dao;
    int lastPosition = -1;

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
    public View getView(int position, View view, ViewGroup viewGroup) {
        BuildDrugNotPrepareListView buildDrugNotPrepareListView;
        buildDrugNotPrepareListView = new BuildDrugNotPrepareListView(viewGroup.getContext());
        buildDrugNotPrepareListView.setDrug(dao.getListDrugCardDao().get(position));

        if(position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                    R.anim.up_from_bottom);
            buildDrugNotPrepareListView.startAnimation(anim);
            lastPosition = position;
        }

        return buildDrugNotPrepareListView;
    }
}
