package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import th.ac.mahidol.rama.emam.activity.AddDrugNotPrepareActivity;
import th.ac.mahidol.rama.emam.activity.AddDrugPatientPRNActivity;
import th.ac.mahidol.rama.emam.activity.HistoryPrepareActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildListDrugAdrAdapter;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.BuildDrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildPreparationForPatientFragment extends Fragment implements View.OnClickListener {
    private String nfcUID, sdlocID, wardName, toDayDate, dateFortvDate, dateActualAdmin, time, firstName, lastName, RFID, prn, mrn, tomorrowDate, tricker;
    private int position, timeposition, positionPrepare;
    private ListView listView, listViewAdr;
    private TextView tvDate, tvTime, tvDrugAdr, tvHistory;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;
    private BuildListDrugAdrAdapter buildListDrugAdrAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();
    private Spinner spinner1, spinner2;
    private ListDrugCardDao dao, daoPRN, daoDrug;
    private PatientDataDao patientDao;
    private Date datetoDay;
    private boolean checkNote = false;


    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time, ListDrugCardDao listDrugCardDao, PatientDataDao patientDao, String prn) {
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
        args.putParcelable("listdrugcard", listDrugCardDao);
        args.putParcelable("patientDao", patientDao);
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
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    public void init(Bundle savedInstanceState) {

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
        daoPRN = getArguments().getParcelable("listdrugcard");
        patientDao = getArguments().getParcelable("patientDao");
        prn = getArguments().getString("prn");
        positionPrepare = position;
        Log.d("check", "BuildPreparationForPatientFragment nfcUId = " + nfcUID + " /sdlocId = " + sdlocID + " /wardName = " + wardName + " /RFID = " + RFID + " /firstName = " + firstName + " /lastName = " + lastName +
                " /timeposition = " + timeposition + " /position = " + position + " /time = " + time + " /prn = " + prn);

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView) rootView.findViewById(R.id.headerPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();
        buildListDrugAdrAdapter = new BuildListDrugAdrAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAdr = (TextView) rootView.findViewById(R.id.tvDrugAdr);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        tvHistory = (TextView) rootView.findViewById(R.id.tvHistory);

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfForActualAdmin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfFortvDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        toDayDate = sdfForDrugUseDate.format(datetoDay);
        dateFortvDate = sdfFortvDate.format(datetoDay);
        dateActualAdmin = sdfForActualAdmin.format(datetoDay);
        Calendar c = Calendar.getInstance();
        c.setTime(datetoDay);
        c.add(Calendar.DATE, 1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());

        tvTime.setText(time);
        tvDate.setText(dateFortvDate);

        if (patientDao != null) {
            mrn = patientDao.getMRN();
            buildHeaderPatientDataView.setData(patientDao, position);
            if (timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(patientDao.getMRN());
                drugCardDao.setCheckType("First Check");

                loadMedicalData(drugCardDao);
            } else {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(patientDao.getMRN());
                drugCardDao.setCheckType("First Check");

                loadMedicalData(drugCardDao);
            }
        }

        getOnClickSpinnerDrugRoute();
        getOnClickSpinnerHelp();
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvHistory.setOnClickListener(this);

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

    private void updateDrugData(ListDrugCardDao drugCardDao) {
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().updateDrugData(drugCardDao);
        call.enqueue(new SaveDrugDataCallback());
    }

    private void getOnClickSpinnerDrugRoute() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("ทั้งหมด") & daoDrug != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + daoDrug.getListDrugCardDao().size() + ")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                } else if (selectedItem.equals("กิน") & daoDrug != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + buildDrugCardListManager.getDaoPO().getListDrugCardDao().size() + ")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                } else if (selectedItem.equals("ฉีด") & daoDrug != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + buildDrugCardListManager.getDaoIV().getListDrugCardDao().size() + ")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                } else if (selectedItem.equals("อื่นๆ") & daoDrug != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size() + ")");
                    buildPreparationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER());
                    listView.setAdapter(buildPreparationForPatientAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getOnClickSpinnerHelp() {
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("เลือกยาทั้งหมด")) {
                    Log.d("check", "เลือกยาทั้งหมด");
                    for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
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
                } else if (selectedItem.equals("เพิ่มบันทึกข้อความ NPO")) {
                    Log.d("check", "เพิ่มบันทึกข้อความ NPO");
                    for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
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
                } else if (selectedItem.equals("เพิ่มยา PRN")) {
                    Log.d("check", "เพิ่มยา PRN");
                    Intent intent = new Intent(getContext(), AddDrugPatientPRNActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("RFID", RFID);
                    intent.putExtra("firstname", firstName);
                    intent.putExtra("lastname", lastName);
                    intent.putExtra("timeposition", timeposition);
                    intent.putExtra("position", positionPrepare);
                    intent.putExtra("time", time);
                    intent.putExtra("patientPRN", patientDao);
                    intent.putExtra("prn", prn);
                    getActivity().startActivity(intent);
                } else if (selectedItem.equals("เพิ่มยาที่ไม่บริหารก่อนหน้านี้")) {
                    Log.d("check", "เพิ่มยาที่ไม่บริหารก่อนหน้านี้");
                    if (timeposition <= 23) {
                        Intent intent = new Intent(getContext(), AddDrugNotPrepareActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("position", positionPrepare);
                        intent.putExtra("time", time);
                        intent.putExtra("prn", prn);
                        intent.putExtra("mrn", mrn);
                        intent.putExtra("checkType", "First Check");
                        intent.putExtra("date", toDayDate);
                        intent.putExtra("patientDao", patientDao);
                        getActivity().startActivity(intent);

                    } else {
                        Intent intent = new Intent(getContext(), AddDrugNotPrepareActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("position", positionPrepare);
                        intent.putExtra("time", time);
                        intent.putExtra("prn", prn);
                        intent.putExtra("mrn", mrn);
                        intent.putExtra("checkType", "First Check");
                        intent.putExtra("date", tomorrowDate);
                        intent.putExtra("patientDao", patientDao);
                        getActivity().startActivity(intent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            Boolean checkNull = true;
            for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
                Log.d("check", "Complete = " + d.getComplete() + " /Note = " + d.getCheckNote() + " /Template = " + d.getDescriptionTemplate());
                if (d.getComplete() == null & d.getCheckNote() == null) {
                    d.setComplete("0");
                    d.setCheckNote("0");
                } else if (d.getComplete().equals("1") & d.getCheckNote() == null) {
                    d.setCheckNote("0");
                    d.setComplete("1");
                } else if (d.getComplete() == null & d.getCheckNote() != null) {
                    d.setComplete("0");
                    if (d.getCheckNote().equals("0"))
                        d.setCheckNote("0");
                    else
                        d.setCheckNote("1");
                } else if (d.getComplete().equals("0") & d.getCheckNote() == null) {
                    d.setComplete("0");
                    if (d.getStrRadio() != null) {
                        if (d.getStrRadio().equals("NPO") | d.getStrRadio().equals("ไม่มียา") | d.getStrRadio().equals("ห้องยาส่งยาผิด") |
                                d.getStrRadio().equals("ยาตก/แตก") | d.getStrRadio().equals("ผู้ป่วยไปทำหัตการ"))
                            d.setCheckNote("1");
                        else
                            d.setCheckNote("0");
                    } else
                        d.setCheckNote("0");
                } else if (d.getComplete() == null & d.getCheckNote().equals("0")) {
                    d.setComplete("0");
                    d.setCheckNote("0");
                }

                if (d.getComplete().equals("0") & d.getCheckNote().equals("0"))
                    checkNull = false;
            }
            if (checkNull) {
                for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
                    d.setRFID(RFID);
                    d.setFirstName(firstName);
                    d.setLastName(lastName);
                    d.setWardName(wardName);
                    d.setActualAdmin(dateActualAdmin);
                    d.setActivityHour(time);

                }
                updateDrugData(buildDrugCardListManager.getDaoAll());
                prn = "prepare";
                tricker = "save";
                Toast.makeText(getContext(), "บันทึกเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PreparationActivity.class);
                intent.putExtra("nfcUId", nfcUID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                intent.putExtra("save", tricker);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "กรุณาเขียนคำอธิบายสำหรับทุกๆ ตัวยาที่ไม่ได้ใส่เครื่องหมายถูก", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btnCancel) {
            if (prn.equals("prepare")) {
                Intent intent = new Intent(getContext(), PreparationActivity.class);
                intent.putExtra("nfcUId", nfcUID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("position", timeposition);
                intent.putExtra("time", time);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else {
                Intent intent = new Intent(getContext(), AddDrugPatientPRNActivity.class);
                intent.putExtra("nfcUId", nfcUID);
                intent.putExtra("sdlocId", sdlocID);
                intent.putExtra("wardname", wardName);
                intent.putExtra("RFID", RFID);
                intent.putExtra("firstname", firstName);
                intent.putExtra("lastname", lastName);
                intent.putExtra("timeposition", timeposition);
                intent.putExtra("position", positionPrepare);
                intent.putExtra("time", time);
                intent.putExtra("prn", prn);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        } else if (view.getId() == R.id.tvHistory) {
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
            intent.putExtra("patientDao", patientDao);
            intent.putExtra("prn", prn);
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
                    if (prn.equals("prepare")) {
                        Intent intent = new Intent(getContext(), PreparationActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("position", timeposition);
                        intent.putExtra("time", time);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        return true;
                    } else {
                        Intent intent = new Intent(getContext(), AddDrugPatientPRNActivity.class);
                        intent.putExtra("nfcUId", nfcUID);
                        intent.putExtra("sdlocId", sdlocID);
                        intent.putExtra("wardname", wardName);
                        intent.putExtra("RFID", RFID);
                        intent.putExtra("firstname", firstName);
                        intent.putExtra("lastname", lastName);
                        intent.putExtra("timeposition", timeposition);
                        intent.putExtra("position", positionPrepare);
                        intent.putExtra("time", time);
                        intent.putExtra("prn", prn);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    class DrugLoadCallback implements Callback<ListDrugCardDao> {
        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            if (daoPRN != null) {
                if (dao.getListDrugCardDao().size() == 0 & daoPRN.getListDrugCardDao().size() != 0) {
                    daoDrug = new ListDrugCardDao();
                    List<DrugCardDao> listDaoPRN = new ArrayList<>();
                    for (DrugCardDao d : daoPRN.getListDrugCardDao()) {
                        if (d.getComplete().equals("1")) {
                            d.setComplete("0");
                        }
                        listDaoPRN.add(d);
                    }
                    daoDrug.setListDrugCardDao(listDaoPRN);
                    buildDrugCardListManager.setDao(daoDrug);
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + daoDrug.getListDrugCardDao().size() + ")");
                    Log.d("check", "daoPRN = " + daoDrug.getListDrugCardDao().size());
                } else if (dao.getListDrugCardDao().size() != 0 & daoPRN.getListDrugCardDao().size() != 0) {
                    daoDrug = new ListDrugCardDao();
                    List<String> checkDrugPrn = new ArrayList<>();
                    List<DrugCardDao> listDaoSum = new ArrayList<>();
                    for (DrugCardDao d : dao.getListDrugCardDao()) {
                        if (d.getPrn().equals("0")) {
                            checkDrugPrn.add(d.getDrugID());
                            listDaoSum.add(d);
                        }
                        if (d.getPrn().equals("1") & d.getComplete() != null) {
                            if (d.getComplete().equals("1") | d.getCheckNote() != null) {
                                checkDrugPrn.add(d.getDrugID());
                                listDaoSum.add(d);
                            }
                        }
                    }
                    for (DrugCardDao d : daoPRN.getListDrugCardDao()) {
                        if (d.getComplete().equals("1")) {
                            d.setComplete("0");
                        }
                        if (!checkDrugPrn.contains(d.getDrugID())) {
                            checkDrugPrn.add(d.getDrugID());
                            listDaoSum.add(d);
                        }
                    }
                    daoDrug.setListDrugCardDao(listDaoSum);
                    for (DrugCardDao d : daoDrug.getListDrugCardDao()) {
                        if (d.getDescriptionTemplate() != null) {
                            String[] strRadio = d.getDescriptionTemplate().split(",");
                            if (strRadio.length == 1) {
                                d.setDescriptionTemplate(strRadio[0]);
                            } else if (strRadio.length == 2) {
                                d.setDescriptionTemplate(strRadio[0]);
                                d.setStrRadio(strRadio[1]);
                            }
                        }
                    }
                    buildDrugCardListManager.setDao(daoDrug);
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + daoDrug.getListDrugCardDao().size() + ")");
                    Log.d("check", "daoDrug & PRN = " + daoDrug.getListDrugCardDao().size());
                }
            } else {
                Log.d("check", "dao drug = " + dao.getListDrugCardDao().size());
                if (dao.getListDrugCardDao().size() != 0) {
                    daoDrug = new ListDrugCardDao();
                    List<DrugCardDao> listDaoNoPRN = new ArrayList<>();
                    for (DrugCardDao d : dao.getListDrugCardDao()) {
                        if (d.getPrn().equals("0")) {
                            listDaoNoPRN.add(d);
                        }
                        if (d.getPrn().equals("1") & d.getComplete() != null) {
                            if (d.getComplete().equals("1") | d.getComplete().equals("0")) {
                                listDaoNoPRN.add(d);
                            }
                        }
                        if (d.getDescriptionTemplate() != null) {
                            String[] strRadio = d.getDescriptionTemplate().split(",");
                            if (strRadio.length == 1) {
                                d.setDescriptionTemplate(strRadio[0]);
                            } else if (strRadio.length == 2) {
                                d.setDescriptionTemplate(strRadio[0]);
                                d.setStrRadio(strRadio[1]);
                            }
                        }
                        if (d.getComplete() == null) {
                            checkNote = true;
                        } else if (d.getComplete() != null) {
                            if (d.getComplete().equals("0")) {
                                checkNote = true;
                            }
                        }
                    }
                    daoDrug.setListDrugCardDao(listDaoNoPRN);
                    buildDrugCardListManager.setDao(daoDrug);
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + daoDrug.getListDrugCardDao().size() + ")");
                    if (checkNote) {
                        btnSave.setVisibility(getView().VISIBLE);
                        btnCancel.setVisibility(getView().VISIBLE);
                    } else {
                        btnSave.setVisibility(getView().INVISIBLE);
                        btnCancel.setVisibility(getView().INVISIBLE);
                    }
                    Log.d("check", "daoSum  = " + daoDrug.getListDrugCardDao().size());
                }
            }
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "DrugLoadCallback Failure " + t);
        }
    }


    class SaveDrugDataCallback implements Callback<ListDrugCardDao> {

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "NewDrugLoadCallback Failure " + t);
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
                        for (DrugAdrDao d : drugAdrDaos) {
                            DrugAdrDao drugAdrDao = new DrugAdrDao();
                            drugAdrDao.setDrugname(d.getDrugname());
                            drugAdrDao.setSideEffect(d.getSideEffect());
                            drugAdrDao.setNaranjo(d.getNaranjo());
                            drugAdrDaoList.add(drugAdrDao);
                        }

                        listDrugAdrDao.setDrugAdrDaoList(drugAdrDaoList);
                        buildListDrugAdrAdapter.setDao(getContext(), listDrugAdrDao);
                        listViewAdr.setAdapter(buildListDrugAdrAdapter);

                        builder.setView(dialogView);
                        builder.setTitle("ประวัติการแพ้ยา(" + listDrugAdrDao.getDrugAdrDaoList().size() + ")");
                        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create();
                        builder.show().getWindow().setLayout(1200, 700);
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
            patientDao = getArguments().getParcelable("patientDao");
            if (patientDao != null) {
                mrn = patientDao.getMRN();
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
