package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildLoginAdministrationFragment;


public class LoginAdministrationActivity extends AppCompatActivity {
    private String wardID, sdlocID, wardName, time;
    private int timeposition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance(savedInstanceState);
    }

    private void  initInstance(Bundle savedInstanceState){
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildLoginAdministrationFragment.newInstance(wardID, sdlocID, wardName, timeposition, time)).commit();
        }
    }

}
