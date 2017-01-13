package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildAdministrationForPatientListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvDosage, tvType, tvRoute, tvFrequency, tvSite, tvComplete;
    private CheckBox chkCheckDrug;
    private ImageView imgvNote;
    private LinearLayout bg;

    public BuildAdministrationForPatientListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildAdministrationForPatientListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildAdministrationForPatientListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildAdministrationForPatientListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_administration_for_patient, this);
    }

    private void initInstances() {
        bg = (LinearLayout) findViewById(R.id.bg);
        tvDrugName = (TextView) findViewById(R.id.tvDrugName);
        tvDosage = (TextView) findViewById(R.id.tvDosage);
        tvType = (TextView) findViewById(R.id.tvType);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvSite = (TextView) findViewById(R.id.tvSite);
        chkCheckDrug = (CheckBox) findViewById(R.id.chkCheckDrug);
        tvComplete = (TextView) findViewById(R.id.tvComplete);
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

    }

    public void setDrugName(DrugCardDao dao, String statusPatient, List<String> listHAD){
        /////////////////////
        //start
        ////////////////////
//        if (dao.getComplete() != null & statusPatient != null) {
//            if (dao.getComplete().equals("1")) {
//                tvComplete.setText("เตรียมยาสมบูรณ์");
//                tvComplete.setTextColor(getResources().getColor(R.color.colorPrimary));
//            } else {
//                tvComplete.setText("เตรียมยาไม่ได้");
//                tvComplete.setTextColor(getResources().getColor(R.color.colorRed));
//            }
//        }
//
//        if(listHAD != null) {
//            for (String s : listHAD) {
//                if (dao.getDrugID().equals(s)) {
//                    tvDrugName.setText(String.valueOf(dao.getTradeName()));
//                    tvDrugName.setTextColor(getResources().getColor(R.color.colorRed));
//                }
//            }
//        }

        if (dao.getComplete() != null & statusPatient != null) {
            if (dao.getComplete().equals("1")) {
                tvComplete.setText("เตรียมยาสมบูรณ์");
                tvComplete.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tvComplete.setText("เตรียมยาไม่ได้");
                tvComplete.setTextColor(getResources().getColor(R.color.colorRed));
            }
        }

        if(listHAD != null) {
            for (String s : listHAD) {
                if (dao.getDrugID().equals(s)) {
                    tvDrugName.setText(String.valueOf(dao.getTradeName()));
                    tvDrugName.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        }

        /////////////////////
        //stop
        ////////////////////
        if(dao.getRoute().equals("PO") & dao.getPrn().equals("0")) {
            bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText(Html.fromHtml("Dosage: <b>" + dao.getDose() + " " + String.valueOf(dao.getUnit())+"</b>"));
            if (dao.getAdminType().equals("C")) {
                tvType.setText("Type: Continue");
            } else {
                tvType.setText("Type: One day");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText(Html.fromHtml("Frequency: <b>" + dao.getFrequency() + " (" + dao.getAdminTime() + ")</b>"));
            tvSite.setText("Site: " + dao.getSite());
        }
        else if(dao.getRoute().equals("IV") & dao.getPrn().equals("0")){
            bg.setBackgroundColor(getResources().getColor(R.color.colorPink));
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText(Html.fromHtml("Dosage: <b>" + dao.getDose() + " " + String.valueOf(dao.getUnit())+"</b>"));
            if (dao.getAdminType().equals("C")) {
                tvType.setText("Type: Continue");
            } else {
                tvType.setText("Type: One day");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText(Html.fromHtml("Frequency: <b>" + dao.getFrequency() + " (" + dao.getAdminTime() + ")</b>"));
            tvSite.setText("Site: " + dao.getSite());
        }
        else {
            bg.setBackgroundColor(getResources().getColor(R.color.colorBluesky));
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText(Html.fromHtml("Dosage: <b>" + dao.getDose() + " " + String.valueOf(dao.getUnit())+"</b>"));
            if (dao.getAdminType().equals("C")) {
                tvType.setText("Type: Continue");
            } else {
                tvType.setText("Type: One day");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText(Html.fromHtml("Frequency: <b>" + dao.getFrequency() + " (" + dao.getAdminTime() + ")</b>"));
            tvSite.setText("Site: " + dao.getSite());
        }
    }

    public CheckBox isCheck(){
        return (CheckBox)findViewById(R.id.chkCheckDrug);
    }

    public ImageView imageViewNote(String statusPatient, String getComplete){
        /////////////////////
        //start
        ////////////////////
        if (statusPatient != null & getComplete != null) {
            if (statusPatient.equals("1") & getComplete.equals("1")) {
                imgvNote.setEnabled(false);
            } else if (statusPatient.equals("0") & getComplete.equals("1")) {
                imgvNote.setEnabled(false);
            } else {
                imgvNote.setImageResource(R.drawable.notechange);
            }
        }
        /////////////////////
        //stop
        ////////////////////
        return imgvNote;
    }

    public ImageView setChangeNote(String checkNote){
        /////////////////////
        //start
        ////////////////////
        if(checkNote != null) {
            if (checkNote.equals("0"))
                imgvNote.setImageResource(R.drawable.note);
            else if (checkNote.equals("1"))
                imgvNote.setImageResource(R.drawable.notechange);
        }
        /////////////////////
        //stop
        ////////////////////

        return imgvNote;

    }

}
