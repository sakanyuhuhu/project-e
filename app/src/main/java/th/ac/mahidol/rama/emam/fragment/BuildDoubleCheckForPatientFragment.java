package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import th.ac.mahidol.rama.emam.adapter.BuildDoubleCheckForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.BuildDrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildDoubleCheckForPatientFragment extends Fragment implements View.OnClickListener{
    private String  sdlocID, wardName, toDayDate, dateFortvDate, dateActualAdmin, time, firstName, lastName, RFID;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAdr;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildDoubleCheckForPatientAdapter buildDoubleCheckForPatientAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();
    private Spinner spinner1;
    private ListDrugCardDao dao;
    private Date datetoDay;

    public BuildDoubleCheckForPatientFragment() {
        super();
    }

    public static BuildDoubleCheckForPatientFragment newInstance(String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time) {
        BuildDoubleCheckForPatientFragment fragment = new BuildDoubleCheckForPatientFragment();
        Bundle args = new Bundle();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doublecheck_for_patient , container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        new getADRForPatient().execute();
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        RFID = getArguments().getString("RFID");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        timeposition = getArguments().getInt("timeposition");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");


        listView = (ListView) rootView.findViewById(R.id.lvDoubleForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildDoubleCheckForPatientAdapter = new BuildDoubleCheckForPatientAdapter();

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
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        String tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        tvTime.setText(time);


        SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
        String data = prefs.getString("patientdoublecheck",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildHeaderPatientDataView.setData(listPatientDataDao, position);

            if(timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Second Check");

                loadMedicalData(drugCardDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                drugCardDao.setCheckType("Second Check");

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

    private void updateDrugData(ListDrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().updateDrugData(drugCardDao);
        call.enqueue(new SaveDrugDataCallback());
    }

    private void getOnClickSpinner(){
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();

                if(selectedItem.equals("ทั้งหมด") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");
                    buildDoubleCheckForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildDoubleCheckForPatientAdapter);
                }
                else if(selectedItem.equals("กิน") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoPO().getListDrugCardDao().size()+")");
                    buildDoubleCheckForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO());
                    listView.setAdapter(buildDoubleCheckForPatientAdapter);
                }
                else if(selectedItem.equals("ฉีด") & dao!=null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoIV().getListDrugCardDao().size()+")");
                    buildDoubleCheckForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV());
                    listView.setAdapter(buildDoubleCheckForPatientAdapter);
                }
                else if(selectedItem.equals("อื่นๆ") & dao!=null){
                    tvDate.setText(dateFortvDate + " (จำนวนยา "+ buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size()+")");
                    buildDoubleCheckForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildDoubleCheckForPatientAdapter);
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
                    d.setCheckNote("1");
                }
                else if(d.getComplete() == null & d.getCheckNote().equals("0")) {
                    d.setComplete("1");
                    d.setCheckNote("0");
                }

                if(d.getComplete().equals("0") & d.getCheckNote().equals("0"))
                    checkNull = false;

            }
            if(checkNull) {
                for(DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
                    if (d.getStrType() != null & d.getStrSize() != null & d.getStrForget() != null) {
                        d.setDescriptionTemplate("ผิดชนิด:" + d.getStrType() + ",ผิดขนาด:" + d.getStrSize() + ",ลืมจัด:" + d.getStrForget());
                    } else if (d.getStrType() == null & d.getStrSize() == null & d.getStrForget() != null) {
                        d.setDescriptionTemplate("ลืมจัด:" + d.getStrForget());
                    } else if (d.getStrType() == null & d.getStrSize() != null & d.getStrForget() != null) {
                        d.setDescriptionTemplate("ผิดขนาด:" + d.getStrSize() + ",ลืมจัด:" + d.getStrForget());
                    } else if (d.getStrType() == null & d.getStrSize() != null & d.getStrForget() == null){
                        d.setDescriptionTemplate("ผิดขนาด:" + d.getStrSize());
                    }else if(d.getStrType() != null & d.getStrSize() == null & d.getStrForget() == null) {
                        d.setDescriptionTemplate("ผิดชนิด:" + d.getStrType());
                    }else if(d.getStrType() != null & d.getStrSize() != null & d.getStrForget() == null) {
                        d.setDescriptionTemplate("ผิดชนิด:" + d.getStrType() + ",ผิดขนาด:" + d.getStrSize());
                    }else if(d.getStrType() != null& d.getStrSize() == null & d.getStrForget() != null) {
                        d.setDescriptionTemplate("ผิดชนิด:" + d.getStrType() + ",ลืมจัด:" + d.getStrForget());
                    }else if(d.getStrType() == null & d.getStrSize() == null & d.getStrForget() == null) {
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
            else {
                Toast.makeText(getContext(), "กรุณาเขียนคำอธิบายสำหรับทุกๆ ตัวยาที่ไม่ได้ใส่เครื่องหมายถูก", Toast.LENGTH_LONG).show();
            }
        }

    }



    class DrugLoadCallback implements Callback<ListDrugCardDao> {

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            for(DrugCardDao d : dao.getListDrugCardDao()){
                if(d.getCheckType().equals("First Check")) {
                    Log.d("check", "Complete = "+d.getComplete());
                    if ((d.getComplete().equals("1")))
                        d.setComplete(null);
                    else {
                        d.setDescription(null);
                        d.setDescriptionTemplate(null);
                    }
                }
                else{
                    if(d.getDescriptionTemplate() != null){
                        String[] strCheckbox1 = d.getDescriptionTemplate().split(",");
                        if (strCheckbox1.length == 1) {
                            String[] strCheckbox2 = strCheckbox1[0].split(":");
                            if(strCheckbox2.length > 1) {
                                if (strCheckbox2[0].equals("ผิดชนิด"))
                                    d.setStrType(strCheckbox2[1]);
                                else if (strCheckbox2[0].equals("ผิดขนาด"))
                                    d.setStrSize(strCheckbox2[1]);
                                else if (strCheckbox2[0].equals("ลืมจัด"))
                                    d.setStrForget(strCheckbox2[1]);
                            }
                            else {
                                if (strCheckbox2[0].equals("ผิดชนิด"))
                                    d.setStrType("");
                                else if (strCheckbox2[0].equals("ผิดขนาด"))
                                    d.setStrSize("");
                                else if (strCheckbox2[0].equals("ลืมจัด"))
                                    d.setStrForget("");
                            }

                        } else if (strCheckbox1.length == 2) {
                            String[] strCheckbox2 = strCheckbox1[0].split(":");
                            String[] strCheckbox3 = strCheckbox1[1].split(":");
                            if(strCheckbox2.length > 1) {
                                if (strCheckbox2[0].equals("ผิดชนิด"))
                                    d.setStrType(strCheckbox2[1]);
                                else if (strCheckbox2[0].equals("ผิดขนาด"))
                                    d.setStrSize(strCheckbox2[1]);
                                else if (strCheckbox2[0].equals("ลืมจัด"))
                                    d.setStrForget(strCheckbox2[1]);
                            }
                            else {
                                if (strCheckbox2[0].equals("ผิดชนิด"))
                                    d.setStrType("");
                                else if (strCheckbox2[0].equals("ผิดขนาด"))
                                    d.setStrSize("");
                                else if (strCheckbox2[0].equals("ลืมจัด"))
                                    d.setStrForget("");
                            }

                            if(strCheckbox3.length > 1) {
                                if (strCheckbox3[0].equals("ผิดชนิด"))
                                    d.setStrType(strCheckbox3[1]);
                                else if (strCheckbox3[0].equals("ผิดขนาด"))
                                    d.setStrSize(strCheckbox3[1]);
                                else if (strCheckbox3[0].equals("ลืมจัด"))
                                    d.setStrForget(strCheckbox3[1]);
                            }
                            else {
                                if (strCheckbox3[0].equals("ผิดชนิด"))
                                    d.setStrType("");
                                else if (strCheckbox3[0].equals("ผิดขนาด"))
                                    d.setStrSize("");
                                else if (strCheckbox3[0].equals("ลืมจัด"))
                                    d.setStrForget("");
                            }
                        } else if (strCheckbox1.length == 3) {
                            String[] strCheckbox2 = strCheckbox1[0].split(":");
                            String[] strCheckbox3 = strCheckbox1[1].split(":");
                            String[] strCheckbox4 = strCheckbox1[2].split(":");
                            if(strCheckbox2.length > 1) {
                                if (strCheckbox2[0].equals("ผิดชนิด"))
                                    d.setStrType(strCheckbox2[1]);
                                else if (strCheckbox2[0].equals("ผิดขนาด"))
                                    d.setStrSize(strCheckbox2[1]);
                                else if (strCheckbox2[0].equals("ลืมจัด"))
                                    d.setStrForget(strCheckbox2[1]);
                            }
                            else {
                                if (strCheckbox2[0].equals("ผิดชนิด"))
                                    d.setStrType("");
                                else if (strCheckbox2[0].equals("ผิดขนาด"))
                                    d.setStrSize("");
                                else if (strCheckbox2[0].equals("ลืมจัด"))
                                    d.setStrForget("");
                            }

                            if(strCheckbox3.length > 1) {
                                if (strCheckbox3[0].equals("ผิดชนิด"))
                                    d.setStrType(strCheckbox3[1]);
                                else if (strCheckbox3[0].equals("ผิดขนาด"))
                                    d.setStrSize(strCheckbox3[1]);
                                else if (strCheckbox3[0].equals("ลืมจัด"))
                                    d.setStrForget(strCheckbox3[1]);
                            }
                            else {
                                if (strCheckbox3[0].equals("ผิดชนิด"))
                                    d.setStrType("");
                                else if (strCheckbox3[0].equals("ผิดขนาด"))
                                    d.setStrSize("");
                                else if (strCheckbox3[0].equals("ลืมจัด"))
                                    d.setStrForget("");
                            }

                            if(strCheckbox4.length > 1) {
                                if (strCheckbox4[0].equals("ผิดชนิด"))
                                    d.setStrType(strCheckbox4[1]);
                                else if (strCheckbox4[0].equals("ผิดขนาด"))
                                    d.setStrSize(strCheckbox4[1]);
                                else if (strCheckbox4[0].equals("ลืมจัด"))
                                    d.setStrForget(strCheckbox4[1]);
                            }
                            else {
                                if (strCheckbox4[0].equals("ผิดชนิด"))
                                    d.setStrType("");
                                else if (strCheckbox4[0].equals("ผิดขนาด"))
                                    d.setStrSize("");
                                else if (strCheckbox4[0].equals("ลืมจัด"))
                                    d.setStrForget("");
                            }
                        }
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
            Log.d("check", "SaveDrugLoadCallback Failure " + t);
        }
    }

    public class getADRForPatient extends AsyncTask<Void, Void, List<DrugAdrDao>> {

        @Override
        protected void onPostExecute(List<DrugAdrDao> drugAdrDaos) {
            super.onPreExecute();
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

            SharedPreferences prefs = getContext().getSharedPreferences("patientdoublecheck", Context.MODE_PRIVATE);
            String data = prefs.getString("patientdoublecheck",null);
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
