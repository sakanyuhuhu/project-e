package th.ac.mahidol.rama.emam.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.addmedication.AddMedicationPatientAllActivity;
import th.ac.mahidol.rama.emam.activity.history.PatientAllActivity;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.fragment.MainSelectMenuFragment;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class MainSelectMenuActivity extends AppCompatActivity {
    private int save = -1;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private ListView mListView;
    private TextView tvName, tvRFID, tvWard;
    private String sdlocID, nfcUID, wardName, RFID, firstName, lastName;
    private String[] mDrawerTitle = {"Home", "Medication Management", "History & Medication List", "Add Medication", "Setting", "บันทึกการให้ยา", "Log out"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_select_menu);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");

        Log.d("check", "MainSelectMenuActivity nfcUId = "+nfcUID+" /sdlocId = "+sdlocID+" /wardname = "+wardName);
        initInstance();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, MainSelectMenuFragment.newInstance(nfcUID,sdlocID, wardName)).commit();
        }
    }

    private void initInstance() {
        mListView = (ListView) findViewById(R.id.lvMenu);
        tvName = (TextView) findViewById(R.id.tvName);
        tvRFID = (TextView) findViewById(R.id.tvRFID);
        tvWard = (TextView) findViewById(R.id.tvWard);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainSelectMenuActivity.this,drawerLayout,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadPersonWard(nfcUID, sdlocID);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerTitle);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getChildAt(position).setBackgroundColor(Color.parseColor("#009688"));
                if (save != -1 & save != position) {
                    parent.getChildAt(save).setBackgroundColor(Color.parseColor("#ffffff"));
                }

                save = position;

                if(position == 0){
                    Intent intent = new Intent(MainSelectMenuActivity.this, MainSelectMenuActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent(MainSelectMenuActivity.this,TimelineActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    startActivity(intent);
                }
                else if(position == 2){
                    Intent intent = new Intent(MainSelectMenuActivity.this, PatientAllActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    startActivity(intent);
                }
                else if(position == 3){
                    Intent intent = new Intent(MainSelectMenuActivity.this, AddMedicationPatientAllActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    startActivity(intent);
                }
                else if(position == 4){
                    Intent intent = new Intent(MainSelectMenuActivity.this, SelectWardActivity.class);
                    startActivity(intent);
                }
                else if(position == 5){

                }
                else if(position == 6){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainSelectMenuActivity.this);
                    builder.setTitle("EMAM");
                    builder.setMessage("คุณต้องการออกจากระบบใช่หรือไม่?");
                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainSelectMenuActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("ไม่ใช่", null);
                    builder.create();
                    builder.show();
                }
            }
        });
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

    private void saveCachePersonWard(CheckPersonWardDao checkPersonWardDao){
        String json = new Gson().toJson(checkPersonWardDao);
        SharedPreferences prefs = this.getSharedPreferences("checkperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("checkperson",json);
        editor.apply();
    }

    private void loadPersonWard(String nfcUID, String sdlocID){
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }

    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            saveCachePersonWard(dao);
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();

            tvName.setText(firstName+" " + lastName);
            tvRFID.setText("RFID : " + RFID);
            tvWard.setText("Ward : " + wardName);
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "Prepare PersonWardLoadCallback Failure " + t);
        }
    }
    
}

