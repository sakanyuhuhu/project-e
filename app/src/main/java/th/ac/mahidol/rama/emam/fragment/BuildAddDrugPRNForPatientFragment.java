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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.Serializable;
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
import th.ac.mahidol.rama.emam.activity.AddPatientPRNActivity;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAddDrugPRNForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.BuildAddPRNPatientManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataViewOLD;

public class BuildAddDrugPRNForPatientFragment extends Fragment implements View.OnClickListener, Serializable{
    private String  nfcUID, sdlocID, wardName, dateFortvDate, time, firstName, lastName, RFID, toDayDate, prn, tomorrowDate, mrn;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAdr;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildHeaderPatientDataViewOLD buildHeaderPatientDataViewOLD;
    private BuildAddDrugPRNForPatientAdapter buildAddDrugPRNForPatientAdapter;
    private ListDrugCardDao dao, listDrugCardDao;
    private ListPatientDataDao listPatientDataDao;
    private Date datetoDay;
    private Button btnAdd, btnCancel;
    private PatientDataDao patientDao;
    private BuildAddPRNPatientManager buildAddPRNPatientManager = new BuildAddPRNPatientManager();

    public BuildAddDrugPRNForPatientFragment() {
        super();
    }

    public static BuildAddDrugPRNForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time, PatientDataDao patientDao, String prn) {
        BuildAddDrugPRNForPatientFragment fragment = new BuildAddDrugPRNForPatientFragment();
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
        args.putParcelable("patientPRN", patientDao);
        args.putString("prn", prn);
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
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_add_drug_prn, container, false);
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
        patientDao = getArguments().getParcelable("patientPRN");
        prn = getArguments().getString("prn");

        Log.d("check", "BuildAddDrugPRNForPatientFragment nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName +
               " /timeposition = "+timeposition+" /position = " + position+" /time = "+time+" /prn = "+ prn);

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
//        buildHeaderPatientDataViewOLD = (BuildHeaderPatientDataViewOLD)rootView.findViewById(R.id.headerPatientAdapter);
        buildAddDrugPRNForPatientAdapter = new BuildAddDrugPRNForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);

        datetoDay = new Date();
        SimpleDateFormat sdfFortvDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        dateFortvDate = sdfFortvDate.format(datetoDay);
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE,1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        tvTime.setText(time);

////            buildHeaderPatientDataView.setData(listPatientDataDao, position);
//            buildHeaderPatientDataViewOLD.setData(listPatientDataDao, position);
//            Log.d("check", "MRN = "+listPatientDataDao.getPatientDao().get(position).getMRN()+ " SIZE = "+listPatientDataDao.getPatientDao().size());
//            if(timeposition <= 23) {
//                DrugCardDao drugCardDao = new DrugCardDao();
//                drugCardDao.setAdminTimeHour(time);
//                drugCardDao.setDrugUseDate(toDayDate);
//                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
//                drugCardDao.setCheckType("First Check");
//
//                loadDrugPRNData(drugCardDao);
//            }
//            else{
//                DrugCardDao drugCardDao = new DrugCardDao();
//                drugCardDao.setAdminTimeHour(time);
//                drugCardDao.setDrugUseDate(tomorrowDate);
//                drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
//                drugCardDao.setCheckType("First Check");
//
//                loadDrugPRNData(drugCardDao);
//            }
//        }

