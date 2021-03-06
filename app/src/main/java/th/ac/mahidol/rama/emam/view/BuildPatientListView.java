package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildPatientListView extends BaseCustomViewGroup {

    private TextView tvPatient, tvBedNo, tvMrn, tvComplete;
    private ImageView point, imgvNote, imgPhotoPatient;

    public BuildPatientListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildPatientListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildPatientListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildPatientListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_patient, this);//view_list_patient_preparation
    }

    private void initInstances() {
        // findViewById here
        tvPatient = (TextView) findViewById(R.id.tvPatient);
        tvBedNo = (TextView) findViewById(R.id.tvBedNo);
        tvMrn = (TextView) findViewById(R.id.tvMrn);
        tvComplete = (TextView) findViewById(R.id.tvComplete);
        point = (ImageView) findViewById(R.id.point);
        imgvNote = (ImageView) findViewById(R.id.imgvNote);
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
        // Restore State from bundle here
    }


    public void setPatient(PatientDataDao dao){
//        URL url = null;
//        try {
//            url = new URL(dao.getLink());
//            try {
//                Log.d("check", "url => "+url);
//                imgPhotoPatient.setImageBitmap(BitmapFactory.decodeStream(url.openConnection() .getInputStream()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        if(!(dao.getLink().equals("\\\\GW3.rama.mahidol.ac.th\\PAT_PHOTO\\no_person.jpg"))) {
            Glide.with(getContext()).load(dao.getLink()).into(imgPhotoPatient);
        }else {

            imgPhotoPatient.setImageResource(R.drawable.ramamahidol_hospital);
        }

        if(dao.getStatus() == null){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setVisibility(INVISIBLE);
            tvComplete.setVisibility(INVISIBLE);
            imgvNote.setVisibility(INVISIBLE);
        }
        else if(dao.getStatus().equals("0")){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setImageResource(R.drawable.red);
            tvComplete.setVisibility(VISIBLE);
            imgvNote.setVisibility(VISIBLE);
        }else if(dao.getStatus().equals("1")){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setImageResource(R.drawable.green);
            tvComplete.setVisibility(VISIBLE);
            imgvNote.setVisibility(INVISIBLE);
        }
        else if(dao.getStatus().equals("2")){
            tvPatient.setText(dao.getFirstName()+" "+dao.getLastName());
            tvBedNo.setText("เลขที่เตียง/ห้อง: " + dao.getBedID());
            tvMrn.setText("HN: " + dao.getMRN());
            point.setVisibility(INVISIBLE);
            tvComplete.setVisibility(VISIBLE);
            imgvNote.setVisibility(VISIBLE);
        }
    }

}
