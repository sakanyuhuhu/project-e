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


public class TimelineListView extends BaseCustomViewGroup {

    private TextView tvTime, tvPatienTime, tvPatienPRNTime;
    private LinearLayout bgfocus;

    public TimelineListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public TimelineListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public TimelineListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public TimelineListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    public void setTime(String textTime, int patientTime, String focustimer, int patientTime2){
        String test[] = textTime.split(":");
        if(focustimer.equals(test[0])){
            bgfocus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryWhite));
        }
        tvTime.setText(textTime);
        tvPatienTime.setText(String.valueOf(patientTime));
        tvPatienPRNTime.setText("("+patientTime2+")");
    }


}
