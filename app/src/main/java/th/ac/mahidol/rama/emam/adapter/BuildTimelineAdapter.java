package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.view.BuildTimelineDateTodayListView;
import th.ac.mahidol.rama.emam.view.BuildTimelineDateTomorrowListView;
import th.ac.mahidol.rama.emam.view.BuildTimelineListView;

public class BuildTimelineAdapter extends BaseAdapter{
    private TimelineDao exc, in;
    private String[] timeline;
    private String focustimer;
    int lastPosition = -1;

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
            BuildTimelineDateTodayListView buildTimelineDateTodayListView;
            buildTimelineDateTodayListView = new BuildTimelineDateTodayListView(viewGroup.getContext());
            buildTimelineDateTodayListView.setTime(timeline[position] ,(exc.getTimelineDao().get(position).getMrn() == null)? 0 :exc.getTimelineDao().get(position).getMrn().size(), focustimer,
                    (in.getTimelineDao().get(position).getMrn() == null)? 0 :in.getTimelineDao().get(position).getMrn().size());

            if(position > lastPosition) {
                Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                        R.anim.up_from_bottom);
                buildTimelineDateTodayListView.startAnimation(anim);
                lastPosition = position;
            }
            return buildTimelineDateTodayListView;
        }
        else if(position == 24){
            BuildTimelineDateTomorrowListView buildTimelineDateTomorrowListView;
            buildTimelineDateTomorrowListView = new BuildTimelineDateTomorrowListView(viewGroup.getContext());
            buildTimelineDateTomorrowListView.setTime(timeline[position] ,(exc.getTimelineDao().get(position).getMrn() == null)? 0 :exc.getTimelineDao().get(position).getMrn().size(), focustimer,
                    (in.getTimelineDao().get(position).getMrn() == null)? 0 :in.getTimelineDao().get(position).getMrn().size());

            if(position > lastPosition) {
                Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                        R.anim.up_from_bottom);
                buildTimelineDateTomorrowListView.startAnimation(anim);
                lastPosition = position;
            }

            return buildTimelineDateTomorrowListView;
        }
        else {
            BuildTimelineListView buildTimelineListView;
            buildTimelineListView = new BuildTimelineListView(viewGroup.getContext());
            buildTimelineListView.setTime(timeline[position] ,(exc.getTimelineDao().get(position).getMrn() == null)? 0 :exc.getTimelineDao().get(position).getMrn().size(), focustimer,
                    (in.getTimelineDao().get(position).getMrn() == null)? 0 : in.getTimelineDao().get(position).getMrn().size());

            if(position > lastPosition) {
                Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                        R.anim.up_from_bottom);
                buildTimelineListView.startAnimation(anim);
                lastPosition = position;
            }

            return buildTimelineListView;
        }
    }
}
