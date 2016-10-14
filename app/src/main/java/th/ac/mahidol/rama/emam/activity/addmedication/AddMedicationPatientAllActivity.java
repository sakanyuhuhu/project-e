package th.ac.mahidol.rama.emam.activity.addmedication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.addmedication.BuildAddMedicationPatientAllFragment;

public class AddMedicationPatientAllActivity extends AppCompatActivity {
    private String sdlocID, nfcUID, wardName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientall_addmedication);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddMedicationPatientAllFragment.newInstance(nfcUID,sdlocID, wardName)).commit();
        }

    }

}
