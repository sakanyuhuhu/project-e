package th.ac.mahidol.rama.emam.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import th.ac.mahidol.rama.emam.view.TimelineListView;

public class TimelineAdapter extends BaseAdapter{

    private String[] strTimeline;
    public TimelineAdapter(String[] listTimeline) {

        this.strTimeline = listTimeline;
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
//        Log.d("check", "strTimeline[position] : "+strTimeline[position]);
        timelineListView = new TimelineListView(viewGroup.getContext());
        timelineListView.setTime(strTimeline[position]);

        return timelineListView;
    }
}
