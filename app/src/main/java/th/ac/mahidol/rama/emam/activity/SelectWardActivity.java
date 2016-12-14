package th.ac.mahidol.rama.emam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildSelectWardFragment;

public class SelectWardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ward);
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildSelectWardFragment.newInstance()).commit();

    }
}
