package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildAddDrugPRNForPatientFragment;

public class AddDrugPatientPRNActivity extends AppCompatActivity {
    private String nfcUID, sdlocID, wardName, time;
    private int timeposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prn_patient_drug_add);

        initInstance(savedInstanceState);
    }

    private void initInstance(Bundle savedInstanceState) {
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");

        Log.d("check", "AddDrugPatientPRNActivity sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+timeposition+" /time = "+time);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddDrugPRNForPatientFragment.newInstance(nfcUID, sdlocID, wardName, timeposition, time)).commit();
        }

    }



}
