package th.ac.mahidol.rama.emam.fragment.history;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.history.History_DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.history.History_PrepareActivity;
import th.ac.mahidol.rama.emam.activity.history.PatientAllActivity;
import th.ac.mahidol.rama.emam.adapter.BuildHistoryPrepareAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.history.BuildHistoryHeaderPatientDataView;

public class BuildHistory_AdministrationFragment extends Fragment implements View.OnClickListener{
    private String  nfcUID, sdlocID, wardName, toDayDate, dateSelect;
    private int position;
    private ListView listView;
    private TextView tvDrugAdr, tvDate,tvPreparation, tvDoublecheck, tvCurrentMed;
    private ImageView imgCalendar;
    private BuildHistoryHeaderPatientDataView buildHistoryHeaderPatientDataView;
    private BuildHistoryPrepareAdapter buildHistoryPrepareAdapter;
    private Date datetoDay;
    long startMillis = 0;
    long endMillis = 0;

    public BuildHistory_AdministrationFragment() {
        super();
    }

    public static BuildHistory_AdministrationFragment newInstance(String nfcUID, String sdlocID, String wardName, int position){
        BuildHistory_AdministrationFragment fragment = new BuildHistory_AdministrationFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", position);
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
        View rootView = inflater.inflate(R.layout.fragment_history_administration, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        new getADRForPatient().execute();
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        position = getArguments().getInt("position");

        Log.d("check", "BuildHistory_AdministrationFragment nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName + " /position = "+ position);

        listView = (ListView) rootView.findViewById(R.id.lvHistoryAdapter);
        buildHistoryHeaderPatientDataView = (BuildHistoryHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildHistoryPrepareAdapter = new BuildHistoryPrepareAdapter();

        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
//        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
//        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
//        tvCurrentMed = (TextView) rootView.findViewById(R.id.tvCurrentMed);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        imgCalendar = (ImageView) rootView.findViewById(R.id.imgCalendar);

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("dd/MM/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        dateSelect = toDayDate;
        tvDate.setText(dateSelect);

        SharedPreferences prefs = getContext().getSharedPreferences("patientalldata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientalldata",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            Log.d("check", "data size = "+listPatientDataDao.getPatientDao().size()+ " position = "+position);
            buildHistoryHeaderPatientDataView.setData(listPatientDataDao, position);

        }

        getDrugFromPraration();
//        tvDoublecheck.setOnClickListener(this);
//        tvPreparation.setOnClickListener(this);
//        tvCurrentMed.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);

    }

    private void getDrugFromPraration(){

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tvDoublecheck){
            Intent intent = new Intent(getContext(), History_DoubleCheckActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else if(view.getId() == R.id.tvPreparation){
            Intent intent = new Intent(getContext(), History_PrepareActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
//        else if(view.getId() == R.id.tvCurrentMed){
//            Intent intent = new Intent(getContext(), CurrentMedActivity.class);
//            intent.putExtra("nfcUId", nfcUID);
//            intent.putExtra("sdlocId", sdlocID);
//            intent.putExtra("wardname", wardName);
//            getActivity().startActivity(intent);
//            getActivity().finish();
//        }
        else if(view.getId() == R.id.imgCalendar){
            final View dialogViewDate = View.inflate(getActivity(), R.layout.custom_dialog_set_date, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            dialogViewDate.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = (DatePicker) dialogViewDate.findViewById(R.id.date_picker);
                    TimePicker timePicker = (TimePicker) dialogViewDate.findViewById(R.id.time_picker);

                    int pickYear, pickMonth, pickDay, pickHour = 0, pickMinute = 0;
                    Calendar calendar = null;
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getHour(),
                                pickMinute = timePicker.getMinute());
                        dateSelect = pickDay+"/"+(pickMonth+1)+"/"+pickYear;
                        tvDate.setText(dateSelect);
                    } else {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getCurrentHour(),
                                pickMinute = timePicker.getCurrentMinute());
                        dateSelect = pickDay+"/"+(pickMonth+1)+"/"+pickYear;
                        tvDate.setText(dateSelect);
                    }

                    Calendar timePick = Calendar.getInstance();
                    timePick.set(pickYear, pickMonth, pickDay, pickHour, pickMinute);
                    startMillis = timePick.getTimeInMillis();
                    endMillis = startMillis + 60 * 60 * 1000;
                    alertDialog.dismiss();
                }
            });
            alertDialog.setView(dialogViewDate);
            alertDialog.show();
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
                    Intent intent = new Intent(getContext(), PatientAllActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    public class getADRForPatient extends AsyncTask<Void, Void, List<DrugAdrDao>> {

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

            SharedPreferences prefs = getContext().getSharedPreferences("patientalldata", Context.MODE_PRIVATE);
            String data = prefs.getString("patientalldata",null);
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
