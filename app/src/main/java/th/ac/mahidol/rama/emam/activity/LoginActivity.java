package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildLoginFragment;

/**
 * Created by mac-mini-1 on 9/13/2016 AD.
 */
public class LoginActivity extends AppCompatActivity {
    private String sdlocID, wardName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance(savedInstanceState);
    }

    private void  initInstance(Bundle savedInstanceState){
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        Log.d("check", "LoginActivity sdlocId = "+sdlocID+" /wardName = "+wardName);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildLoginFragment.newInstance(sdlocID, wardName)).commit();
        }
    }

}
