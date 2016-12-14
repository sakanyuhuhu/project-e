package th.ac.mahidol.rama.emam.fragment.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import th.ac.mahidol.rama.emam.activity.alarm.PatientOnTimeActivity;
import th.ac.mahidol.rama.emam.adapter.BuildListDrugAdrAdapter;
import th.ac.mahidol.rama.emam.adapter.alarm.BuildPatientMedOnTimeAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataWhiteView;

public class BuildPatientMedOnTimeFragment extends Fragment implements View.OnClickListener {
    private String wardID, sdlocID, wardName, toDayDate, mrn, time;
    private int position;
    private ListView listView, listViewAdr;
    private TextView tvDrugAdr, tvNumAdr;
    private Button btnOK;
    private BuildHeaderPatientDataWhiteView buildHeaderPatientDataWhiteView;
    private BuildPatientMedOnTimeAdapter buildPatientMedOnTimeAdapter;
    private BuildListDrugAdrAdapter buildListDrugAdrAdapter;
    private ListDrugCardDao dao;
    private PatientDataDao patientDao;
    private Date datetoDay;


    public BuildPatientMedOnTimeFragment() {
        super();
    }

    public static BuildPatientMedOnTimeFragment newInstance(String wardID, String sdlocID, String wardName, PatientDataDao patientDao, int position, String time) {
        BuildPatientMedOnTimeFragment fragment = new BuildPatientMedOnTimeFragment();
        Bundle args = new Bundle();
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putParcelable("patient", patientDao);
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
        View rootView = inflater.inflate(R.layout.fragment_patient_med, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    public void init(Bundle savedInstanceState) {

    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        new getADRForPatient().execute();
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        patientDao = getArguments().getParcelable("patient");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");

        listView = (ListView) rootView.findViewById(R.id.lvMedication);
        buildHeaderPatientDataWhiteView = (BuildHeaderPatientDataWhiteView) rootView.findViewById(R.id.headerPatientAdapter);
        buildPatientMedOnTimeAdapter = new BuildPatientMedOnTimeAdapter();
        buildListDrugAdrAdapter = new BuildListDrugAdrAdapter();

        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        btnOK = (Button) rootView.findViewById(R.id.btnOK);

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = sdfForDrugUseDate.format(datetoDay);

        if (patientDao != null) {
            mrn = patientDao.getMRN();
            buildHeaderPatientDataWhiteView.setData(patientDao, position);

            DrugCardDao drugCardDao = new DrugCardDao();
            drugCardDao.setAdminTimeHour(time);
            drugCardDao.setDrugUseDate(toDayDate);
            drugCardDao.setMRN(patientDao.getMRN());
            drugCardDao.setCheckType("First Check");

            loadMedicalData(drugCardDao);
        }

        btnOK.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadMedicalData(DrugCardDao drugCardDao) {
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOK) {
            Intent intent = new Intent(getContext(), PatientOnTimeActivity.class);
            intent.putExtra("wardId", wardID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
            getActivity().finish();
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
                if (event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(getContext(), PatientOnTimeActivity.class);
                    intent.putExtra("wardId", wardID);
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


    class DrugLoadCallback implements Callback<ListDrugCardDao> {
        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            List<String> listDrugId = new ArrayList<>();
            List<DrugCardDao> listDrug = new ArrayList<>();
            for(DrugCardDao d : dao.getListDrugCardDao()) {
                if (d.getPrn().equals("0")) {
                    if (!listDrugId.contains(d.getDrugID())) {
                        listDrugId.add(d.getDrugID());
                        listDrug.add(d);
                    }
                }
            }
            dao.setListDrugCardDao(listDrug);
            buildPatientMedOnTimeAdapter.setDao(dao);
            listView.setAdapter(buildPatientMedOnTimeAdapter);
                   }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "DrugLoadCallback Failure " + t);
        }
    }


    public class getADRForPatient extends AsyncTask<Void, Void, List<DrugAdrDao>> {

        @Override
        protected void onPostExecute(final List<DrugAdrDao> drugAdrDaos) {
            super.onPostExecute(drugAdrDaos);
            final ListDrugAdrDao listDrugAdrDao = new ListDrugAdrDao();
            final List<DrugAdrDao> drugAdrDaoList = new ArrayList<DrugAdrDao>();
            if (drugAdrDaos.size() != 0) {
                String tempString = "การแพ้ยา:แตะสำหรับดูรายละเอียด";
                SpannableString spanString = new SpannableString(tempString);
                spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
                tvDrugAdr.setText(spanString);
                tvDrugAdr.setTextColor(getResources().getColor(R.color.colorRed));
                tvDrugAdr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custom_dialog_adr, null);
                        listViewAdr = (ListView) dialogView.findViewById(R.id.listViewAdr);
                        tvNumAdr = (TextView) dialogView.findViewById(R.id.tvNumAdr);
                        for (DrugAdrDao d : drugAdrDaos) {
                            DrugAdrDao drugAdrDao = new DrugAdrDao();
                            drugAdrDao.setDrugname(d.getDrugname());
                            drugAdrDao.setSideEffect(d.getSideEffect());
                            drugAdrDao.setNaranjo(d.getNaranjo());
                            drugAdrDaoList.add(drugAdrDao);
                        }

                        listDrugAdrDao.setDrugAdrDaoList(drugAdrDaoList);
                        buildListDrugAdrAdapter.setDao(getContext(), listDrugAdrDao);
                        tvNumAdr.setText("ประวัติการแพ้ยา(" + listDrugAdrDao.getDrugAdrDaoList().size() + ")");
                        listViewAdr.setAdapter(buildListDrugAdrAdapter);
                        builder.setView(dialogView);
                        builder.create();
                        builder.show();
                    }
                });
            } else {
                tvDrugAdr.setText("การแพ้ยา:ไม่มีข้อมูลแพ้ยา");
            }
        }

        @Override
        protected List<DrugAdrDao> doInBackground(Void... params) {
            List<DrugAdrDao> itemsList = new ArrayList<DrugAdrDao>();
            SoapManager soapManager = new SoapManager();
            patientDao = getArguments().getParcelable("patient");
            if (patientDao != null) {
                mrn = patientDao.getMRN();
                itemsList = parseXML(soapManager.getDrugADR("Get_Adr", mrn));
            }
            return itemsList;
        }

        private ArrayList<DrugAdrDao> parseXML(String soap) {
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
