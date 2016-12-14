package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildDoubleCheckForPatientListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvDosage, tvType, tvRoute, tvFrequency, tvSite;
    private ImageView imgvNote;
    private LinearLayout bg;

    public BuildDoubleCheckForPatientListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildDoubleCheckForPatientListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildDoubleCheckForPatientListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildDoubleCheckForPatientListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_doublecheck_for_patient, this);
    }

    private void initInstances() {
        bg = (LinearLayout) findViewById(R.id.bg);
        tvDrugName = (TextView) findViewById(R.id.tvDrugName);
        tvDosage = (TextView) findViewById(R.id.tvDosage);
        tvType = (TextView) findViewById(R.id.tvType);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvSite = (TextView) findViewById(R.id.tvSite);
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

    public void setDrugName(DrugCardDao dao){
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
//        else if(dao.getPrn().equals("1")){
//            bg.setBackgroundColor(getResources().getColor(R.color.colorOrange));
//            tvDrugName.setText(String.valueOf(dao.getTradeName()));
//            tvDosage.setText(Html.fromHtml("Dosage: <b>" + dao.getDose() + " " + String.valueOf(dao.getUnit())+"</b>"));
//            if (dao.getAdminType().equals("C")) {
//                tvType.setText("Type: Continue");
//            } else {
//                tvType.setText("Type: One day");
//            }
//            tvRoute.setText("Route: " + dao.getRoute());
//            tvFrequency.setText(Html.fromHtml("Frequency: <b>" + dao.getFrequency() + " (" + dao.getAdminTime() + ")</b>"));
//            tvSite.setText("Site: " + dao.getSite());
//        }
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

    public ImageView imageViewNote(){
        return (ImageView) findViewById(R.id.imgvNote);
    }

    public void setChangeNote(){
        Log.d("check", "setChangeNote");
        imgvNote.setImageResource(R.drawable.notechange);

    }

}
