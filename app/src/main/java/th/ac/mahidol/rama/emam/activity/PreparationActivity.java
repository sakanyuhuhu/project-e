package th.ac.mahidol.rama.emam.activity;


import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.fragment.PreparationFragment;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

public class PreparationActivity extends AppCompatActivity {

    private SQLiteManager dbHelper;
    private NfcAdapter mNfcAdapter;
    private String gettimer;
    private String sdlocId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);
        gettimer = getIntent().getExtras().getString("Time");
        sdlocId = getIntent().getExtras().getString("sdlocId");

        //initInstance();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, PreparationFragment.newInstance(gettimer, sdlocId)).commit();
        }
    }

//    private void initInstance() {
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if(mNfcAdapter == null){
//            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
//            finish();
//        }else{
//            if(!mNfcAdapter.isEnabled()){
//                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//            }
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        Log.d("check", "onNewIntent PreparationActivity");
//        boolean checkRegisterNFC;
//        String action = intent.getAction();
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
//            Tag nfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            String nfcTagId = ByteArrayToHexString(nfcTag.getId());
//            Toast.makeText(this, nfcTagId, Toast.LENGTH_LONG).show();
//
//            dbHelper = new SQLiteManager(this);
//            dbHelper.addNFCRegister("HN1", nfcTagId);
//            checkRegisterNFC = dbHelper.getNFCRegister(nfcTagId);
//
//            if(checkRegisterNFC == true){
//                Log.d("check", "checkRegisterNFC : " + checkRegisterNFC);
//                getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, PreparationFragment.newInstance(gettimer)).commit();
//            }
//            else{
//                Toast.makeText(this, "Not found NFC tag!", Toast.LENGTH_LONG).show();
//            }
//        }
//        super.onNewIntent(intent);
//    }
//
//    private String ByteArrayToHexString(byte [] inarray) {
//        int i, j, in;
//        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
//        String out= "";
//
//        for(j = 0 ; j < inarray.length ; ++j)
//        {
//            in = (int) inarray[j] & 0xff;
//            i = (in >> 4) & 0x0f;
//            out += hex[i];
//            i = in & 0x0f;
//            out += hex[i];
//        }
//        return out;
//    }
//
//    @Override
//    protected void onResume() {
//        if(mNfcAdapter != null)
//            enableForegroundDispatchSystem();
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        if(mNfcAdapter != null)
//            disableForegroundDispatchSystem();
//        super.onPause();
//    }
//
//    private void enableForegroundDispatchSystem(){
//        Intent intent = getIntent();
//        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        IntentFilter[] intentFilters = new IntentFilter[]{};
//        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
//
//    }
//
//    private void disableForegroundDispatchSystem(){
//        mNfcAdapter.disableForegroundDispatch(this);
//    }
}
