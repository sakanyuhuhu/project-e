package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildDrugHistoryListView extends BaseCustomViewGroup {

    private TextView tvAdmintime, tvCheckstatus, tvTimeName, tvStatus;

    public BuildDrugHistoryListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildDrugHistoryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildDrugHistoryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildDrugHistoryListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_med_dialog_history, this);
    }

    private void initInstances() {
        // findViewById here
        tvAdmintime = (TextView) findViewById(R.id.tvAdmintime);
        tvCheckstatus = (TextView) findViewById(R.id.tvCheckstatus);
        tvTimeName = (TextView) findViewById(R.id.tvTimeName);
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

    public void setDrugAdr(DrugCardDao dao){
        tvAdmintime.setText("Admin Time "+ dao.getAdminTime()+":00");
        if(dao.getComplete() != null) {
            if(dao.getComplete().equals("1")) {
                tvCheckstatus.setText("เตรียมยา");
                tvCheckstatus.setTextColor(getResources().getColor(R.color.colorBlue));
            }else
                tvCheckstatus.setText("เตรียมยาไม่ได้");
        }
        tvTimeName.setText(dao.getActualAdmin()+" "+dao.getFirstName());
        if(dao.getDescription() == null )
        tvStatus.setText(dao.getDescription()+dao.getDescriptionTemplate());

    }

}
