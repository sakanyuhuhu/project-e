package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildDrugAdrListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvSideEffect, tvNaranjo;

    public BuildDrugAdrListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildDrugAdrListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildDrugAdrListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildDrugAdrListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_adr, this);
    }

    private void initInstances() {
        // findViewById here
        tvDrugName = (TextView) findViewById(R.id.tvDrugName);
        tvSideEffect = (TextView) findViewById(R.id.tvSideEffect);
        tvNaranjo = (TextView) findViewById(R.id.tvNaranjo);
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

    public void setDrugAdr(DrugAdrDao dao){
        tvDrugName.setText(Html.fromHtml("<b>Drug :</b> "+ dao.getDrugname()));
        tvSideEffect.setText(Html.fromHtml("<b>Side Effect :</b> " +dao.getSideEffect()));
        tvNaranjo.setText(Html.fromHtml("<b>Naranjo :</b> " +dao.getNaranjo()));
    }

}
