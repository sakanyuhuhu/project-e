package th.ac.mahidol.rama.emam.activity.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.alarm.BuildPatientOnTimeFragment;

public class PatientOnTimeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientall);

        initInstance(savedInstanceState);

    }


    public void initInstance(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPatientOnTimeFragment.newInstance()).commit();
        }
    }

}
