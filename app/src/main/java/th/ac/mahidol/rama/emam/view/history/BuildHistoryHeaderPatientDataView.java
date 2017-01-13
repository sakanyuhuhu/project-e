package th.ac.mahidol.rama.emam.view.history;

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
import th.ac.mahidol.rama.emam.view.BaseCustomViewGroup;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;

public class BuildHistoryHeaderPatientDataView extends BaseCustomViewGroup {
    private TextView tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private ImageView imgPhotoPatient;

    public BuildHistoryHeaderPatientDataView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildHistoryHeaderPatientDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildHistoryHeaderPatientDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildHistoryHeaderPatientDataView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_history_header_patient_data, this);
    }

    private void initInstances() {
        tvBedNo = (TextView) findViewById(R.id.tvBedNo);
        tvPatientName = (TextView) findViewById(R.id.tvPatientName);
        tvPatientID = (TextView) findViewById(R.id.tvPatientID);
        tvHN = (TextView) findViewById(R.id.tvMrn);
        tvBirth = (TextView) findViewById(R.id.tvBirth);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
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

    public void setData(PatientDataDao dao, int position){
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

        tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
        tvPatientName.setText(dao.getInitialName()+ dao.getFirstName()+" "+dao.getLastName());
        tvPatientID.setText(dao.getIdCardNo());
        tvHN.setText("HN:" + dao.getMRN());
        tvSex.setText("เพศ:"+ dao.getGender());
        tvBirth.setText("วันเกิด:"+ dao.getDob());
        tvAge.setText("อายุ:"+ dao.getAge());
        tvStatus.setText("สถานะภาพ:"+ dao.getMaritalstatus());
    }
}
