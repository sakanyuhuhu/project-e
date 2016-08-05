package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.PreparationForPatientFragment;

public class PreparationForPatientActivity extends AppCompatActivity {

    private String nfcUId, sdlocId, gettimer, patientName, bedNo, mRN, strdf2;
    private int tricker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_for_patient);
        gettimer = getIntent().getExtras().getString("timer");
        nfcUId = getIntent().getExtras().getString("nfcUId");
        sdlocId = getIntent().getExtras().getString("sdlocId");
        strdf2 = getIntent().getExtras().getString("strdf");
        patientName = getIntent().getExtras().getString("patientName");
        bedNo = getIntent().getExtras().getString("bedNo");
        mRN = getIntent().getExtras().getString("mRN");
        tricker = getIntent().getExtras().getInt("tricker");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, PreparationForPatientFragment.newInstance(gettimer, nfcUId, sdlocId, patientName, bedNo, mRN, strdf2, tricker)).commit();
        }
    }
}
