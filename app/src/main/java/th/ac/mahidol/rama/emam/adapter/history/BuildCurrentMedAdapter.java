package th.ac.mahidol.rama.emam.adapter.history;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListCurrentMedDao;
import th.ac.mahidol.rama.emam.view.history.BuildCurrentMedListView;

public class BuildCurrentMedAdapter extends BaseAdapter {
    private ListCurrentMedDao dao;
    int lastPosition = -1;

    public void setDao(ListCurrentMedDao dao){
        this.dao = dao;

    }

    @Override
    public int getCount() {
        if(dao == null)
           return 0;
        if(dao.getCurrentMedDaoList() == null)
            return 0;
        return dao.getCurrentMedDaoList().size();
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

        BuildCurrentMedListView buildCurrentMedListView;
        buildCurrentMedListView = new BuildCurrentMedListView(viewGroup.getContext());
        buildCurrentMedListView.setDrugName(dao.getCurrentMedDaoList().get(position));

        if(position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                    R.anim.up_from_bottom);
            buildCurrentMedListView.startAnimation(anim);
            lastPosition = position;
        }

            return buildCurrentMedListView;
    }
}
