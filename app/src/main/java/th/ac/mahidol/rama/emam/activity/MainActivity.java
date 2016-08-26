package th.ac.mahidol.rama.emam.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.PatientDao;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;
import th.ac.mahidol.rama.emam.manager.SearchPatientByWardManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;

public class MainActivity extends AppCompatActivity {

    private SQLiteManager dbHelper;
    private NfcAdapter mNfcAdapter;
    private String nfcTagID, sdlocID, wardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstance();

    }

    private void initInstance() {
        SharedPreferences prefs = this.getSharedPreferences("SETWARD", Context.MODE_PRIVATE);
        sdlocID = prefs.getString("sdlocId", null);
        wardName = prefs.getString("wardname", null);

//        if(sdlocID != null & wardName != null) {
////            new getPatientByWard().execute();//getPatientByWard for call SoapManager before MainSelectMenuActivity
//            Intent intent = new Intent(MainActivity.this, MainSelectMenuActivity.class);
//            intent.putExtra("nfcUId", "21592265");
//            intent.putExtra("sdlocId", sdlocID);
//            intent.putExtra("wardname", wardName);
//            startActivity(intent);
//        }
//        else {
//            Intent intent = new Intent(MainActivity.this, SelectWardActivity.class);
//            startActivity(intent);
//        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }else{
            if(!mNfcAdapter.isEnabled()){
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("check","new intent");
        boolean checkRegisterNFC;
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Tag nfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcTagID = ByteArrayToHexString(nfcTag.getId());
            Toast.makeText(this, nfcTagID, Toast.LENGTH_LONG).show();

            dbHelper = new SQLiteManager(this);
            dbHelper.addNFCRegister(nfcTagID);
            checkRegisterNFC = dbHelper.getNFCRegister(nfcTagID);

            if(checkRegisterNFC == true & sdlocID != null & wardName != null){
                intent = new Intent(MainActivity.this, MainSelectMenuActivity.class);
                intent.putExtra("nfcUId", "21592265");
                intent.putExtra("nfcUId", nfcTagID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Not found NFC tag!", Toast.LENGTH_LONG).show();
                intent = new Intent(MainActivity.this, SelectWardActivity.class);
                startActivity(intent);
            }
        }
        super.onNewIntent(intent);
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

    @Override
    protected void onResume() {
        if(mNfcAdapter != null)
            enableForegroundDispatchSystem();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mNfcAdapter != null)
            disableForegroundDispatchSystem();
        super.onPause();
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

    public class getPatientByWard extends AsyncTask<Void, Void, List<PatientDao>> {

        @Override
        protected void onPostExecute(List<PatientDao> patientDaos) {//การทำงานที่ต้องรอการประมวลผลจาก getPatientByWard ให้ย้ายมาทำในนี้
            super.onPostExecute(patientDaos);
//            Log.d("check", " patientDaos.size() = " +  patientDaos.size());
/*
            listBedNo = new ArrayList<String>();
            listMrn = new ArrayList<String>();
            for (int i = 0; i < patientDaos.size(); i++) {
                listBedNo.add(patientDaos.get(i).getBedno());
                listMrn.add(patientDaos.get(i).getMrn());
//                Log.d("check", i + " listBedNo    : " + listBedNo.get(i));
//                Log.d("check", i + " listMRN      : " + listMrn.get(i));
            }
            listDataCenterManager = new ListDataCenterManager();
            listDataCenterManager.getListMedicalCard(listMrn);*/

        }

        @Override
        protected List<PatientDao> doInBackground(Void... params) {
            List<PatientDao> itemsList = new ArrayList<PatientDao>();
            SoapManager soapManager = new SoapManager();
            //itemsList = parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", sdlocId));
            itemsList = parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", "2TC"));
            return itemsList;
        }

        private   ArrayList<PatientDao>  parseXML(String soap) {
            List<PatientDao> itemsList = new ArrayList<PatientDao>();
            try {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                SearchPatientByWardManager searchPatientXMLHandler = new SearchPatientByWardManager();
                xmlReader.setContentHandler(searchPatientXMLHandler);
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(soap));
                xmlReader.parse(inStream);
                itemsList = searchPatientXMLHandler.getItemsList();

                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
                Log.w("AndroidParseXMLActivity", e);
            }

            return (ArrayList<PatientDao>) itemsList;
        }
    }

}
