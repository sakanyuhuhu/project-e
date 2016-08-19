package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;

public class BuildHeaderPatientDataView extends BaseCustomViewGroup {
    private TextView tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private String gender, strAge, stryear;
    private int yearBirth, age = 0;

    public BuildHeaderPatientDataView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildHeaderPatientDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildHeaderPatientDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildHeaderPatientDataView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_header_patient_data, this);
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

    public void setData(String bedID, String patirntName, String idCardNo, String hn,String gender, String dob, String maritalstatus){

        tvBedNo.setText("เลขที่เตียง/ห้อง: " + bedID);
        tvPatientName.setText(patirntName);
        tvPatientID.setText(idCardNo);
        tvHN.setText("HN:" + hn);
        tvSex.setText("เพศ:"+ gender);
        tvBirth.setText("วันเกิด:"+ dob);
//      tvAge.setText("อายุ:"+stryear+"ปี,"+" เดือน,"+" วัน");
        tvStatus.setText("สถานะภาพ:"+ maritalstatus);
    }
}
