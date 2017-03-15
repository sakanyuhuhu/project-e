package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.view.BuildAddPatientAllPRNListView;

public class BuildAddPatientAllPRNAdapter extends BaseAdapter {
    private ListPatientDataDao dao;
    private TimelineDao timelineDao;
    int lastPosition = -1;

    public void setDao(ListPatientDataDao dao, TimelineDao timelineDao){
        this.dao = dao;
        this.timelineDao = timelineDao;

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        BuildAddPatientAllPRNListView patientListView;

        if(convertView != null){
            patientListView = (BuildAddPatientAllPRNListView) convertView;
        } else {
            patientListView = new BuildAddPatientAllPRNListView(viewGroup.getContext());
            patientListView.setPatient(dao.getPatientDao().get(position), timelineDao);
        }


        if(position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                    R.anim.up_from_bottom);
            patientListView.startAnimation(anim);
            lastPosition = position;
        }

        return patientListView;
    }
}
