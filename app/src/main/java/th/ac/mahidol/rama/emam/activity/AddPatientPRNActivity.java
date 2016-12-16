package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.fragment.BuildAddPatientPRNFragment;


public class AddPatientPRNActivity extends AppCompatActivity {
    private String nfcUID, wardID, sdlocID, wardName, time;
    private int timeposition;
    private String prn ="addprn";
    private TimelineDao timelineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prn_patient_add);

        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        timelineDao = getIntent().getParcelableExtra("include");

        initInstance(savedInstanceState);

    }

    private void  initInstance(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddPatientPRNFragment.newInstance(nfcUID, wardID, sdlocID, wardName, timeposition, time, prn, timelineDao)).commit();
        }
    }

}
