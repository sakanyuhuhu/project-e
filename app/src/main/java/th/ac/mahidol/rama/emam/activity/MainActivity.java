package th.ac.mahidol.rama.emam.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.db.SQLEMAMHelper;
import th.ac.mahidol.rama.emam.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    SQLEMAMHelper dbEMAMHelper;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if(!mNfcAdapter.isEnabled()){
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * method read NFC.*/
    @Override
    protected void onNewIntent(Intent intent) {
        boolean chkregisNFC;
        String action = intent.getAction();
//        Log.d("check", "onNewIntent1 = " + intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Tag nfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String nfcTagId = ByteArrayToHexString(nfcTag.getId());
            Log.d("check", "nfcTagId = " + nfcTagId);
            Toast.makeText(this, nfcTagId, Toast.LENGTH_LONG).show();
            /**
             * send NFC tag to DB for check authen login.*/
            dbEMAMHelper = new SQLEMAMHelper(this);
            dbEMAMHelper.addNFCRegister("HN1", nfcTagId);
            chkregisNFC = dbEMAMHelper.getNFCRegister(nfcTagId);

            /***
             * Check getregisNFC handle to AddNFCDataActivity.class */
            if(chkregisNFC == true){
                Log.d("check", "chkregisNFC = " + chkregisNFC);
//                Intent intensendNFC = new Intent(MainActivity.this, AddNFCDataActivity.class);
//                intensendNFC.putExtra("tagNFC", nfcTagId);
//                startActivity(intensendNFC);

                SharedPreferences prefs = this.getSharedPreferences("setWard",Context.MODE_PRIVATE);
                String value = prefs.getString("ward", null);
                if(value == null) {
                    getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, MainFragment.newInstance()).commit();
                }
                else {
                    intent = new Intent(MainActivity.this, MainSelectMenuActivity.class);
                    startActivity(intent);
                }
            }
            else{
                Toast.makeText(this, "Not found NFC tag!", Toast.LENGTH_LONG).show();
            }
        }
        super.onNewIntent(intent);
    }

    /**
     * แปลง ID ที่ได้จาก TAG เช่น B@123123 เป็น String.*/
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
//        Log.d("check", "out " + out);
        return out;
    }

    @Override
    protected void onResume() {
        enableForegroundDispatchSystem();
        super.onResume();
    }

    @Override
    protected void onPause() {
        disableForegroundDispatchSystem();
        super.onPause();
    }

    /**
     * method ที่สั่งให้เปิดอ่าน NFC tag.*/
    private void enableForegroundDispatchSystem(){
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

    }
    /**
     * method ที่สั่งให้ปิดการอ่าน NFC tag.*/
    private void disableForegroundDispatchSystem(){
        mNfcAdapter.disableForegroundDispatch(this);
    }
}
