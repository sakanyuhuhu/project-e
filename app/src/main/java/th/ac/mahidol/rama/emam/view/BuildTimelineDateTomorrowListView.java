package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildTimelineDateTomorrowListView extends BaseCustomViewGroup {

    private TextView tvTime, tvPatienTime;
    private LinearLayout bgfocus;
    private String dateTomorrow[], year[];
    private TextView tvDate, tvPatienPRNTime;
    private Date date;

    public BuildTimelineDateTomorrowListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildTimelineDateTomorrowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildTimelineDateTomorrowListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildTimelineDateTomorrowListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_timeline_date, this);
    }

    private void initInstances() {
        bgfocus = (LinearLayout) findViewById(R.id.bgfocus);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvPatienTime = (TextView) findViewById(R.id.tvPatienTime);
        tvPatienPRNTime = (TextView) findViewById(R.id.tvPatienPRNTime);
        tvDate = (TextView) findViewById(R.id.tvDate);

        date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,1);

        DateFormat dff = DateFormat.getDateInstance(DateFormat.FULL, new Locale("th","TH"));
        String tomorrowDate = dff.format(c.getTime());
        dateTomorrow = tomorrowDate.split("ที่");
        year = dateTomorrow[1].split("ค.ศ.");

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
    }

    public void setTime(String textTime, int patientTime, String focustimer, int patientTime2){
        tvDate.setText(dateTomorrow[0]+","+year[0]+","+year[1]);
        String test[] = textTime.split(":");
        if(focustimer.equals(test[0])){
            bgfocus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryWhite));
        }
        tvTime.setText(textTime);
        tvPatienTime.setText(String.valueOf(patientTime));
        tvPatienPRNTime.setText("("+patientTime2+")");
    }


}
