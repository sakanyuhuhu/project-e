package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationForPatientFragment;

public class PreparationForPatientActivity extends AppCompatActivity {

    private String nfcUID, sdlocID, time;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_for_patient);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        Log.d("check", "PreparationForPatientActivity nfcUId = " + nfcUID + " /sdlocId = " + sdlocID + " /position = " + position+" /time = "+time);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationForPatientFragment.newInstance(nfcUID, sdlocID, position, time)).commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
