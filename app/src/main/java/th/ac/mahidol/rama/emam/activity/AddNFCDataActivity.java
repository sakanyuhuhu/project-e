package th.ac.mahidol.rama.emam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

/**
 * Created by mi- on 5/7/2559.
 */
public class AddNFCDataActivity extends AppCompatActivity {

    private TextView tvHn, tvNfc;
    SQLiteManager dbEMAMHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nfc_data);

        String HN;
        String tagNFC;
        tvHn = (TextView) findViewById(R.id.tvHn);
        tvNfc = (TextView) findViewById(R.id.tvNfc);
        /**
         *Show data from DB, by send tagNFC for get HN data.
        */
        tagNFC = getIntent().getExtras().getString("tagNFC");
        Log.d("check", tagNFC);
        dbEMAMHelper = new SQLiteManager(AddNFCDataActivity.this);
        HN = dbEMAMHelper.getHNRegister(tagNFC);
        tvHn.setText(HN);
        tvNfc.setText(tagNFC);

        /*call selectWard*/
        Intent intent = new Intent(AddNFCDataActivity.this, SelectWardActivity.class);
        startActivity(intent);
    }

}
