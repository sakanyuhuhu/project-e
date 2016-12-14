package th.ac.mahidol.rama.emam.activity.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.fragment.alarm.BuildPatientMedOnTimeFragment;

public class PatientMedOnTimeActivity extends AppCompatActivity {
    private String wardID, sdlocID, wardName, time;
    private int position;
    private PatientDataDao patientDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_med_patient);

        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        patientDao = getIntent().getParcelableExtra("patient");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");

        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPatientMedOnTimeFragment.newInstance(wardID, sdlocID, wardName, patientDao, position, time)).commit();

    }


}
