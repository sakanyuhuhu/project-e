package th.ac.mahidol.rama.emam.activity;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationFragment;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

public class PreparationActivity extends AppCompatActivity {

    private SQLiteManager dbHelper;
    private NfcAdapter mNfcAdapter;
    private String sdlocID, nfcUID, wardName, nfcTagId, time;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);
        nfcUID = getIntent().getExtras().getString("nfcUId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        Log.d("check", "PreparationActivity nfcUId = "+ nfcUID +" /sdlocId = "+sdlocID+" /wardName = "+wardName+" /position = "+position+" /time = "+time);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationFragment.newInstance(nfcUID, sdlocID, wardName, position, time)).commit();
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(PreparationActivity.this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        boolean checkRegisterNFC;
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Tag nfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcTagId = ByteArrayToHexString(nfcTag.getId());
            Toast.makeText(this, nfcTagId, Toast.LENGTH_LONG).show();

//            dbHelper = new SQLiteManager(this);
//            dbHelper.addNFCRegister(nfcTagId);
//            checkRegisterNFC = dbHelper.getNFCRegister(nfcTagId);

//            if(checkRegisterNFC == true){
//                Toast.makeText(this, "NFC found!", Toast.LENGTH_LONG).show();
//            }
//            else{
//                Toast.makeText(this, "Not found NFC tag!", Toast.LENGTH_LONG).show();
//            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        if(mNfcAdapter != null)
            enableForegroundDispatchSystem();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(mNfcAdapter != null)
            disableForegroundDispatchSystem();
        super.onResume();
    }

    private void enableForegroundDispatchSystem(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

    }

    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

}
