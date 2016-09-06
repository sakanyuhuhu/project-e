package th.ac.mahidol.rama.emam.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.state.BundleSavedState;


public class BuildDoubleCheckListView extends BaseCustomViewGroup {

    private TextView tvPatient, tvBedNo, tvMrn;
    private ImageView testnote;

    public BuildDoubleCheckListView(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public BuildDoubleCheckListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public BuildDoubleCheckListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public BuildDoubleCheckListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.view_list_patient_doublecheck, this);
    }

    private void initInstances() {
        // findViewById here
        tvPatient = (TextView) findViewById(R.id.tvPatient);
        tvBedNo = (TextView) findViewById(R.id.tvBedNo);
        tvMrn = (TextView) findViewById(R.id.tvMrn);
        testnote = (ImageView) findViewById(R.id.testnote);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    public void setPatient(String textBedNo, String textPatient, String textMrn, int tricker){
        if(tricker == 1){
            testnote.setImageResource(R.drawable.notechange);
        }
        tvPatient.setText(textPatient);
        tvBedNo.setText("เลขที่เตียง/ห้อง: " + textBedNo);
        tvMrn.setText("HN: " + textMrn);
    }

}
