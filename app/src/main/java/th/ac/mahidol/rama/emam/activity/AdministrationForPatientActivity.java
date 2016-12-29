package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.BuildAdministrationForPatientFragment;

public class AdministrationForPatientActivity extends AppCompatActivity {

    private String nfcUID, wardID, sdlocID, wardName, time, firstName, lastName, RFID;
    private int position, timeposition;
    private PatientDataDao patientAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration_for_patient);

        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        timeposition = getIntent().getExtras().getInt("timeposition");
        patientAdmin = getIntent().getParcelableExtra("patientAdmin");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAdministrationForPatientFragment.newInstance(nfcUID, wardID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, patientAdmin, time)).commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