//        SharedPreferences prefs = getContext().getSharedPreferences("patientprnAll", Context.MODE_PRIVATE);
//        String data = prefs.getString("patientprnAll",null);
//        if(data != null){
//            listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
//            Log.d("check", "****************************"+listPatientDataDao.getPatientDao().size());
//        }

        if(patientDao != null){
            buildHeaderPatientDataView.setData(patientDao, position);
            if(timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(patientDao.getMRN());
                drugCardDao.setCheckType("First Check");

                loadDrugPRNData(drugCardDao);

                PatientDataDao patientDataDao = new PatientDataDao();
                patientDataDao.setTime(time);
                patientDataDao.setDate(toDayDate);
                buildAddPRNPatientManager.checkPatientinData(patientDataDao);
            }
            else{
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(patientDao.getMRN());
                drugCardDao.setCheckType("First Check");

                loadDrugPRNData(drugCardDao);

                PatientDataDao patientDataDao = new PatientDataDao();
                patientDataDao.setTime(time);
                patientDataDao.setDate(tomorrowDate);
                buildAddPRNPatientManager.checkPatientinData(patientDataDao);
            }
        }

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void saveCachePatientData(ListPatientDataDao patientDataDao){
        String json = new Gson().toJson(patientDataDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientprn", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientprn",json);
        editor.apply();
    }

    private void loadDrugPRNData(DrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getCompareDrug(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAdd){
            Boolean checkNull = false;
            for (DrugCardDao d : dao.getListDrugCardDao()) {
                if(d.getComplete() == null)
                    d.setComplete("0");
                if(d.getComplete().equals("1"))
                    checkNull = true;
            }
            if(checkNull) {
                listPatientDataDao = new ListPatientDataDao();
                List<PatientDataDao> patientDataDaoList = new ArrayList<>();
                if(patientDao != null) {
                    patientDao.setTime(time);
                    if (timeposition <= 23) {
                        patientDao.setDate(toDayDate);
                        patientDataDaoList.add(patientDao);
                    } else {
                        patientDao.setDate(tomorrowDate);
                        patientDataDaoList.add(patientDao);
                    }
                }
                listPatientDataDao.setPatientDao(patientDataDaoList);
                saveCachePatientData(listPatientDataDao);
                buildAddPRNPatientManager.appendPatientDao(listPatientDataDao);//add new data to patientindata

                listDrugCardDao = new ListDrugCardDao();
                List<DrugCardDao> listDaoPRN = new ArrayList<>();
                for(DrugCardDao d : dao.getListDrugCardDao()){
                    if(d.getComplete().equals("1")){
                        d.setActivityHour(time);
                        d.setWardName(wardName);
                        d.setRFID(RFID);
                        d.setFirstName(firstName);
                        d.setLastName(lastName);
                        listDaoPRN.add(d);
                    }
                }
                listDrugCardDao.setListDrugCardDao(listDaoPRN);
                Toast.makeText(getContext(), "เพิ่มยาเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                intent.putExtra("nfcUId", nfcUID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("RFID", RFID);
                intent.putExtra("firstname", firstName);
                intent.putExtra("lastname", lastName);
                intent.putExtra("timeposition", timeposition);
                intent.putExtra("position", position);
                intent.putExtra("time", time);
                intent.putExtra("dao",listDrugCardDao);
                intent.putExtra("patientDao", patientDao);
                intent.putExtra("prn", prn);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
            else
                Toast.makeText(getContext(), "กรุณาใส่เครื่องหมายถูกให้ยาที่จะเพิ่ม", Toast.LENGTH_LONG).show();
        }
        else if(view.getId() == R.id.btnCancel){
            if(prn.equals("prepare")){
                Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                intent.putExtra("nfcUId", nfcUID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("RFID", RFID);
                intent.putExtra("firstname", firstName);
                intent.putExtra("lastname", lastName);
                intent.putExtra("timeposition", timeposition);
                intent.putExtra("position", position);
                intent.putExtra("time", time);
                intent.putExtra("prn", prn);
                getActivity().startActivity(intent);
            }
            else {
                Intent intent = new Intent(getContext(), AddPatientPRNActivity.class);
                intent.putExtra("nfcUId", nfcUID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", position);
                intent.putExtra("time", time);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK){
                    if(prn.equals("prepare")) {
                        Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("timeposition", timeposition);
                        intent.putExtra("position", position);
                        intent.putExtra("time", time);
                        intent.putExtra("prn", prn);
                        getActivity().startActivity(intent);
                        return true;
                    }
                    else {
                        Intent intent = new Intent(getContext(), AddPatientPRNActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("position", position);
                        intent.putExtra("time", time);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }






    class DrugLoadCallback implements Callback<ListDrugCardDao>{
        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            tvDate.setText(dateFortvDate + " (จำนวนยา "+dao.getListDrugCardDao().size()+")");
            buildAddDrugPRNForPatientAdapter.setDao(getContext(),  dao);
            listView.setAdapter(buildAddDrugPRNForPatientAdapter);
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "DrugLoadCallback Failure " + t);
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

//            SharedPreferences prefs = getContext().getSharedPreferences("patientprn", Context.MODE_PRIVATE);
//            String data = prefs.getString("patientprn",null);
//            if(data != null){
//                ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
//                Log.d("check", "*****doInBackground data = " + listPatientDataDao.getPatientDao().get(position).getMRN());
//                itemsList = parseXML(soapManager.getDrugADR("Get_Adr", listPatientDataDao.getPatientDao().get(position).getMRN()));
//            }
            patientDao = getArguments().getParcelable("patientPRN");
            if(patientDao != null){
                mrn = patientDao.getMRN();
                Log.d("check", "MRN doInBackground = "+mrn);
                itemsList = parseXML(soapManager.getDrugADR("Get_Adr", mrn));
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
