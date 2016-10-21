package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildPreparationListView extends BaseCustomViewGroup {

    private TextView tvPatient, tvBedNo, tvMrn, tvComplete;
    private ImageView point, imgvNote;

    public BuildPreparationListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildPreparationListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildPreparationListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildPreparationListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_patient_preparation, this);
    }

    private void initInstances() {
        // findViewById here
        tvPatient = (TextView) findViewById(R.id.tvPatient);
        tvBedNo = (TextView) findViewById(R.id.tvBedNo);
        tvMrn = (TextView) findViewById(R.id.tvMrn);
        tvComplete = (TextView) findViewById(R.id.tvComplete);
        point = (ImageView) findViewById(R.id.point);
        imgvNote = (ImageView) findViewById(R.id.imgvNote);
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

    public void setPatient(PatientDataDao dao){
//        tvPatient.setText(textPatient);
//        tvBedNo.setText("เลขที่เตียง/ห้อง: " + textBedNo);
//        tvMrn.setText("HN: " + textMrn);

        if(dao.getComplete() == null){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setVisibility(INVISIBLE);
            tvComplete.setVisibility(INVISIBLE);
            imgvNote.setVisibility(INVISIBLE);
        }
        else if(dao.getComplete().equals("0")){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setImageResource(R.drawable.red);
            tvComplete.setVisibility(VISIBLE);
            imgvNote.setVisibility(VISIBLE);
        }else if(dao.getComplete().equals("1")){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setImageResource(R.drawable.green);
            tvComplete.setVisibility(VISIBLE);
            imgvNote.setVisibility(INVISIBLE);
        }
    }

}
