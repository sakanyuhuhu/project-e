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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildCheckLoginDAO.CheckLoginDao;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchLoginManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;


public class LoginUserActivity extends AppCompatActivity {
    private String username, password, wardID, sdlocID, wardName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance(savedInstanceState);
    }

    private void initInstance(Bundle savedInstanceState) {
        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        new getResultForLogin().execute();
    }


    private void loadPersonWard(String staffID, String sdlocID) {
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonLogin(staffID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }


    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {
        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            if (Integer.parseInt(wardID) == Integer.parseInt(String.valueOf(dao.getWardId()))) {
                Intent intent = new Intent(getApplicationContext(), MainSelectMenuActivity.class);
                intent.putExtra("nfcUId", dao.getNfcUId());
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(LoginUserActivity.this, "กรุณาตรวจสอบสิทธิ์การใช้งาน", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {

        }
    }

    public class getResultForLogin extends AsyncTask<Void, Void, List<CheckLoginDao>> {

        @Override
        protected void onPostExecute(List<CheckLoginDao> checkLoginDaos) {
            super.onPostExecute(checkLoginDaos);
            if(checkLoginDaos.get(0).getName() != null) {
                if (!checkLoginDaos.get(0).getName().equals("")) { // check leave in rama
                    loadPersonWard(username, sdlocID);
                } else {
                    Toast.makeText(getApplicationContext(), "กรุณาตรวจสอบ Username และ Password หรือติดต่อผู้ดูแลระบบ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("wardId", wardID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    finish();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "กรุณาตรวจสอบ Username และ Password หรือติดต่อผู้ดูแลระบบ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("wardId", wardID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                finish();
            }
        }

        @Override
        protected List<CheckLoginDao> doInBackground(Void... params) {
            List<CheckLoginDao> itemsList = new ArrayList<CheckLoginDao>();
            SoapManager soapManager = new SoapManager();
            if (username != null & password != null) {
                itemsList = parseXML(soapManager.getLogin("Get_staff_detail", username, password));
            }

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
