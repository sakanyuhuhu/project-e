package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildTimelineFragment;

public class TimelineActivity extends AppCompatActivity {
    private String wardID, sdlocID, nfcUID, wardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildTimelineFragment.newInstance(nfcUID, sdlocID, wardName, wardID)).commit();
        }

    }

}
