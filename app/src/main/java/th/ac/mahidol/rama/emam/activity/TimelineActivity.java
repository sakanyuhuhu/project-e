package th.ac.mahidol.rama.emam.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
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

        initInstance(savedInstanceState);

    }

    private void initInstance(Bundle savedInstanceState) {
        SharedPreferences prefs = this.getSharedPreferences("checkpersonlogin", Context.MODE_PRIVATE);
        String data = prefs.getString("checkpersonlogin", null);
        if (data != null) {
            CheckPersonWardDao dao = new Gson().fromJson(data, CheckPersonWardDao.class);
            if(dao != null) {
                if(Integer.parseInt(wardID) == Integer.parseInt(String.valueOf(dao.getWardId())))
                    nfcUID = dao.getNfcUId();
            }
        }

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildTimelineFragment.newInstance(nfcUID, sdlocID, wardName, wardID)).commit();
        }
    }

}
