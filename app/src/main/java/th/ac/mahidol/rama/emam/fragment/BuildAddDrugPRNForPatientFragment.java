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
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAddDrugPRNForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildAddDrugPRNForPatientFragment extends Fragment implements View.OnClickListener, Serializable{
    private String  nfcUID, sdlocID, wardName, dateFortvDate, time, firstName, lastName, RFID;
    private int position, timeposition;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAdr;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildAddDrugPRNForPatientAdapter buildAddDrugPRNForPatientAdapter;
    private ListDrugCardDao dao, listDrugCardDao;
    private Date datetoDay;
    private Button btnAdd;
    private ListPatientDataDao listPatientDataDao;

    public BuildAddDrugPRNForPatientFragment() {
        super();
    }

    public static BuildAddDrugPRNForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time) {
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

        Log.d("check", "BuildAddDrugPRNForPatientFragment nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName +
               " /timeposition = "+timeposition+" /position = " + position+" /time = "+time);

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildAddDrugPRNForPatientAdapter = new BuildAddDrugPRNForPatientAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        datetoDay = new Date();
        SimpleDateFormat sdfFortvDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFortvDate = sdfFortvDate.format(datetoDay);

        tvTime.setText(time);

        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            buildHeaderPatientDataView.setData(listPatientDataDao, position);
            loadDrugPRNData(listPatientDataDao.getPatientDao().get(position).getMRN());
        }

        btnAdd.setOnClickListener(this);
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

    private void loadDrugPRNData(String mrn){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getListMedicalCardForPreparePRN(mrn);
        call.enqueue(new DrugLoadCallback());
    }

    private void updateDrugData(ListDrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().updateDrugData(drugCardDao);
        call.enqueue(new SaveDrugDataCallback());
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
                List<PatientDataDao> patientDataDaoList = new ArrayList<>();
                PatientDataDao patientDataDao = new PatientDataDao();
                patientDataDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());
                patientDataDao.setBedID(listPatientDataDao.getPatientDao().get(position).getBedID());
                patientDataDao.setInitialName(listPatientDataDao.getPatientDao().get(position).getInitialName());
                patientDataDao.setFirstName(listPatientDataDao.getPatientDao().get(position).getFirstName());
                patientDataDao.setLastName(listPatientDataDao.getPatientDao().get(position).getLastName());
                patientDataDao.setIdCardNo(listPatientDataDao.getPatientDao().get(position).getIdCardNo());
                patientDataDao.setMaritalstatus(listPatientDataDao.getPatientDao().get(position).getMaritalstatus());
                patientDataDao.setDob(listPatientDataDao.getPatientDao().get(position).getDob());
                patientDataDao.setGender(listPatientDataDao.getPatientDao().get(position).getGender());
                patientDataDao.setWardName(listPatientDataDao.getPatientDao().get(position).getWardName());
                patientDataDao.setSdlocId(listPatientDataDao.getPatientDao().get(position).getSdlocId());
                patientDataDao.setAge(listPatientDataDao.getPatientDao().get(position).getAge());
                patientDataDao.setTime(time);
                patientDataDaoList.add(patientDataDao);
                listPatientDataDao.setPatientDao(patientDataDaoList);
                saveCachePatientData(listPatientDataDao);

                listDrugCardDao = new ListDrugCardDao();
                List<DrugCardDao> listDaoPRN = new ArrayList<>();
                for(DrugCardDao d : dao.getListDrugCardDao()){
                    if(d.getComplete().equals("1")){
                        d.setActivityHour(String.valueOf(timeposition));
                        listDaoPRN.add(d);
                    }
                }
                listDrugCardDao.setListDrugCardDao(listDaoPRN);
//                int i=0;
//                for (DrugCardDao d : listDrugCardDao.getListDrugCardDao()) {
//                    Log.d("check", i+" id "+ d.getId());
//                    Log.d("check", i+" getAdminTimeHour "+ d.getAdminTimeHour());
//                    Log.d("check", i+" getDrugUseDate "+ d.getDrugUseDate());
//                    Log.d("check", i+" getTradeName "+ d.getTradeName());
//                    Log.d("check", i+" getMRNv"+ d.getMRN());
//                    Log.d("check", i+" getAdminType"+ d.getAdminType());
//                    Log.d("check", i+" getAdminTime "+ d.getAdminTime());
//                    Log.d("check", i+" getPrn "+ d.getPrn());
//                    Log.d("check", i+" getStatus "+ d.getStatus());
//                    Log.d("check", i+" getCheckType "+ d.getCheckType());
//                    Log.d("check", i+" getCheckNote "+ d.getCheckNote());
//                    Log.d("check", i+" getRFID "+ d.getRFID());
//                    Log.d("check", i+" getFirstName "+ d.getFirstName());
//                    Log.d("check", i+" getLastName "+ d.getLastName());
//                    Log.d("check", i+" getActualAdmin "+ d.getActualAdmin());
//                    Log.d("check", i+" getWardName "+ d.getWardName());
//                    Log.d("check", i+" getActivityHour "+ d.getActivityHour());
//                    i++;
//                }
//                updateDrugData(listDrugCardDao);
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
                getActivity().startActivity(intent);
                getActivity().finish();
            }
            else
                Toast.makeText(getContext(), "กรุณาใส่เครื่องหมายถูกให้ยาที่จะเพิ่ม", Toast.LENGTH_LONG).show();
        }
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
