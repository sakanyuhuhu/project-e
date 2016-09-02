package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.BuildDrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildPreparationForPatientFragment extends Fragment implements View.OnClickListener{
    private String nfcUID, sdlocID, wardName, toDayDate, dateFortvDate, dateActualAdmin, time, firstName, lastName;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAdr;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();;
    private Spinner spinner1;
    private ListDrugCardDao dao;
    private Date datetoDay;

    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, String firstName, String lastName, int timeposition, int position, String time) {
        BuildPreparationForPatientFragment fragment = new BuildPreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putString("firstname", firstName);
        args.putString("lastname", lastName);
        args.putInt("timeposition", timeposition);
        args.putInt("position", position);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    public void init(Bundle savedInstanceState){

    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        new getADRForPatient().execute();
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        timeposition = getArguments().getInt("timeposition");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");

        Log.d("check", "BuildPreparationForPatientFragment nfcUId = " + nfcUID + " /sdlocId = " + sdlocID + " /wardName = " + wardName + " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " +timeposition +" /position = " + position+" /time = "+time);

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfForActualAdmin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfFortvDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        toDayDate = sdfForDrugUseDate.format(datetoDay);
        dateFortvDate = sdfFortvDate.format(datetoDay);
        dateActualAdmin = sdfForActualAdmin.format(datetoDay);
        tvTime.setText(time);

        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            Log.d("check", "data size = "+listPatientDataDao.getPatientDao().size()+ " position = "+position);
            buildHeaderPatientDataView.setData(listPatientDataDao, position);

            if(timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Firstcheck");

                loadMedicalData(drugCardDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate+1);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Firstcheck");

                loadMedicalData(drugCardDao);
            }
        }

        getOnClickSpinner();
        btnSave.setOnClickListener(this);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadMedicalData(DrugCardDao drugCardDao){

        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }

    private void loadsetDrugData(ListDrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().setNewDrugData(drugCardDao);
        call.enqueue(new NewDrugLoadCallback());
    }

    private void getOnClickSpinner(){
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();

                if(selectedItem.equals("ทั้งหมด") & dao!=null) {
                    tvDate.setText("  " + dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("กิน") & dao!=null) {
                    tvDate.setText("  " + dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoPO().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("ฉีด") & dao!=null) {
                    tvDate.setText("  " + dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoIV().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("อื่นๆ") & dao!=null){
                    tvDate.setText("  " + dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave){
            Log.d("check", "check save");
            Boolean checkNull = true;
            for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                Log.d("check", "Complete = "+d.getComplete() +" /CheckNote = "+d.getCheckNote());
                if(d.getComplete() == null & d.getCheckNote() == null){
                    d.setComplete("0");
                    d.setCheckNote("0");
                }
                if(d.getComplete().equals("0") & d.getCheckNote().equals("0")){
                    checkNull = false;
                }
            }
            if(checkNull) {
                for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                    d.setRFID(nfcUID);
                    d.setFirstName(firstName);
                    d.setLastName(lastName);
                    d.setWardName(wardName);
                    d.setActualAdmin(dateActualAdmin);
                    d.setActivityHour(time);
                }
//                String json = new Gson().toJson(buildDrugCardListManager.getDaoAll());
//                Log.d("check", "DrugLoadCallback = "+json);
//                loadsetDrugData(buildDrugCardListManager.getDaoAll());
                Log.d("check", "Saved Status " + checkNull);
            }
            else {
                Log.d("check", "Can't Save Status " + checkNull);
                Toast.makeText(getContext(), "กรุณาเขียนคำอธิบายสำหรับทุกๆ ตัวยาที่ไม่ได้ใส่เครื่องหมายถูก", Toast.LENGTH_LONG).show();
            }
        }
    }




    class DrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            Log.d("check", "DrugCardDao.size = " + dao.getListDrugCardDao().size());
            String json = new Gson().toJson(dao);
            Log.d("check", "DrugLoadCallback = "+json);
            buildDrugCardListManager.setDao(dao);
            tvDate.setText("  " + dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "DrugLoadCallback Failure " + t);
        }
    }


    class NewDrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            Log.d("check", "NewDrugLoadCallback = " + dao.getListDrugCardDao().size());
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "NewDrugLoadCallback Failure " + t);
        }
    }


    public class getADRForPatient extends AsyncTask<Void, Void, List<DrugAdrDao>>{

        @Override
        protected void onPostExecute(List<DrugAdrDao> drugAdrDaos) {
            super.onPostExecute(drugAdrDaos);
            Log.d("check", "*****DrugAdrDao size = " +  drugAdrDaos.size());

            tvDrugAdr.setText("   การแพ้ยา:แตะเพื่อดูข้อมูล");
        }

        @Override
        protected List<DrugAdrDao> doInBackground(Void... params) {
            List<DrugAdrDao> itemsList = new ArrayList<DrugAdrDao>();
            SoapManager soapManager = new SoapManager();

            SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
            String data = prefs.getString("patientintdata",null);
            if(data != null){
                ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
                Log.d("check", "*****doInBackground data = " + listPatientDataDao.getPatientDao().get(position).getMRN());
                itemsList = parseXML(soapManager.getDrugADR("Get_Adr", listPatientDataDao.getPatientDao().get(position).getMRN()));//4503598  on appcenter have data, But on devfox_ws data(empty)
            }
            return itemsList;
        }

        private   ArrayList<DrugAdrDao>  parseXML(String soap) {
            List<DrugAdrDao> itemsList = new ArrayList<DrugAdrDao>();
            try {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                SearchDrugAdrManager searchDrugAdrXMLHandler = new SearchDrugAdrManager();
                xmlReader.setContentHandler(searchDrugAdrXMLHandler);
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(soap));
                xmlReader.parse(inStream);
                itemsList = searchDrugAdrXMLHandler.getItemsList();

                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
                Log.w("AndroidParseXMLActivity", e);
            }

            return (ArrayList<DrugAdrDao>) itemsList;
        }
    }

}
