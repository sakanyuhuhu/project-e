package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.fragment.BuildAddDrugPRNForPatientFragment;

public class AddDrugPatientPRNActivity extends AppCompatActivity {
    private String nfcUID, wardID, sdlocID, wardName, time, RFID, firstName, lastName, prn;
    private int position, timeposition;
    private PatientDataDao patientDao;
    private TimelineDao timelineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prn_patient_drug_add);

        initInstance(savedInstanceState);
    }

    private void initInstance(Bundle savedInstanceState) {
        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        timeposition = getIntent().getExtras().getInt("timeposition");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        patientDao = getIntent().getParcelableExtra("patientPRN");
        prn = getIntent().getExtras().getString("prn");
        timelineDao = getIntent().getParcelableExtra("include");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddDrugPRNForPatientFragment.newInstance(nfcUID, wardID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time, patientDao, prn, timelineDao)).commit();
        }

    }



}
