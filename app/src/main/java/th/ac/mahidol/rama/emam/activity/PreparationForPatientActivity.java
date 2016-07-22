package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.PreparationForPatientFragment;

public class PreparationForPatientActivity extends AppCompatActivity {

    private String nfcUId;
    private String sdlocId;
    private String gettimer;
    private String patientName;
    private String bedNo;
    private String mRN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_for_patient);
        gettimer = getIntent().getExtras().getString("timer");
        nfcUId = getIntent().getExtras().getString("nfcUId");
        sdlocId = getIntent().getExtras().getString("sdlocId");
        patientName = getIntent().getExtras().getString("patientName");
        bedNo = getIntent().getExtras().getString("bedNo");
        mRN = getIntent().getExtras().getString("mRN");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, PreparationForPatientFragment.newInstance(gettimer, nfcUId, sdlocId, patientName, bedNo, mRN)).commit();
        }
    }
}
