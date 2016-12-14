package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.BuildHistoryDoubleCheckFragment;


public class HistoryDoubleCheckActivity extends AppCompatActivity {
    private String nfcUID,sdlocID, wardName, time, firstName, lastName, RFID;
    private int position, timeposition;
    private PatientDataDao patientDouble;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_history);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        timeposition = getIntent().getExtras().getInt("timeposition");
        position = getIntent().getExtras().getInt("position");
        patientDouble = getIntent().getParcelableExtra("patientDouble");
        time = getIntent().getExtras().getString("time");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildHistoryDoubleCheckFragment.newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, patientDouble, time)).commit();
        }
    }
}
