package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.view.TimelineDateTodayListView;
import th.ac.mahidol.rama.emam.view.TimelineDateTomorrowListView;
import th.ac.mahidol.rama.emam.view.TimelineListView;

public class BuildTimelineAdapter extends BaseAdapter{
    private TimelineDao exc, in;
    private String[] timeline;
    private String focustimer;

    public void setDao(String[] timeline, TimelineDao exc, String focustimer, TimelineDao in){
        this.timeline = timeline;
        this.exc = exc;
        this.focustimer = focustimer;
        this.in = in;
    }
    @Override
    public int getCount() {
        if(exc == null)
            return 0;
        if(exc.getTimelineDao() == null)
            return 0;
        return exc.getTimelineDao().size();
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
        if(position == 0){
            TimelineDateTodayListView timelineDateTodayListView;
            timelineDateTodayListView = new TimelineDateTodayListView(viewGroup.getContext());
            timelineDateTodayListView.setTime(timeline[position] ,(exc.getTimelineDao().get(position).getMrn() == null)? 0 :exc.getTimelineDao().get(position).getMrn().size(), focustimer,
                    (in.getTimelineDao().get(position).getMrn() == null)? 0 :in.getTimelineDao().get(position).getMrn().size());
            return timelineDateTodayListView;
        }
        else if(position == 24){
            TimelineDateTomorrowListView timelineDateTomorrowListView;
            timelineDateTomorrowListView = new TimelineDateTomorrowListView(viewGroup.getContext());
            timelineDateTomorrowListView.setTime(timeline[position] ,(exc.getTimelineDao().get(position).getMrn() == null)? 0 :exc.getTimelineDao().get(position).getMrn().size(), focustimer,
                    (in.getTimelineDao().get(position).getMrn() == null)? 0 :in.getTimelineDao().get(position).getMrn().size());
            return timelineDateTomorrowListView;
        }
        else {
            TimelineListView timelineListView;
            timelineListView = new TimelineListView(viewGroup.getContext());
            timelineListView.setTime(timeline[position] ,(exc.getTimelineDao().get(position).getMrn() == null)? 0 :exc.getTimelineDao().get(position).getMrn().size(), focustimer,
                    (in.getTimelineDao().get(position).getMrn() == null)? 0 :in.getTimelineDao().get(position).getMrn().size());
            return timelineListView;
        }
    }
}
