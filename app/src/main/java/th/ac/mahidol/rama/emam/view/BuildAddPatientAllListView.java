package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildAddPatientAllListView extends BaseCustomViewGroup {

    private TextView tvPatient, tvBedNo, tvMrn;
    private ImageView imgPhotoPatient;

    public BuildAddPatientAllListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildAddPatientAllListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildAddPatientAllListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildAddPatientAllListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_patient_all, this);
    }

    private void initInstances() {
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

    public void setPatient(PatientDataDao dao){
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
        tvPatient.setText(dao.getFirstName() + " " + dao.getLastName());
        tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
        tvMrn.setText("HN: " + dao.getMRN());
    }
}
