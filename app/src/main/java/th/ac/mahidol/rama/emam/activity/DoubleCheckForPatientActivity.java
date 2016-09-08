package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildDoubleCheckForPatientFragment;

/**
 * Created by mac-mini-1 on 9/8/2016 AD.
 */
public class DoubleCheckForPatientActivity extends AppCompatActivity {
    private String sdlocID, wardName, time, firstName, lastName, RFID;
    private int position, timeposition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doublecheck_for_patient);
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        timeposition = getIntent().getExtras().getInt("timeposition");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        Log.d("check", "PreparationForPatientActivity sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " +timeposition +" /position = " + position+" /time = "+time);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildDoubleCheckForPatientFragment.newInstance(sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time)).commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}