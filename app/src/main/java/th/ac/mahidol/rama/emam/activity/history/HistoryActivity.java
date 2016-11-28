package th.ac.mahidol.rama.emam.activity.history;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.ViewPagerAdapter;
import th.ac.mahidol.rama.emam.fragment.history.BuildCurrentMedFragment;
import th.ac.mahidol.rama.emam.fragment.history.BuildHistory_AdministrationFragment;
import th.ac.mahidol.rama.emam.fragment.history.BuildHistory_DoubleCheckFragment;
import th.ac.mahidol.rama.emam.fragment.history.BuildHistory_PreparationFragment;

public class HistoryActivity extends AppCompatActivity {
    private String nfcUID, sdlocID, wardName;
    private int position;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        position = getIntent().getExtras().getInt("position");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuildHistory_PreparationFragment().newInstance(nfcUID, sdlocID, wardName, position), "FIRST CHECK");
        adapter.addFragment(new BuildHistory_DoubleCheckFragment().newInstance(nfcUID, sdlocID, wardName, position), "DOUBLE CHECK");
        adapter.addFragment(new BuildHistory_AdministrationFragment().newInstance(nfcUID, sdlocID, wardName, position), "ADMINISTRATION");
        adapter.addFragment(new BuildCurrentMedFragment().newInstance(nfcUID, sdlocID, wardName, position), "CURRENT MED");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
