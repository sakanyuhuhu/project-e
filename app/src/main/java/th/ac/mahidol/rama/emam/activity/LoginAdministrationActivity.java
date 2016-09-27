package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildLoginAdministrationFragment;

/**
 * Created by mac-mini-1 on 9/13/2016 AD.
 */
public class LoginAdministrationActivity extends AppCompatActivity {
    private String sdlocID, wardName, time;
    private int timeposition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance(savedInstanceState);
    }

    private void  initInstance(Bundle savedInstanceState){
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");

        Log.d("check", "LoginAdministrationActivity sdlocId = "+sdlocID+" /wardName = "+wardName+" /timeposition = "+timeposition+" /time = "+time);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildLoginAdministrationFragment.newInstance(sdlocID, wardName, timeposition, time)).commit();
        }
    }

}
