package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.ViewPagerAdapter;
import th.ac.mahidol.rama.emam.fragment.BuildHistoryPrepareFragment;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationForPatientFragment;

public class PreparationForPatientActivity extends AppCompatActivity {

    private String nfcUID,sdlocID, wardName, time, firstName, lastName, RFID;
    private int position, timeposition;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_for_patient);
        initInstance(savedInstanceState);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void  initInstance(Bundle savedInstanceState){
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        timeposition = getIntent().getExtras().getInt("timeposition");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        Log.d("check", "PreparationForPatientActivity nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " +timeposition +" /position = " + position+" /time = "+time);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationForPatientFragment.newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time)).commit();
//        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuildPreparationForPatientFragment().newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time), "PREPARATION");
        adapter.addFragment(new BuildHistoryPrepareFragment().newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
