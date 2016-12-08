package th.ac.mahidol.rama.emam.view.history;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.BaseCustomViewGroup;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildListDrugHistoryListView extends BaseCustomViewGroup {

    private LinearLayout lnDrug;
    private TextView tvAdmintime, tvCheckstatus, tvTimeName, tvStatus;
    private String[] date, actualAdmin;

    public BuildListDrugHistoryListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildListDrugHistoryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildListDrugHistoryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildListDrugHistoryListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        lnDrug = (LinearLayout) findViewById(R.id.lnDrug);
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

    public void setDrug(DrugCardDao dao, String drugId) {
        if (dao.getDrugID().equals(drugId)){
            tvAdmintime.setText("Admin Time " + dao.getAdminTime() + ":00");
            if (dao.getComplete() != null) {
                if (dao.getComplete().equals("1")) {
                    tvCheckstatus.setText("เตรียมยา");
                    tvCheckstatus.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                } else
                    tvCheckstatus.setText("เตรียมยาไม่ได้");
            }
            if (dao.getActualAdmin() != null) {
                date = dao.getActualAdmin().split("\\s");
                actualAdmin = date[1].split("\\.");
                tvTimeName.setText(actualAdmin[0] + " " + dao.getFirstName());
            }
            tvStatus.setText(dao.getDescription() + " " + dao.getDescriptionTemplate());
        }
        else{
            lnDrug.setVisibility(INVISIBLE);
        }
    }

}
