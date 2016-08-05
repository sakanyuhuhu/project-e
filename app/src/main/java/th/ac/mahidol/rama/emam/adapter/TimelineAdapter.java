package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.TimelineListView;

public class TimelineAdapter extends BaseAdapter{

    private String[] strTimeline;
    private int[] patientTime;
    public TimelineAdapter(String[] listTimeline, int[] patientTime) {
        this.strTimeline = listTimeline;
        this.patientTime = patientTime;
    }

    @Override
    public int getCount() {
        return strTimeline.length;
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
        timelineListView.setTime(strTimeline[position], patientTime[position]);

        return timelineListView;
    }
}
