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
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.HistoryAdministrationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAdministrationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.BuildDrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildAdministrationForPatientFragment extends Fragment implements View.OnClickListener{
    private String  nfcUID, sdlocID, wardName, toDayDate, dateFortvDate, dateActualAdmin, time, firstName, lastName, RFID;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAdr, tvHistory;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildAdministrationForPatientAdapter buildAdministrationForPatientAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();
    private Spinner spinner1, spinner2;
    private ListDrugCardDao dao;
    private Date datetoDay;
    private boolean checkNote = false;

    public BuildAdministrationForPatientFragment() {
        super();
    }

    public static BuildAdministrationForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time) {
        BuildAdministrationForPatientFragment fragment = new BuildAdministrationForPatientFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_administration_for_patient, container, false);
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


        listView = (ListView) rootView.findViewById(R.id.lvAdminForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildAdministrationForPatientAdapter = new BuildAdministrationForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        tvHistory = (TextView) rootView.findViewById(R.id.tvHistory);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

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

        SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
        String data = prefs.getString("patientadministration",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildHeaderPatientDataView.setData(listPatientDataDao, position);

            if(timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Administration");

                loadMedicalData(drugCardDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Administration");

                loadMedicalData(drugCardDao);
            }
        }

        getOnClickSpinnerDrugRoute();
        getOnClickSpinnerHelp();
        btnSave.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                }
                else if(selectedItem.equals("กิน") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoPO().getListDrugCardDao().size()+")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO());
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                }
                else if(selectedItem.equals("ฉีด") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoIV().getListDrugCardDao().size()+")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV());
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                }
                else if(selectedItem.equals("อื่นๆ") & dao!=null){
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size()+")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildAdministrationForPatientAdapter);
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
                    for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                        d.setComplete("1");
                        d.setCheckNote("0");
                        d.setStatus("normal");
                        d.setDescriptionTemplate("");
                        d.setDescription("");
                        d.setIdRadio(R.id.rdb1);
                        d.setStrRadio("");
                        d.setCheckType("Administration");
                    }
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                }
                else if(selectedItem.equals("เพิ่มบันทึกข้อความ NPO")){
                    for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                        d.setComplete("0");
                        d.setCheckNote("1");
                        d.setStatus("normal");
                        d.setDescriptionTemplate("");
                        d.setDescription("");
                        d.setIdRadio(R.id.rdb2);
                        d.setStrRadio("NPO");
                        d.setCheckType("Administration");
                    }
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildAdministrationForPatientAdapter);
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
//                else if(d.getComplete().equals("0") & d.getCheckNote() == null) {
//                    d.setComplete("0");
//                    d.setCheckNote("1");
//
//                }
                else if(d.getComplete().equals("0") & d.getCheckNote() == null) {
                    d.setComplete("0");
                    if(d.getStrRadio() != null) {
                        if (d.getStrRadio().equals("NPO") | d.getStrRadio().equals("ไม่มียา") | d.getStrRadio().equals("คนไข้ปฏิเสธ") |
                                d.getStrRadio().equals("คนไข้มีอาการคลื่นไส้อาเจียน") | d.getStrRadio().equals("แทงเส้นเลือดไม่ได้") | d.getStrRadio().equals("แพทย์สั่ง off ณ เวลานั้น") |
                                d.getStrRadio().equals("ผู้ป่วยไปทำหัตการ"))
                            d.setCheckNote("1");
                        else
                            d.setCheckNote("0");
                    }
                    else
                        d.setCheckNote("0");
                }
                else if(d.getComplete() == null & d.getCheckNote().equals("0")) {
                    d.setComplete("1");
                    d.setCheckNote("0");
                }

                if(d.getComplete().equals("0") & d.getCheckNote().equals("0"))
                    checkNull = false;
            }
            if(checkNull) {
                for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()){
                    Log.d("check", "Hold:BP " + d.getStrBP() + "|Heart Rate " + d.getStrHR() + "|CBG " + d.getStrCBG());
                    if (d.getStrBP() != null & d.getStrHR() != null & d.getStrCBG() != null) {
                        d.setDescriptionTemplate("Hold:BP " + d.getStrBP() + "|Heart Rate " + d.getStrHR() + "|CBG " + d.getStrCBG());
                    } else if (d.getStrBP() == null & d.getStrHR() == null & d.getStrCBG() != null) {
                        d.setDescriptionTemplate("Hold:CBG " + d.getStrCBG());
                    } else if (d.getStrBP() == null & d.getStrHR() != null & d.getStrCBG() != null) {
                        d.setDescriptionTemplate("Hold:Heart Rate " + d.getStrHR() + "|CBG " + d.getStrCBG());
                    } else if (d.getStrBP() == null & d.getStrHR() != null & d.getStrCBG() == null){
                        d.setDescriptionTemplate("Hold:Heart Rate " + d.getStrHR());
                    }else if(d.getStrBP() != null & d.getStrHR() == null & d.getStrCBG() == null) {
                        d.setDescriptionTemplate("Hold:BP " + d.getStrBP());
                    }else if(d.getStrBP() != null & d.getStrHR() != null & d.getStrCBG() == null) {
                        d.setDescriptionTemplate("Hold:BP " + d.getStrBP() + "|Heart Rate " + d.getStrHR());
                    }else if(d.getStrBP() != null & d.getStrHR() == null & d.getStrCBG() != null) {
                        d.setDescriptionTemplate("Hold:BP " + d.getStrBP() + "|CBG " + d.getStrCBG());
                    }else if(d.getStrBP() == null & d.getStrHR() == null & d.getStrCBG() == null) {
                        d.setDescriptionTemplate("");
                    }
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
            else
                Toast.makeText(getContext(), "กรุณาเขียนคำอธิบายสำหรับทุกๆ ตัวยาที่ไม่ได้ใส่เครื่องหมายถูก", Toast.LENGTH_LONG).show();
        }
        else if(view.getId() == R.id.tvHistory){
            Intent intent = new Intent(getContext(), HistoryAdministrationActivity.class);
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
        else if(view.getId() == R.id.btnCancel){
            Intent intent = new Intent(getContext(), AdministrationActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
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
                if(d.getCheckType().equals("Second Check")) {
                    if (d.getComplete().equals("1"))
                        d.setComplete(null);
                    else {
                        d.setDescriptionTemplate(null);
                        d.setDescription(null);
                        d.setStrRadio(null);
                    }
                }
                else {
                    if(d.getDescriptionTemplate() != null){
                        Log.d("check", "DescriptionTemplate = "+d.getDescriptionTemplate());
                        String[] strRadio = d.getDescriptionTemplate().split(",");
                        if(strRadio.length == 1){
                            String[] strHold1 = strRadio[0].split(":");
                            if(strHold1.length > 1) {
                                String[] strHold2 = strHold1[1].split("\\|");
                                String[] bp = strHold2[0].split("\\s");
                                if (bp.length == 1)
                                    d.setStrBP("");
                                else
                                    d.setStrBP(bp[1]);

                                String[] hr = strHold2[1].split("\\s");
                                if (hr.length == 2)
                                    d.setStrHR("");
                                else
                                    d.setStrHR(hr[2]);

                                String[] cbg = strHold2[2].split("\\s");
                                if (cbg.length == 1)
                                    d.setStrCBG("");
                                else
                                    d.setStrCBG(cbg[1]);
                            }
                        }
                        else if (strRadio.length == 2) {
                            if(strRadio[0].equals("")){
                                d.setStrRadio(strRadio[1]);
                            }
                            else {
                                d.setStrRadio(strRadio[1]);
                                String[] strHold1 = strRadio[0].split(":");
                                String[] strHold2 = strHold1[1].split("\\|");
                                String[] bp = strHold2[0].split("\\s");
                                if (bp.length == 1)
                                    d.setStrBP("");
                                else
                                    d.setStrBP(bp[1]);

                                String[] hr = strHold2[1].split("\\s");
                                if (hr.length == 2)
                                    d.setStrHR("");
                                else
                                    d.setStrHR(hr[2]);

                                String[] cbg = strHold2[2].split("\\s");
                                if (cbg.length == 1)
                                    d.setStrCBG("");
                                else
                                    d.setStrCBG(cbg[1]);
                            }
                        }
                    }
                }
                if(d.getComplete() == null){
                    checkNote = true;
                }else if(d.getComplete() != null){
                    if(d.getComplete().equals("0")){
                        checkNote = true;
                    }
                }
            }
            buildDrugCardListManager.setDao(dao);
            tvDate.setText(dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");
            if(checkNote){
                btnSave.setVisibility(getView().VISIBLE);
                btnCancel.setVisibility(getView().VISIBLE);
            }
            else{
                btnSave.setVisibility(getView().INVISIBLE);
                btnCancel.setVisibility(getView().INVISIBLE);
            }
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
            }
            else {
                tvDrugAdr.setText("การแพ้ยา:ไม่มีข้อมูลแพ้ยา");
            }
        }

        @Override
        protected List<DrugAdrDao> doInBackground(Void... params) {
            List<DrugAdrDao> itemsList = new ArrayList<DrugAdrDao>();
            SoapManager soapManager = new SoapManager();

            SharedPreferences prefs = getContext().getSharedPreferences("patientadministration", Context.MODE_PRIVATE);
            String data = prefs.getString("patientadministration",null);
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
