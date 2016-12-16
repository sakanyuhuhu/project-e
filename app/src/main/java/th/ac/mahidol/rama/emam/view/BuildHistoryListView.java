package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildHistoryListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvDosage, tvRoute, tvFrequency, tvSite, tvMethod;
    private ImageView imgvNote;

    public BuildHistoryListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildHistoryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildHistoryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildHistoryListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_prepare_history, this);
    }

    private void initInstances() {
        tvDrugName = (TextView) findViewById(R.id.tvDrugName);
        tvDosage = (TextView) findViewById(R.id.tvDosage);
        tvRoute = (TextView) findViewById(R.id.tvRoute);
        tvFrequency = (TextView) findViewById(R.id.tvFrequency);
        tvSite = (TextView) findViewById(R.id.tvSite);
        tvMethod = (TextView) findViewById(R.id.tvMethod);
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
        if(dao.getComplete().equals("1")) {
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText("Dosage: " + dao.getDose() + " " + String.valueOf(dao.getUnit()));
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText("Frequency: " + dao.getFrequency() + " (" + dao.getAdminTime() + ")");
            tvSite.setText("Site: " + dao.getSite());
            tvMethod.setText("Method: " + dao.getMethod());
            imgvNote.setVisibility(INVISIBLE);
        }
        else {
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText("Dosage: " + dao.getDose() + " " + String.valueOf(dao.getUnit()));
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText("Frequency: " + dao.getFrequency() + " (" + dao.getAdminTime() + ")");
            tvSite.setText("Site: " + dao.getSite());
            tvMethod.setText("Method: " + dao.getMethod());
            imgvNote.setVisibility(VISIBLE);
        }
    }
}
