package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildTimelineListView extends BaseCustomViewGroup {

    private TextView tvTime, tvPatienTime, tvPatienPRNTime;
    private LinearLayout bgfocus;

    public BuildTimelineListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildTimelineListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildTimelineListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildTimelineListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_timeline, this);
    }

    private void initInstances() {
        // findViewById here
        bgfocus = (LinearLayout) findViewById(R.id.bgfocus);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvPatienTime = (TextView) findViewById(R.id.tvPatienTime);
        tvPatienPRNTime = (TextView) findViewById(R.id.tvPatienPRNTime);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    public void setTime(String textTime, int patientExc, String focustimer, int patientInc){
        String test[] = textTime.split(":");
        if(focustimer.equals(test[0])){
            bgfocus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryWhite));
        }
        tvTime.setText(textTime);
        tvPatienTime.setText(String.valueOf(patientExc));
        tvPatienPRNTime.setText("("+patientInc+")");
    }


}
