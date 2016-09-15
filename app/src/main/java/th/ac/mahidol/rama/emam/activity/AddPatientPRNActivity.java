package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildAddPatientPRNFragment;

/**
 * Created by mac-mini-1 on 9/13/2016 AD.
 */
public class AddPatientPRNActivity extends AppCompatActivity {
    private String sdlocID, wardName, time;
    private int timeposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prn_patient_add);

        initInstance(savedInstanceState);
    }

    private void  initInstance(Bundle savedInstanceState){
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        Log.d("check", "AddPatientPRNActivity sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+timeposition+" /time = "+time);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddPatientPRNFragment.newInstance(sdlocID, wardName, timeposition, time)).commit();
        }
    }
}
