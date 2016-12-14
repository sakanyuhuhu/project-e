package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.BuildAddDrugNotPrepareFragment;


public class AddDrugNotPrepareActivity extends AppCompatActivity {
    private String nfcUID, wardID, sdlocID, wardName, time, RFID, firstName, lastName, prn, mrn, checkType, date;
    private int timeposition;
    private PatientDataDao patientDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_not_drug_add);

        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        prn = getIntent().getExtras().getString("prn");
        mrn = getIntent().getExtras().getString("mrn");
        checkType = getIntent().getExtras().getString("checkType");
        date = getIntent().getExtras().getString("date");
        patientDao = getIntent().getParcelableExtra("patientDao");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddDrugNotPrepareFragment.newInstance(nfcUID, wardID, sdlocID, wardName, timeposition, time, RFID, firstName, lastName, prn, mrn, checkType, date, patientDao)).commit();
        }
    }
}
