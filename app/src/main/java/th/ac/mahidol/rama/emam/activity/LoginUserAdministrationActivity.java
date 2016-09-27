package th.ac.mahidol.rama.emam.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import th.ac.mahidol.rama.emam.dao.buildCheckLoginDAO.CheckLoginDao;
import th.ac.mahidol.rama.emam.fragment.BuildAdministrationFragment;
import th.ac.mahidol.rama.emam.manager.SearchLoginManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;

/**
 * Created by mac-mini-1 on 9/13/2016 AD.
 */
public class LoginUserAdministrationActivity extends AppCompatActivity {
    private String username, password, sdlocID, wardName, time, nfcUID = null;
    private int timeposition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance(savedInstanceState);
    }

    private void  initInstance(Bundle savedInstanceState){
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        sdlocID  = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        timeposition = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        new getResultForLogin().execute();
    }


    public class getResultForLogin extends AsyncTask<Void, Void, List<CheckLoginDao>> {

        @Override
        protected void onPostExecute(List<CheckLoginDao> checkLoginDaos) {
            super.onPostExecute(checkLoginDaos);
            Log.d("check", "*****getResultForLogin onPostExecute = " +  checkLoginDaos.get(0).getName());
            if(checkLoginDaos.get(0).getRole().equals("G")){ // N
                getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAdministrationFragment.newInstance(nfcUID, sdlocID, wardName, timeposition, time, checkLoginDaos.get(0).getName())).commit();
            }
            else {
                Toast.makeText(getApplicationContext(), "กรุณาตรวจสอบ Username และ Password หรือติดต่อผู้ดูแลระบบ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginPreparationActivity.class);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }

        }

        @Override
        protected List<CheckLoginDao> doInBackground(Void... params) {
            List<CheckLoginDao> itemsList = new ArrayList<CheckLoginDao>();
            SoapManager soapManager = new SoapManager();

            if(username != null & password != null) {
                itemsList = parseXML(soapManager.getLogin("Get_staff_detail", username, password));
            }
            Log.d("check", "itemsList doInBackground = "+ itemsList);
            return itemsList;
        }



        private ArrayList<CheckLoginDao> parseXML(String soap) {
            List<CheckLoginDao> itemsList = new ArrayList<CheckLoginDao>();
            try {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                SearchLoginManager searchLoginXMLHandler = new SearchLoginManager();
                xmlReader.setContentHandler(searchLoginXMLHandler);
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(soap));
                xmlReader.parse(inStream);
                itemsList = searchLoginXMLHandler.getItemsList();

                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
                Log.w("AndroidParseXMLActivity", e);
            }

            return (ArrayList<CheckLoginDao>) itemsList;
        }
    }


}
