package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildAddPatientAllPRNListView extends BaseCustomViewGroup {

    private TextView tvPatient, tvBedNo, tvMrn;
    private LinearLayout bg;
    private ImageView imgPhotoPatient;

    public BuildAddPatientAllPRNListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildAddPatientAllPRNListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildAddPatientAllPRNListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildAddPatientAllPRNListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_patient_all, this);
    }

    private void initInstances() {
        bg = (LinearLayout) findViewById(R.id.bg);
        tvPatient = (TextView) findViewById(R.id.tvPatient);
        tvBedNo = (TextView) findViewById(R.id.tvBedNo);
        tvMrn = (TextView) findViewById(R.id.tvMrn);
        imgPhotoPatient = (ImageView) findViewById(R.id.imgPhotoPatient);
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

    public void setPatient(PatientDataDao dao, TimelineDao timelineDao) {
        /////////
        //start test call link photo
        /////////
        URL url = null;
        try {
            url = new URL(dao.getLink());
            try {
                imgPhotoPatient.setImageBitmap(BitmapFactory.decodeStream(url.openConnection() .getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        /////////
        //end
        /////////

        if (timelineDao != null){
            for (MrnTimelineDao m : timelineDao.getTimelineDao()) {
                for (String s : m.getMrn()) {
                    if (dao.getMRN().equals(s)) {
                        bg.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                        tvPatient.setText(dao.getFirstName() + " " + dao.getLastName());
                        tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
                        tvMrn.setText("HN: " + dao.getMRN());
                    } else {
                        tvPatient.setText(dao.getFirstName() + " " + dao.getLastName());
                        tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
                        tvMrn.setText("HN: " + dao.getMRN());
                    }
                }
            }
        }
        tvPatient.setText(dao.getFirstName() + " " + dao.getLastName());
        tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
        tvMrn.setText("HN: " + dao.getMRN());
    }
}
