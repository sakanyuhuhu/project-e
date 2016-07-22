package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;

public class DoubleCheckActivity extends AppCompatActivity {

    private String nfcUId;
    private String sdlocId;
    private String gettimer;
    private String patientName;
    private String bedNo;
    private String mRN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doublecheck);
        gettimer = getIntent().getExtras().getString("timer");
        nfcUId = getIntent().getExtras().getString("nfcUId");
        sdlocId = getIntent().getExtras().getString("sdlocId");

        Log.d("check", "DoubleCheckActivity");

        if(savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, PreparationForPatientFragment.newInstance(gettimer, nfcUId, sdlocId, patientName, bedNo, mRN)).commit();
        }
    }
}
