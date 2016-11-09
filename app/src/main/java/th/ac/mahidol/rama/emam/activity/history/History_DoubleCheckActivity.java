package th.ac.mahidol.rama.emam.activity.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.history.BuildHistory_DoubleCheckFragment;

public class History_DoubleCheckActivity extends AppCompatActivity {
    private String nfcUID,sdlocID, wardName;
    private int position;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_history);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        position = getIntent().getExtras().getInt("position");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildHistory_DoubleCheckFragment.newInstance(nfcUID, sdlocID, wardName, position)).commit();
        }
    }

}
