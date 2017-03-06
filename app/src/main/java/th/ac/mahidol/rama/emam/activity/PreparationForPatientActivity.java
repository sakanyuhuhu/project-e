package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationForPatientFragment;

public class PreparationForPatientActivity extends AppCompatActivity {

    private String nfcUID, wardID, sdlocID, wardName, time, firstName, lastName, RFID, prn;
    private int position, timeposition;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListDrugCardDao listDrugCardDao;
    private PatientDataDao patientDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_for_patient);

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
        listDrugCardDao = getIntent().getParcelableExtra("dao");
        patientDao = getIntent().getParcelableExtra("patientDao");
        prn = getIntent().getExtras().getString("prn");

        initInstance(savedInstanceState);

    }

    private void  initInstance(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationForPatientFragment.newInstance(nfcUID, wardID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time,listDrugCardDao, patientDao, prn)).commit();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
