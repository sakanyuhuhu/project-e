package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.DoubleCheckForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.BuildDrugHistoryAdapter;
import th.ac.mahidol.rama.emam.adapter.BuildHistoryAdapter;
import th.ac.mahidol.rama.emam.adapter.BuildListDrugAdrAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataViewOLD;

public class BuildHistoryDoubleCheckFragment extends Fragment implements View.OnClickListener {
    private String nfcUID, sdlocID, wardName, time, firstName, lastName, RFID, startDate, mrn, newDateStart;
    private int position, timeposition;
    private ListView listView, listViewAdr, lvMedHistory;
    private String[] admintime;
    private TextView tvTime, tvDrugAdr, tvDoublecheck, tvDate, tvDrugName, tvRoute, tvDosage, tvNumAdr;
    private ImageView imgCalendar;
    private BuildHeaderPatientDataViewOLD buildHeaderPatientDataViewOLD;
    private BuildHistoryAdapter buildHistoryAdapter;
    private BuildListDrugAdrAdapter buildListDrugAdrAdapter;
    private BuildDrugHistoryAdapter buildDrugHistoryAdapter;
    private PatientDataDao patientDouble;
    private ListDrugCardDao dao;
    private Date datetoDay, date;
    long startMillis = 0;
    long endMillis = 0;

    public BuildHistoryDoubleCheckFragment() {
        super();
    }

    public static BuildHistoryDoubleCheckFragment newInstance(String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, PatientDataDao patientDouble, String time) {
        BuildHistoryDoubleCheckFragment fragment = new BuildHistoryDoubleCheckFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putString("RFID", RFID);
        args.putString("firstname", firstName);
        args.putString("lastname", lastName);
        args.putInt("timeposition", timeposition);
        args.putInt("position", position);
        args.putParcelable("patientDouble", patientDouble);
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
        View rootView = inflater.inflate(R.layout.fragment_doublecheck_history, container, false);
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
        RFID = getArguments().getString("RFID");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        timeposition = getArguments().getInt("timeposition");
        position = getArguments().getInt("position");
        patientDouble = getArguments().getParcelable("patientDouble");
        time = getArguments().getString("time");

        Log.d("check", "BuildHistoryDoubleCheckFragment nfcUId = " + nfcUID + " /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = " + RFID + " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " + timeposition + " /position = " + position + " /time = " + time);

        Log.d("check", "patientDouble mrn = " + patientDouble.getMRN());

        listView = (ListView) rootView.findViewById(R.id.lvHistoryAdapter);
        buildHeaderPatientDataViewOLD = (BuildHeaderPatientDataViewOLD) rootView.findViewById(R.id.headerPatientAdapter);
        buildHistoryAdapter = new BuildHistoryAdapter();
        buildListDrugAdrAdapter = new BuildListDrugAdrAdapter();
        buildDrugHistoryAdapter = new BuildDrugHistoryAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        imgCalendar = (ImageView) rootView.findViewById(R.id.imgCalendar);

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("MM/dd/yyyy");
        startDate = sdfForDrugUseDate.format(datetoDay);
        tvDate.setText(startDate);
        tvTime.setText(time);
        admintime = time.split(":");

        if (patientDouble != null) {
            buildHeaderPatientDataViewOLD.setData(patientDouble);
        }

        getDrugDouble(patientDouble.getMRN(), "Second Check", startDate);
        tvDoublecheck.setOnClickListener(this);
        imgCalendar.setOnClickListener(this);
    }

    private void getDrugDouble(String mrn, String checkType, String startDate) {
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getMedicalHistory(mrn, checkType, startDate);
        call.enqueue(new DrugHistoryLoadCallback());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvDoublecheck) {
            Intent intent = new Intent(getContext(), DoubleCheckForPatientActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("RFID", RFID);
            intent.putExtra("firstname", firstName);
            intent.putExtra("lastname", lastName);
            intent.putExtra("timeposition", timeposition);
            intent.putExtra("position", position);
            intent.putExtra("patientDouble", patientDouble);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (view.getId() == R.id.imgCalendar) {
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
                        startDate = (pickMonth + 1) + "/" + pickDay + "/" + pickYear;
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        try {
                            date = df.parse(startDate);
                            newDateStart = df.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvDate.setText(newDateStart);
                        getDrugDouble(patientDouble.getMRN(), "Second Check", newDateStart);
                    } else {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getCurrentHour(),
                                pickMinute = timePicker.getCurrentMinute());
                        startDate = (pickMonth + 1) + "/" + pickDay + "/" + pickYear;
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        try {
                            date = df.parse(startDate);
                            newDateStart = df.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvDate.setText(newDateStart);
                        getDrugDouble(patientDouble.getMRN(), "Second Check", newDateStart);
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


    class DrugHistoryLoadCallback implements Callback<ListDrugCardDao> {
        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            List<DrugCardDao> listDrugTime = new ArrayList<>();
            for (DrugCardDao d : dao.getListDrugCardDao()) {
                if (d.getActivityHour().equals(admintime[0])) {
                    listDrugTime.add(d);
                }
            }
            dao.setListDrugCardDao(listDrugTime);
            buildHistoryAdapter.setDao(getContext(), dao);
            listView.setAdapter(buildHistoryAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater.inflate(R.layout.custom_dialog_history, null);
                    lvMedHistory = (ListView) dialogView.findViewById(R.id.lvMedHistory);
                    tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
                    tvRoute = (TextView) dialogView.findViewById(R.id.tvRoute);
                    tvDosage = (TextView) dialogView.findViewById(R.id.tvDosage);

                    tvDrugName.setText(dao.getListDrugCardDao().get(position).getTradeName());
                    tvRoute.setText("Route: " + dao.getListDrugCardDao().get(position).getRoute());
                    tvDosage.setText("Dosage: " + dao.getListDrugCardDao().get(position).getDose() +" "+ dao.getListDrugCardDao().get(position).getUnit());
                    buildDrugHistoryAdapter.setDao(dao.getListDrugCardDao().get(position), time);
                    lvMedHistory.setAdapter(buildDrugHistoryAdapter);

                    builder.setView(dialogView);
                    builder.create();
                    builder.show();

                }
            });
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
            Log.d("check", "*****DrugAdrDao onPostExecute = " + drugAdrDaos.size());

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
            patientDouble = getArguments().getParcelable("patientDouble");
            if (patientDouble != null) {
                mrn = patientDouble.getMRN();
                Log.d("check", "MRN doInBackground = " + mrn);
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
