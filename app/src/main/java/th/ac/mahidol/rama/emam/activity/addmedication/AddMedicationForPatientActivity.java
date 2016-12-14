package th.ac.mahidol.rama.emam.activity.addmedication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.addmedication.BuildAddMedicationForPatientFragment;

public class AddMedicationForPatientActivity extends AppCompatActivity {
    private String wardID, sdlocID, nfcUID, wardName;
    private int position;
    private PatientDataDao patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wardID = getIntent().getExtras().getString("wardId");
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        position = getIntent().getExtras().getInt("position");
        patient = getIntent().getParcelableExtra("patient");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddMedicationForPatientFragment.newInstance(nfcUID, wardID, sdlocID, wardName, position, patient)).commit();
        }

    }

}
