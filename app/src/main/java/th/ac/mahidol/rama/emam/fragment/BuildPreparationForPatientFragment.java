package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.HistoryPrepareActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
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
    private String  nfcUID, sdlocID, wardName, toDayDate, dateFortvDate, dateActualAdmin, time, firstName, lastName, RFID;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAdr, tvHistory;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();
    private Spinner spinner1, spinner2;
    private ListDrugCardDao dao;
    private Date datetoDay;

    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time) {
        BuildPreparationForPatientFragment fragment = new BuildPreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putString("RFID", RFID);
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
        RFID = getArguments().getString("RFID");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        timeposition = getArguments().getInt("timeposition");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");

        Log.d("check", "BuildPreparationForPatientFragment nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " +timeposition +" /position = " + position+" /time = "+time);

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
//        tvHistory = (TextView) rootView.findViewById(R.id.tvHistory);

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfForActualAdmin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfFortvDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        toDayDate = sdfForDrugUseDate.format(datetoDay);
        dateFortvDate = sdfFortvDate.format(datetoDay);
        dateActualAdmin = sdfForActualAdmin.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        String tomorrowDate = sdfForDrugUseDate.format(c.getTime());

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
                drugCardDao.setCheckType("First Check");

                loadMedicalData(drugCardDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("First Check");

                loadMedicalData(drugCardDao);
            }
        }

        getOnClickSpinnerDrugRoute();
        getOnClickSpinnerHelp();
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
//        tvHistory.setOnClickListener(this);

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

    private void updateDrugData(ListDrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().updateDrugData(drugCardDao);
        call.enqueue(new SaveDrugDataCallback());
    }

    private void getOnClickSpinnerDrugRoute(){
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("ทั้งหมด") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("กิน") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoPO().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("ฉีด") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoIV().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("อื่นๆ") & dao!=null){
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size()+")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getOnClickSpinnerHelp(){
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
               if(selectedItem.equals("เลือกยาทั้งหมด")){
                    Log.d("check", "เลือกยาทั้งหมด");
                    for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                        d.setComplete("1");
                        d.setCheckNote("0");
                        d.setStatus("normal");
                        d.setDescriptionTemplate("");
                        d.setDescription("");
                        d.setIdRadio(R.id.rdb1);
                        d.setStrRadio("");
                        d.setCheckType("First Check");
                    }
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildPreparationForPatientAdapter);
               }
               else if(selectedItem.equals("เพิ่มบันทึกข้อความ NPO")){
                    Log.d("check", "เพิ่มบันทึกข้อความ NPO");
                    for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                        d.setComplete("0");
                        d.setCheckNote("1");
                        d.setStatus("normal");
                        d.setDescriptionTemplate("");
                        d.setDescription("");
                        d.setIdRadio(R.id.rdb2);
                        d.setStrRadio("NPO");
                        d.setCheckType("First Check");
                    }
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
                else if(selectedItem.equals("เพิ่มยา PRN")){
                    Log.d("check", "เพิ่มยา PRN");
                   for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                       Log.d("check", "PRN = "+d.getPrn());
                   }
                }
                else if(selectedItem.equals("เพิ่มยาที่ไม่บริหารก่อนหน้านี้")){
                    Log.d("check", "เพิ่มยาที่ไม่บริหารก่อนหน้านี้");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave){
            Boolean checkNull = true;
            for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                Log.d("check", "Complete = "+d.getComplete() + " /Note = "+d.getCheckNote());
                if(d.getComplete() == null & d.getCheckNote() == null){
                    d.setComplete("0");
                    d.setCheckNote("0");
                }
                else if(d.getComplete().equals("1") & d.getCheckNote() == null) {
                    d.setCheckNote("0");
                    d.setComplete("1");
                }
                else if(d.getComplete() == null & d.getCheckNote() != null) {
                    d.setComplete("0");
                    if(d.getCheckNote().equals("0"))
                        d.setCheckNote("0");
                    else
                        d.setCheckNote("1");
                }
                else if(d.getComplete().equals("0") & d.getCheckNote() == null) {
                    d.setComplete("0");
                    if(d.getStrRadio().equals("NPO")|d.getStrRadio().equals("ไม่มียา")|d.getStrRadio().equals("ห้องยาส่งยาผิด")|
                            d.getStrRadio().equals("ยาตก/แตก")|d.getStrRadio().equals("ผู้ป่วยไปทำหัตการ"))
                        d.setCheckNote("1");
                    else
                        d.setCheckNote("0");
                }
                else if(d.getComplete() == null & d.getCheckNote().equals("0")) {
                    d.setComplete("0");
                    d.setCheckNote("0");
                }

                if(d.getComplete().equals("0") & d.getCheckNote().equals("0"))
                    checkNull = false;
            }
            if(checkNull) {
                for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                    d.setRFID(RFID);
                    d.setFirstName(firstName);
                    d.setLastName(lastName);
                    d.setWardName(wardName);
                    d.setActualAdmin(dateActualAdmin);
                    d.setActivityHour(time);
                }

                updateDrugData(buildDrugCardListManager.getDaoAll());
                Toast.makeText(getContext(), "บันทึกเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(), "กรุณาเขียนคำอธิบายสำหรับทุกๆ ตัวยาที่ไม่ได้ใส่เครื่องหมายถูก", Toast.LENGTH_LONG).show();
            }
        }
        else if(view.getId() == R.id.btnCancel){
            Intent intent = new Intent(getContext(), PreparationActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", position);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else if(view.getId() == R.id.tvHistory){
            Intent intent = new Intent(getContext(), HistoryPrepareActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("RFID", RFID);
            intent.putExtra("firstname", firstName);
            intent.putExtra("lastname", lastName);
            intent.putExtra("timeposition", timeposition);
            intent.putExtra("position", position);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }




    class DrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            for(DrugCardDao d : dao.getListDrugCardDao()){
                if(d.getDescriptionTemplate() != null){
                    String[] strRadio = d.getDescriptionTemplate().split(",");
                    if (strRadio.length == 1) {
                        d.setDescriptionTemplate(strRadio[0]);
                    } else if (strRadio.length == 2) {
                        d.setDescriptionTemplate(strRadio[0]);
                        d.setStrRadio(strRadio[1]);
                    }
                }
            }
            buildDrugCardListManager.setDao(dao);
            tvDate.setText(dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");


        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "DrugLoadCallback Failure " + t);
        }
    }


    class SaveDrugDataCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
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
            Log.d("check", "*****DrugAdrDao onPostExecute = " +  drugAdrDaos.size());

            if(drugAdrDaos.size() != 0){
                String tempString = "การแพ้ยา:แตะสำหรับดูรายละเอียด";
                SpannableString spanString = new SpannableString(tempString);
                spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
                tvDrugAdr.setText(spanString);
                tvDrugAdr.setTextColor(getResources().getColor(R.color.colorRed));
//                tvDrugAdr.setText("การแพ้ยา:แตะสำหรับรายละเอียด");
            }
            else {
                tvDrugAdr.setText("การแพ้ยา:ไม่มีข้อมูลแพ้ยา");
            }
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
                itemsList = parseXML(soapManager.getDrugADR("Get_Adr", listPatientDataDao.getPatientDao().get(position).getMRN()));
            }
            Log.d("check", "itemsList doInBackground = "+ itemsList);
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
