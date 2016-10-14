package th.ac.mahidol.rama.emam.view.history;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CurrentMedDao;
import th.ac.mahidol.rama.emam.view.BaseCustomViewGroup;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildCurrentMedListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvDosage, tvType, tvRoute, tvFrequency, tvSite, tvStatus;
    private LinearLayout bg;

    public BuildCurrentMedListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildCurrentMedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildCurrentMedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildCurrentMedListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_currentmed, this);
    }

    private void initInstances() {
        bg = (LinearLayout) findViewById(R.id.bg);
        tvDrugName = (TextView) findViewById(R.id.tvDrugName);
        tvDosage = (TextView) findViewById(R.id.tvDosage);
        tvType = (TextView) findViewById(R.id.tvType);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvSite = (TextView) findViewById(R.id.tvSite);
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

    public void setDrugName(CurrentMedDao dao){
        if(dao.getRoute().equals("PO") & dao.isPrn() == false) {
            bg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            tvDrugName.setText(String.valueOf(dao.getName()));
            tvDosage.setText("Dosage: " + dao.getDose() + " " + String.valueOf(dao.getUnit()));
            if (dao.getTakeaction().equals("C")) {
                tvType.setText("Type: Continue");
            }
            else if(dao.getTakeaction().equals("D")){
                tvType.setText("Type: Delete");
            }
            else if(dao.getTakeaction().equals("O")){
                tvType.setText("Type: Off");
            }
            else if(dao.getTakeaction().equals("S")){
                tvType.setText("Type: Stat Dose");
            }
            else{
                tvType.setText("Type: One day dose");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText("Frequency: " + dao.getQtytmg() + " (" + dao.getSpectime() + ")");
            tvSite.setText("Site: " + dao.getSite());
        }
        else if(dao.getRoute().equals("IV") & dao.isPrn() == false){
            bg.setBackgroundColor(getResources().getColor(R.color.colorPink));
            tvDrugName.setText(String.valueOf(dao.getName()));
            tvDosage.setText("Dosage: " + dao.getDose() + " " + String.valueOf(dao.getUnit()));
            if (dao.getTakeaction().equals("C")) {
                tvType.setText("Type: Continue");
            }
            else if(dao.getTakeaction().equals("D")){
                tvType.setText("Type: Delete");
            }
            else if(dao.getTakeaction().equals("O")){
                tvType.setText("Type: Off");
            }
            else if(dao.getTakeaction().equals("S")){
                tvType.setText("Type: Stat Dose");
            }
            else {
                tvType.setText("Type: One day");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText("Frequency: " + dao.getQtytmg() + " (" + dao.getSpectime() + ")");
            tvSite.setText("Site: " + dao.getSite());
        }
        else if(dao.isPrn() == true){
            bg.setBackgroundColor(getResources().getColor(R.color.colorOrange));
            tvDrugName.setText(String.valueOf(dao.getName()));
            tvDosage.setText("Dosage: " + dao.getDose() + " " + String.valueOf(dao.getUnit()));
            if (dao.getTakeaction().equals("C")) {
                tvType.setText("Type: Continue");
            }
            else if(dao.getTakeaction().equals("D")){
                tvType.setText("Type: Delete");
            }
            else if(dao.getTakeaction().equals("O")){
                tvType.setText("Type: Off");
            }
            else if(dao.getTakeaction().equals("S")){
                tvType.setText("Type: Stat Dose");
            }
            else {
                tvType.setText("Type: One day");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText("Frequency: " + dao.getQtytmg() + " (" + dao.getSpectime() + ")");
            tvSite.setText("Site: " + dao.getSite());
        }
        else {
            bg.setBackgroundColor(getResources().getColor(R.color.colorBluesky));
            tvDrugName.setText(String.valueOf(dao.getName()));
            tvDosage.setText("Dosage: " + dao.getDose() + " " + String.valueOf(dao.getUnit()));
            if (dao.getTakeaction().equals("C")) {
                tvType.setText("Type: Continue");
            }
            else if(dao.getTakeaction().equals("D")){
                tvType.setText("Type: Delete");
            }
            else if(dao.getTakeaction().equals("O")){
                tvType.setText("Type: Off");
            }
            else if(dao.getTakeaction().equals("S")){
                tvType.setText("Type: Stat Dose");
            }
            else {
                tvType.setText("Type: One day");
            }
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText("Frequency: " + dao.getQtytmg() + " (" + dao.getSpectime() + ")");
            tvSite.setText("Site: " + dao.getSite());
        }
    }
}
