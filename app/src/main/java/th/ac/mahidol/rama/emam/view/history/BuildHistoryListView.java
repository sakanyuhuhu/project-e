package th.ac.mahidol.rama.emam.view.history;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.view.BaseCustomViewGroup;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildHistoryListView extends BaseCustomViewGroup {

    private TextView tvDrugName, tvDosage, tvRoute, tvFrequency, tvSite, tvMethod;
    private ImageView imgvNote, ivPhotoMed;

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
        ivPhotoMed = (ImageView) findViewById(R.id.ivPhotoMed);
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
        URL url = null;
        try {
            url = new URL(dao.getLink());
            try {
                if(!url.equals("")){
                    ivPhotoMed.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(dao.getComplete().equals("1")) {
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText(Html.fromHtml("Dosage: <b>" + dao.getDose() + " " + String.valueOf(dao.getUnit()) + "</b>"));
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText(Html.fromHtml("Frequency: <b>" + dao.getFrequency() + " (" + dao.getAdminTime() + ")</b>"));
            tvSite.setText("Site: " + dao.getSite());
            tvMethod.setText("Method: " + dao.getMethod());
            imgvNote.setVisibility(INVISIBLE);
        }
        else {
            tvDrugName.setText(String.valueOf(dao.getTradeName()));
            tvDosage.setText(Html.fromHtml("Dosage: <b>" + dao.getDose() + " " + String.valueOf(dao.getUnit()) + "</b>"));
            tvRoute.setText("Route: " + dao.getRoute());
            tvFrequency.setText(Html.fromHtml("Frequency: <b>" + dao.getFrequency() + " (" + dao.getAdminTime() + ")</b>"));
            tvSite.setText("Site: " + dao.getSite());
            tvMethod.setText("Method: " + dao.getMethod());
            imgvNote.setVisibility(VISIBLE);
        }
    }
}
