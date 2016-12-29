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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
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
import th.ac.mahidol.rama.emam.activity.AdministrationActivity;
import th.ac.mahidol.rama.emam.activity.HistoryAdministrationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildAdministrationForPatientAdapter;
import th.ac.mahidol.rama.emam.adapter.BuildListDrugAdrAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugAdrDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.BuildDrugCardListManager;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchDrugAdrManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataWhiteView;

public class BuildAdministrationForPatientFragment extends Fragment implements View.OnClickListener {
    private String nfcUID, wardID, sdlocID, wardName, toDayDate, dateFortvDate, dateActualAdmin, time, firstName, lastName, RFID, tomorrowDate, tvcurrentTime, useTime, tricker, mrn, txtstatus = null;
    private int position, timeposition, currentTime, adminTime, sumTime;
    private String[] admintime;
    private ListView listView, listViewAdr;
    private TextView tvDate, tvTime, tvDrugAdr, tvHistory, tvCurrentTime, tvNumAdr;
    private EditText txtStatus;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataWhiteView buildHeaderPatientDataWhiteView;
    private BuildAdministrationForPatientAdapter buildAdministrationForPatientAdapter;
    private BuildListDrugAdrAdapter buildListDrugAdrAdapter;
    private BuildDrugCardListManager buildDrugCardListManager = new BuildDrugCardListManager();
    private Spinner spinner1, spinner2;
    private ListDrugCardDao dao;
    private PatientDataDao patientAdmin;
    private Date datetoDay;
    private boolean checkNote = false;
    long startMillis = 0;
    long endMillis = 0;
    private List<String> listHAD;

    public BuildAdministrationForPatientFragment() {
        super();
    }

    public static BuildAdministrationForPatientFragment newInstance(String nfcUID, String wardID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, PatientDataDao patientAdmin, String time) {
        BuildAdministrationForPatientFragment fragment = new BuildAdministrationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putString("RFID", RFID);
        args.putString("firstname", firstName);
        args.putString("lastname", lastName);
        args.putInt("timeposition", timeposition);
        args.putInt("position", position);
        args.putParcelable("patientAdmin", patientAdmin);
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

    public void init(Bundle savedInstanceState) {

    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        new getADRForPatient().execute();
        nfcUID = getArguments().getString("nfcUId");
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        RFID = getArguments().getString("RFID");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        timeposition = getArguments().getInt("timeposition");
        position = getArguments().getInt("position");
        patientAdmin = getArguments().getParcelable("patientAdmin");
        time = getArguments().getString("time");

        listView = (ListView) rootView.findViewById(R.id.lvAdminForPatientAdapter);
        buildHeaderPatientDataWhiteView = (BuildHeaderPatientDataWhiteView) rootView.findViewById(R.id.headerPatientAdapter);
        buildAdministrationForPatientAdapter = new BuildAdministrationForPatientAdapter();
        buildListDrugAdrAdapter = new BuildListDrugAdrAdapter();

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
        SimpleDateFormat sdfFortvCurrentTime = new SimpleDateFormat("HH:mm");

        toDayDate = sdfForDrugUseDate.format(datetoDay);
        dateFortvDate = sdfFortvDate.format(datetoDay);
        dateActualAdmin = sdfForActualAdmin.format(datetoDay);
        tvcurrentTime = sdfFortvCurrentTime.format(datetoDay);
        Calendar c = Calendar.getInstance();
        currentTime = c.get(Calendar.HOUR_OF_DAY);
        c.setTime(datetoDay);
        c.add(Calendar.DATE, 1);
        tomorrowDate = sdfForDrugUseDate.format(c.getTime());
        admintime = time.split(":");
        adminTime = Integer.parseInt(admintime[0]);
        sumTime = currentTime - adminTime;

        tvTime.setText(time);

        getListHAD();

        if (patientAdmin != null) {
            buildHeaderPatientDataWhiteView.setData(patientAdmin, position);
            if (timeposition <= 23) {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(toDayDate);
                drugCardDao.setMRN(patientAdmin.getMRN());
                drugCardDao.setCheckType("Administration");

                loadMedicalData(drugCardDao);
            } else {
                DrugCardDao drugCardDao = new DrugCardDao();
                drugCardDao.setAdminTimeHour(time);
                drugCardDao.setDrugUseDate(tomorrowDate);
                drugCardDao.setMRN(patientAdmin.getMRN());
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

    private void loadMedicalData(DrugCardDao drugCardDao) {

        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }

    private void updateDrugData(ListDrugCardDao drugCardDao) {
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().updateDrugData(drugCardDao);
        call.enqueue(new SaveDrugDataCallback());
    }

    private void getListHAD() {
        Call<List<String>> call = HttpManager.getInstance().getService().getListHAD();
        call.enqueue(new ListHADCallback());
    }

    private void getOnClickSpinnerDrugRoute() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (selectedItem.equals("ทั้งหมด") & dao != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + dao.getListDrugCardDao().size() + ")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll(), patientAdmin.getStatus(), listHAD);
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                } else if (selectedItem.equals("กิน") & dao != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + buildDrugCardListManager.getDaoPO().getListDrugCardDao().size() + ")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoPO(), patientAdmin.getStatus(), listHAD);
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                } else if (selectedItem.equals("ฉีด") & dao != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + buildDrugCardListManager.getDaoIV().getListDrugCardDao().size() + ")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoIV(), patientAdmin.getStatus(), listHAD);
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                } else if (selectedItem.equals("อื่นๆ") & dao != null) {
                    tvDate.setText(dateFortvDate + " (จำนวนยา " + buildDrugCardListManager.getDaoOTHER().getListDrugCardDao().size() + ")");
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoOTHER(), patientAdmin.getStatus(), listHAD);
                    listView.setAdapter(buildAdministrationForPatientAdapter);
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
                    for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
                        d.setComplete("1");
                        d.setCheckNote("0");
                        d.setStatus("normal");
                        d.setDescriptionTemplate("");
                        d.setDescription("");
                        d.setIdRadio(R.id.rdb1);
                        d.setStrRadio("");
                        d.setCheckType("Administration");
                    }
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll(), patientAdmin.getStatus(), listHAD);
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                } else if (selectedItem.equals("เพิ่มบันทึกข้อความ NPO")) {
                    for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
                        d.setComplete("0");
                        d.setCheckNote("1");
                        d.setStatus("normal");
                        d.setDescriptionTemplate("");
                        d.setDescription("");
                        d.setIdRadio(R.id.rdb2);
                        d.setStrRadio("NPO");
                        d.setCheckType("Administration");
                    }
                    buildAdministrationForPatientAdapter.setDao(getContext(), buildDrugCardListManager.getDaoAll(), patientAdmin.getStatus(), listHAD);
                    listView.setAdapter(buildAdministrationForPatientAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_set_time, null);
        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup);
        tvCurrentTime = (TextView) dialogView.findViewById(R.id.tvCurrentTime);
        txtStatus = (EditText) dialogView.findViewById(R.id.txtStatus);

