package th.ac.mahidol.rama.emam.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildAddDrugNotPrepareFragment;


public class AddDrugNotPrepareActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private ListView listView;
    private String nfcUID, sdlocID, wardName, time, RFID, firstName, lastName, prn, mrn, checkType, date;
    private int timeposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_not_drug_add);

        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        RFID = getIntent().getExtras().getString("RFID");
        firstName = getIntent().getExtras().getString("firstname");
        lastName = getIntent().getExtras().getString("lastname");
        prn = getIntent().getExtras().getString("prn");
        mrn = getIntent().getExtras().getString("mrn");
        checkType = getIntent().getExtras().getString("checkType");
        date = getIntent().getExtras().getString("date");

        Log.d("check", "AddDrugNotPrepareActivity nfcUId = "+nfcUID+" /sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+timeposition+" /time = "+time+
        " /RFID = "+RFID+" /firstName = "+firstName+" /lastName = "+lastName+" /prn = "+prn+" /mrn = "+mrn+" /checkType = "+checkType+" /date = "+date);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAddDrugNotPrepareFragment.newInstance(nfcUID, sdlocID, wardName, timeposition, time, RFID, firstName, lastName, prn, mrn, checkType, date)).commit();
        }

        initInstance();
    }

    private void initInstance() {
        listView = (ListView) findViewById(R.id.lvMenu);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(AddDrugNotPrepareActivity.this,drawerLayout,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }



}
