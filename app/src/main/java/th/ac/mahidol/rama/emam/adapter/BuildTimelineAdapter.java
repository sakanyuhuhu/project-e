package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.view.TimelineListView;

public class BuildTimelineAdapter extends BaseAdapter{
    private TimelineDao dao;
    private String[] timeline;
    private String focustimer;

    public void setDao(String[] timeline, TimelineDao dao, String focustimer){
        this.timeline = timeline;
        this.dao = dao;
        this.focustimer = focustimer;
    }
    @Override
    public int getCount() {
        if(dao == null)
            return 0;
        if(dao.getTimelineDao() == null)
            return 0;
        return dao.getTimelineDao().size();
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
        TimelineListView timelineListView;
        timelineListView = new TimelineListView(viewGroup.getContext());
        timelineListView.setTime(timeline[position] ,(dao.getTimelineDao().get(position).getMrn() == null)? 0 :dao.getTimelineDao().get(position).getMrn().size(), focustimer);

        return timelineListView;
    }
}
