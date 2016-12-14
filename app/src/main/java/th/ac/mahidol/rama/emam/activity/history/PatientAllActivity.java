package th.ac.mahidol.rama.emam.activity.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.history.BuildPatientAllFragment;

public class PatientAllActivity extends AppCompatActivity {
    private String wardID, sdlocID, nfcUID, wardName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientall);

        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPatientAllFragment.newInstance(nfcUID, wardID, sdlocID, wardName)).commit();
        }

    }

}