        tvCurrentTime.setText(tvcurrentTime + ":00");
        useTime = tvcurrentTime;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) dialogView.findViewById(selectedId);
                if (radioButton.getText().equals("บริหารยา ณ เวลานี้")) {
                    useTime = tvcurrentTime + ":00";
                } else if (radioButton.getText().equals("ระบุเวลาในการบริหารยา")) {
                    final View dialogViewClock = View.inflate(getActivity(), R.layout.custom_dialog_set_clock, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                    dialogViewClock.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePicker datePicker = (DatePicker) dialogViewClock.findViewById(R.id.date_picker);
                            TimePicker timePicker = (TimePicker) dialogViewClock.findViewById(R.id.time_picker);
                            Date date;
                            SimpleDateFormat sdfForActualAdmin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            int pickYear, pickMonth, pickDay, pickHour = 0, pickMinute = 0;
                            Calendar calendar = null;
                            if (android.os.Build.VERSION.SDK_INT >= 23) {
                                calendar = new GregorianCalendar(
                                        pickYear = datePicker.getYear(),
                                        pickMonth = datePicker.getMonth(),
                                        pickDay = datePicker.getDayOfMonth(),
                                        pickHour = timePicker.getHour(),
                                        pickMinute = timePicker.getMinute());
                                useTime = String.format("%02d", pickHour) + ":" + String.format("%02d", pickMinute) + ":00"; //*****used lpad
                                try {
                                    if (timeposition <= 23) {
                                        date = sdfForActualAdmin.parse(toDayDate + " " + useTime);
                                        dateActualAdmin = sdfForActualAdmin.format(date);
                                        tvCurrentTime.setText(useTime);
                                    } else {
                                        date = sdfForActualAdmin.parse(tomorrowDate + " " + useTime);
                                        dateActualAdmin = sdfForActualAdmin.format(date);
                                        tvCurrentTime.setText(useTime);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                calendar = new GregorianCalendar(
                                        pickYear = datePicker.getYear(),
                                        pickMonth = datePicker.getMonth(),
                                        pickDay = datePicker.getDayOfMonth(),
                                        pickHour = timePicker.getCurrentHour(),
                                        pickMinute = timePicker.getCurrentMinute());
                                useTime = String.format("%02d", pickHour) + ":" + String.format("%02d", pickMinute) + ":00"; //*****used lpad
                                try {
                                    if (timeposition <= 23) {
                                        date = sdfForActualAdmin.parse(toDayDate + " " + useTime);
                                        dateActualAdmin = sdfForActualAdmin.format(date);
                                        tvCurrentTime.setText(useTime);
                                    } else {
                                        date = sdfForActualAdmin.parse(tomorrowDate + " " + useTime);
                                        dateActualAdmin = sdfForActualAdmin.format(date);
                                        tvCurrentTime.setText(useTime);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            Calendar timePick = Calendar.getInstance();
                            timePick.set(pickYear, pickMonth, pickDay, pickHour, pickMinute);
                            startMillis = timePick.getTimeInMillis();
                            endMillis = startMillis + 60 * 60 * 1000;
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(dialogViewClock);
                    alertDialog.show();
                }
            }
        });

        builder.setView(dialogView);
        builder.setTitle("กรุณาระบุเหตุผล");
        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (txtStatus.getText().toString().equals("")) {
                    final AlertDialog.Builder builderStatus = new AlertDialog.Builder(getContext());
                    builderStatus.setTitle("กรุณาระบุเหตุผล");
                    builderStatus.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getTimeDialog();
                        }
                    });
                    builderStatus.create();
                    builderStatus.show().getWindow().setLayout(1000, 250);
                } else {
                    txtstatus = txtStatus.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("คุณต้องการจะบันทึกข้อมูลใช่หรือไม่?");
                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveAdministration();
                        }
                    });
                    builder.setNegativeButton("ไม่ใช่", null);
                    builder.create();
                    builder.show();
                }
            }
        });
        builder.setNegativeButton("ยกเลิก", null);
        builder.create();
        builder.show().getWindow().setLayout(1000, 850);
    }

    public void saveAdministration() {
        Boolean checkNull = true;
        for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
            Log.d("check", "getComplete = " + d.getComplete() + "    getchecknote = " + d.getCheckNote());
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
//                    if (d.getStrRadio().equals("NPO") | d.getStrRadio().equals("ไม่มียา") | d.getStrRadio().equals("คนไข้ปฏิเสธ") |
//                            d.getStrRadio().equals("คนไข้มีอาการคลื่นไส้อาเจียน") | d.getStrRadio().equals("แทงเส้นเลือดไม่ได้") | d.getStrRadio().equals("แพทย์สั่ง off ณ เวลานั้น") |
//                            d.getStrRadio().equals("ผู้ป่วยไปทำหัตการ"))
                    if (!d.getStrRadio().equals(""))
                        d.setCheckNote("1");
                    else
                        d.setCheckNote("0");
                } else
                    d.setCheckNote("0");
            } else if (d.getComplete() == null & d.getCheckNote().equals("0")) {
                d.setComplete("1");
                d.setCheckNote("0");
            }

            if (d.getComplete().equals("0") & d.getCheckNote().equals("0"))
                checkNull = false;
        }
        if (checkNull) {
            for (DrugCardDao d : buildDrugCardListManager.getDaoAll().getListDrugCardDao()) {
                if (d.getStrBP() != null & d.getStrHR() != null & d.getStrCBG() != null) {
                    d.setDescriptionTemplate("Hold:BP " + d.getStrBP() + "|Heart Rate " + d.getStrHR() + "|CBG " + d.getStrCBG());
                } else if (d.getStrBP() == null & d.getStrHR() == null & d.getStrCBG() != null) {
                    d.setDescriptionTemplate("Hold:CBG " + d.getStrCBG());
                } else if (d.getStrBP() == null & d.getStrHR() != null & d.getStrCBG() != null) {
                    d.setDescriptionTemplate("Hold:Heart Rate " + d.getStrHR() + "|CBG " + d.getStrCBG());
                } else if (d.getStrBP() == null & d.getStrHR() != null & d.getStrCBG() == null) {
                    d.setDescriptionTemplate("Hold:Heart Rate " + d.getStrHR());
                } else if (d.getStrBP() != null & d.getStrHR() == null & d.getStrCBG() == null) {
                    d.setDescriptionTemplate("Hold:BP " + d.getStrBP());
                } else if (d.getStrBP() != null & d.getStrHR() != null & d.getStrCBG() == null) {
                    d.setDescriptionTemplate("Hold:BP " + d.getStrBP() + "|Heart Rate " + d.getStrHR());
                } else if (d.getStrBP() != null & d.getStrHR() == null & d.getStrCBG() != null) {
                    d.setDescriptionTemplate("Hold:BP " + d.getStrBP() + "|CBG " + d.getStrCBG());
                } else if (d.getStrBP() == null & d.getStrHR() == null & d.getStrCBG() == null) {
                    d.setDescriptionTemplate("");
                }
                d.setRFID(RFID);
                d.setFirstName(firstName);
                d.setLastName(lastName);
                d.setWardName(wardName);
                d.setActualAdmin(dateActualAdmin);
                d.setActivityHour(time);
                if (d.getDescription() != null) {
                    d.setComplete("0");
                    if (!d.getDescription().equals("") & txtstatus != null) {
                        d.setDescription(d.getDescription() + " " + txtstatus);
                    } else {
                        if (txtstatus != null) {
                            d.setDescription(txtstatus);
                        }
                    }
                }
            }

            updateDrugData(buildDrugCardListManager.getDaoAll());
            tricker = "save";
            Toast.makeText(getContext(), "บันทึกเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), AdministrationActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("wardId", wardID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("position", timeposition);
            intent.putExtra("time", time);
            intent.putExtra("save", tricker);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else
            Toast.makeText(getContext(), "กรุณาเขียนคำอธิบายสำหรับทุกๆ ตัวยาที่ไม่ได้ใส่เครื่องหมายถูก", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(getContext(), AdministrationActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("wardId", wardID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("position", timeposition);
                    intent.putExtra("time", time);
                    intent.putExtra("save", tricker);
                    getActivity().startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            if (sumTime == 1 || sumTime == -1 || sumTime == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("คุณต้องการจะบันทึกข้อมูลใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveAdministration();
                    }
                });
                builder.setNegativeButton("ไม่ใช่", null);
                builder.create();
                builder.show();
            } else {
                getTimeDialog();
            }
        } else if (view.getId() == R.id.tvHistory) {
            Intent intent = new Intent(getContext(), HistoryAdministrationActivity.class);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardId", wardID);
            intent.putExtra("wardname", wardName);
            intent.putExtra("RFID", RFID);
            intent.putExtra("firstname", firstName);
            intent.putExtra("lastname", lastName);
            intent.putExtra("timeposition", timeposition);
            intent.putExtra("position", position);
            intent.putExtra("patientAdmin", patientAdmin);
            intent.putExtra("time", time);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if (view.getId() == R.id.btnCancel) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("คุณต้องการยกเลิกใช่หรือไม่?");
            builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getContext(), AdministrationActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("wardId", wardID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("position", timeposition);
                    intent.putExtra("time", time);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("ไม่ใช่", null);
            builder.create();
            builder.show();
        }
    }


    class ListHADCallback implements Callback<List<String>> {

        @Override
        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
            listHAD = response.body();
        }

        @Override
        public void onFailure(Call<List<String>> call, Throwable t) {
        }
    }


    class DrugLoadCallback implements Callback<ListDrugCardDao> {

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
            for (DrugCardDao d : dao.getListDrugCardDao()) {
                if (d.getCheckType().equals("Second Check")) {
                    if (d.getComplete().equals("1") | d.getComplete().equals("0")) {
                        d.setComplete(null);
                        d.setDescriptionTemplate(null);
                        d.setDescription(null);
                        d.setStrRadio(null);
                    } else {
                        d.setComplete(null);
                        d.setDescriptionTemplate(null);
                        d.setDescription(null);
                        d.setStrRadio(null);
                    }
                } else {
                    if (d.getDescriptionTemplate() != null) {
                        String[] strRadio = d.getDescriptionTemplate().split(",");
                        if (strRadio.length == 1) {
                            String[] strHold1 = strRadio[0].split(":");
                            if (strHold1.length > 1) {
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
                        } else if (strRadio.length == 2) {
                            if (strRadio[0].equals("")) {
                                d.setStrRadio(strRadio[1]);
                            } else {
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
                if (d.getComplete() == null) {
                    checkNote = true;
                } else if (d.getComplete() != null) {
                    if (d.getComplete().equals("0")) {
                        checkNote = true;
                    }
                }
            }
            buildDrugCardListManager.setDao(dao);
            tvDate.setText(dateFortvDate + " (จำนวนยา " + dao.getListDrugCardDao().size() + ")");
            if (checkNote) {
                btnSave.setVisibility(getView().VISIBLE);
                btnCancel.setVisibility(getView().VISIBLE);
            } else {
                btnSave.setVisibility(getView().INVISIBLE);
                btnCancel.setVisibility(getView().INVISIBLE);
            }
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
        }
    }


    class SaveDrugDataCallback implements Callback<ListDrugCardDao> {

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            dao = response.body();
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
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
            if (patientAdmin != null) {
                mrn = patientAdmin.getMRN();
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

//                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
//                Log.w("AndroidParseXMLActivity", e);
            }

            return (ArrayList<DrugAdrDao>) itemsList;
        }
    }

}
