package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class PreparationForPatientListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvDosage, tvType, tvRoute, tvFrequency, tvSite;
    private CheckBox chkCheckMed;
    private ImageView imgvNote;

    public PreparationForPatientListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public PreparationForPatientListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public PreparationForPatientListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public PreparationForPatientListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_preparation_for_patient, this);
    }

    private void initInstances() {
        // findViewById here
        tvDrugName = (TextView) findViewById(R.id.tvDrugName);
        tvDosage = (TextView) findViewById(R.id.tvDosage);
        tvType = (TextView) findViewById(R.id.tvType);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvSite = (TextView) findViewById(R.id.tvSite);
        chkCheckMed = (CheckBox) findViewById(R.id.chkCheckMed);
        imgvNote = (ImageView) findViewById(R.id.imgvNote);
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

    public void setDrugName(String textDrugName, String textDosage, String textType, String textRoute, String textFrequency, String textUnit, String textadminTime, String textSite){
        tvDrugName.setText(String.valueOf(textDrugName));
        tvDosage.setText(" Dosage: " + textDosage+" "+ String.valueOf(textUnit));
        if(textType.equals("C")) {
            tvType.setText(" Type: Continue");
        }else{
            tvType.setText(" Type: One day");
        }
        tvRoute.setText(" Route: " + textRoute);
        tvFrequency.setText(" Frequency: " + textFrequency+" ("+textadminTime+")");
        tvSite.setText(" Site: " + textSite);
    }

    public void setCheck(Boolean check){
        chkCheckMed.setChecked(check);
    }

    public CheckBox isCheck(){

        return (CheckBox)findViewById(R.id.chkCheckMed);
    }

    public ImageView imageViewNote(){
        return (ImageView) findViewById(R.id.imgvNote);
    }

}
