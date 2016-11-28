package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.BuildHistoryAdministrationFragment;

public class HistoryAdministrationActivity extends AppCompatActivity {
    private String nfcUID,sdlocID, wardName, time, firstName, lastName, RFID;
    private int position, timeposition;
    private PatientDataDao patientAdmin;

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
        patientAdmin = getIntent().getExtras().getParcelable("patientAdmin");
        time = getIntent().getExtras().getString("time");
        Log.d("check", "HistoryPrepareActivity nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " +timeposition +" /position = " + position+" /time = "+time);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildHistoryAdministrationFragment.newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, patientAdmin, time)).commit();
        }
    }
}
