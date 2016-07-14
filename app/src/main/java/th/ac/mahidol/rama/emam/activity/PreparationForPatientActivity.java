package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.PreparationForPatientFragment;

public class PreparationForPatientActivity extends AppCompatActivity {

    private String nfcUId;
    private String sdlocId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_for_patient);
        nfcUId = getIntent().getExtras().getString("nfcUId");
        sdlocId = getIntent().getExtras().getString("sdlocId");
        Log.d("check", "This's PreparationForPatientActivity");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, PreparationForPatientFragment.newInstance(nfcUId,sdlocId )).commit();
        }
    }
}
