package th.ac.mahidol.rama.emam.activity.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.alarm.BuildPatientOnTimeFragment;

public class PatientOnTimeActivity extends AppCompatActivity {
    private String sdlocID, wardName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientall);
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPatientOnTimeFragment.newInstance(sdlocID, wardName)).commit();
        }

    }

}
