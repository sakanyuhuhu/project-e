package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.PreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.DrugForPatientInTime;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.DrugForPatientInTime2;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.ListDrugForPatientInTime;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.ListDrugForPatientInTime2;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.ListMedicalCardCollectionDao;
import th.ac.mahidol.rama.emam.dao.patientinfoforperson.PatientInfoForPersonCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

public class PreparationForPatientFragment extends Fragment {
    private String nfcUId, sdlocId, gettimer, patientName, bedNo, mRN, strtimer, strdf2, dateToday, formatedDate, stryear;
    private TextView tvTime, tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private int tricker, yearstr;
    private TextView tvDrugAllergy, tvDate;
    private Button btnCancel, btnSave;
    private Spinner spinner1;
    private ListMedicalCardCollectionDao listMedicalCardCollectionDao;
    private SQLiteManager dbHelper;
    private PreparationForPatientAdapter preparationForPatientAdapter;
    private List<String> listDrugName, listDosage, unit, listFrequency, type, route, adminTime, site;
    private List<String> listDrugName2, listDosage2, unit2, listFrequency2, type2, route2, adminTime2, site2;
    private List<String> booleanListCheckHold, booleanListcheck, listvalueCheckNoteRadio, listStatusHold, listStatus;
    private List<String> listdrugName, listdosage, listunit, listroute;
    private List<Integer> noteTricker;

    public PreparationForPatientFragment() {
        super();
    }

    public static PreparationForPatientFragment newInstance(String gettimer, String nfcUId, String sdlocId, String patientName, String bedNo, String mRN, String strdf2, int tricker) {
        PreparationForPatientFragment fragment = new PreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("timer", gettimer);
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("patientName", patientName);
        args.putString("bedNo", bedNo);
        args.putString("mRN", mRN);
        args.putString("strdf", strdf2);
        args.putInt("tricker", tricker);
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

    private void init(Bundle savedInstanceState) {
//        16-08-59
        booleanListCheckHold = new ArrayList<String>();
        booleanListcheck = new ArrayList<String>();
        listvalueCheckNoteRadio = new ArrayList<String>();
        listStatusHold = new ArrayList<String>();
        listStatus = new ArrayList<String>();
        listdrugName = new ArrayList<String>();
        listdosage = new ArrayList<String>();
        listunit = new ArrayList<String>();
        listroute = new ArrayList<String>();
        noteTricker = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        gettimer = getArguments().getString("timer");
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        patientName = getArguments().getString("patientName");
        bedNo = getArguments().getString("bedNo");
        mRN = getArguments().getString("mRN");
        strdf2 = getArguments().getString("strdf");
        tricker = getArguments().getInt("tricker");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId + " / patientName : " + patientName + " / bedNo : " + bedNo + " / mRN : " + mRN+" / strdf2 : "+strdf2+" / tricker : "+tricker);
        if(gettimer.substring(0,1).equals("0")){
            strtimer = gettimer.substring(1,2);
        }
        else{
            strtimer = gettimer.substring(0,2);
        }
        Date today = new Date();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateToday = sDateFormat.format(today);
        stryear = dateToday.substring(0,4);
        yearstr = Integer.parseInt(stryear);

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvBedNo = (TextView) rootView.findViewById(R.id.tvBedNo);
        tvPatientName = (TextView) rootView.findViewById(R.id.tvPatientName);
        tvPatientID = (TextView) rootView.findViewById(R.id.tvPatientID);
        tvHN = (TextView) rootView.findViewById(R.id.tvMrn);
        tvBirth = (TextView) rootView.findViewById(R.id.tvBirth);
        tvAge = (TextView) rootView.findViewById(R.id.tvAge);
        tvSex = (TextView) rootView.findViewById(R.id.tvSex);
        tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
        tvDrugAllergy = (TextView) rootView.findViewById(R.id.tvDrugAllergy);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);

        Call<PatientInfoForPersonCollectionDao> callInfo = HttpManager.getInstance().getService().loadListPatientInfo(mRN);
        callInfo.enqueue(new Callback<PatientInfoForPersonCollectionDao>() {
            @Override
            public void onResponse(Call<PatientInfoForPersonCollectionDao> call, Response<PatientInfoForPersonCollectionDao> response) {
                String sex, strAge, stryear;
                int year, age = 0;
                if(response.body().getListPatientInfoBean().getGender().equals("M"))
                    sex = "ชาย";
                else
                    sex = "หญิง";
                strAge = response.body().getListPatientInfoBean().getDob().trim();
                if(strAge.equals("")){
                    stryear = strAge;
                }
                else{
                    stryear = strAge.substring(6);
                    year = Integer.parseInt(stryear);
                    age = yearstr - year;
                }

                tvBedNo.setText("เลขที่เตียง/ห้อง: " + bedNo);
                tvPatientName.setText(patientName);
                tvPatientID.setText(response.body().getListPatientInfoBean().getIdCardNo());
                tvHN.setText("HN:" + mRN);
                tvSex.setText("เพศ:"+sex);
                tvBirth.setText("วันเกิด:"+response.body().getListPatientInfoBean().getDob().trim());
                if(stryear.equals("")){
                    tvAge.setText("อายุ:"+stryear+"ปี,"+" เดือน,"+" วัน");
                }else{
                    Log.d("check", "AGE : "+String.valueOf(age));
                    tvAge.setText("อายุ:"+String.valueOf(age)+"ปี,"+" เดือน,"+" วัน");
                }
                tvStatus.setText("สถานะภาพ:"+response.body().getListPatientInfoBean().getMaritalstatus());
            }

            @Override
            public void onFailure(Call<PatientInfoForPersonCollectionDao> call, Throwable t) {
                Log.d("check", "PreparationForPatientFragment callInfo Failure " + t);
            }
        });
        tvTime.setText(gettimer);
        tvDrugAllergy.setText("   การแพ้ยา: ");

        Call<ListMedicalCardCollectionDao> callDrug = HttpManager.getInstance().getService().loadListMedicalCard(mRN);
        callDrug.enqueue(new Callback<ListMedicalCardCollectionDao>() {
            @Override
            public void onResponse(Call<ListMedicalCardCollectionDao> call, Response<ListMedicalCardCollectionDao> response) {
                listMedicalCardCollectionDao = response.body();
                Log.d("check", "SIZE listMedical " + listMedicalCardCollectionDao.getListMedicalCardBean().size());
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                listDrugName = new ArrayList<String>();
                listDosage = new ArrayList<String>();
                unit = new ArrayList<String>();
                listFrequency = new ArrayList<String>();
                type = new ArrayList<String>();
                route = new ArrayList<String>();
                adminTime = new ArrayList<String>();
                site = new ArrayList<String>();
                listDrugName2 = new ArrayList<String>();
                listDosage2 = new ArrayList<String>();
                unit2 = new ArrayList<String>();
                listFrequency2 = new ArrayList<String>();
                type2 = new ArrayList<String>();
                route2 = new ArrayList<String>();
                adminTime2 = new ArrayList<String>();
                site2 = new ArrayList<String>();

                DrugForPatientInTime drugForPatientInTime;
                DrugForPatientInTime2 drugForPatientInTime2;
                ListDrugForPatientInTime listDrugForPatientInTime = null;
                ListDrugForPatientInTime2 listDrugForPatientInTime2 = null;
                for (int i = 0; i < listMedicalCardCollectionDao.getListMedicalCardBean().size(); i++) {
                    formatedDate = sDateFormat.format(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDrugUseDate());
                    if(strtimer.equals(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour())&& formatedDate.equals(strdf2)) {
//                        Log.d("check","1/formatedDate : " + formatedDate);
                        drugForPatientInTime = new DrugForPatientInTime();
                        listDrugForPatientInTime = new ListDrugForPatientInTime();
                        drugForPatientInTime.setDrugName(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName());
                        drugForPatientInTime.setDosage(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDose());
                        drugForPatientInTime.setUnit(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getUnit());
                        drugForPatientInTime.setFrequency(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getFrequency());
                        drugForPatientInTime.setType(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminType());
                        drugForPatientInTime.setRoute(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getRoute());
                        drugForPatientInTime.setAdminTime(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTime());
                        drugForPatientInTime.setSite(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getSite());
                        listDrugForPatientInTime.setDrugForPatientInTimeList((List<DrugForPatientInTime>) drugForPatientInTime);
                    }
                    else if(strtimer.equals(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour()) && (!(formatedDate.equals(strdf2)))){
//                        Log.d("check","2/formatedDate : " + formatedDate +" strdf2: "+strdf2);
                        drugForPatientInTime2 = new DrugForPatientInTime2();
                        listDrugForPatientInTime2 = new ListDrugForPatientInTime2();
                        drugForPatientInTime2.setDrugName(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName());
                        drugForPatientInTime2.setDosage(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDose());
                        drugForPatientInTime2.setUnit(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getUnit());
                        drugForPatientInTime2.setFrequency(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getFrequency());
                        drugForPatientInTime2.setType(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminType());
                        drugForPatientInTime2.setRoute(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getRoute());
                        drugForPatientInTime2.setAdminTime(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTime());
                        drugForPatientInTime2.setSite(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getSite());
                        listDrugForPatientInTime2.setDrugForPatientInTimeList2((List<DrugForPatientInTime2>) drugForPatientInTime2);
                    }
                }

                saveCache(listDrugForPatientInTime);
                saveCache2(listDrugForPatientInTime2);

                if(tricker == 1) {
                    Log.d("check","listDrugName : " + listDrugName.size());
                    tvDate.setText("  " + dateToday + " (จำนวนยา " + listDrugName.size() + ")");
                    ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                    preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName, listDosage, type, route, listFrequency, unit, adminTime, site);
                    listView.setAdapter(preparationForPatientAdapter);

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            if(selectedItem.equals("ทั้งหมด")) {

                            }
                            else if(selectedItem.equals("กิน")) {

                            }
                            else if(selectedItem.equals("ฉีด")) {

                            }
                            else{

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Log.d("check", "onNothingSelectedToday : "+adapterView);
                        }
                    });

                }
                else {
                    Log.d("check","listDrugName2 : " + listDrugName2.size());
                    tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2.size() + ")");
                    ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                    preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2, listDosage2, type2, route2, listFrequency2, unit2, adminTime2, site2);
                    listView.setAdapter(preparationForPatientAdapter);

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            if(selectedItem.equals("ทั้งหมด")) {

                            }
                            else if(selectedItem.equals("กิน")) {

                            }
                            else if(selectedItem.equals("ฉีด")) {

                            }
                            else{

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Log.d("check", "onNothingSelectedNext : "+adapterView);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ListMedicalCardCollectionDao> call, Throwable t) {
                Log.d("check", "PreparationForPatientFragment callDrug Failure " + t);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 booleanListCheckHold = new ArrayList<String>();
//                 booleanListcheck = new ArrayList<String>();//ถ้าย้ายไป new ใน init แล้ว ได้ค่ากลับมาเป็น false ทั้งหมดเลย
//                 listvalueCheckNoteRadio = new ArrayList<String>();
//                 listStatusHold = new ArrayList<String>();
//                 listStatus = new ArrayList<String>();
//15-08-16
//                 listdrugName = new ArrayList<String>();
//                 listdosage = new ArrayList<String>();
//                 listunit = new ArrayList<String>();
//                 listroute = new ArrayList<String>();

                for(int i=0; i<preparationForPatientAdapter.getIsCheckStatus().size();i++){
                    booleanListCheckHold.add(preparationForPatientAdapter.getIsCheckHold().get(i).toString());
                    listStatusHold.add(preparationForPatientAdapter.getStrStatusHold().get(i).toString());
                    booleanListcheck.add(preparationForPatientAdapter.getIsCheckStatus().get(i).toString());
                    listvalueCheckNoteRadio.add(preparationForPatientAdapter.getValueCheckNoteRadio().get(i).toString());
                    listStatus.add(preparationForPatientAdapter.getStrStatus().get(i).toString());
                    noteTricker.add((Integer) preparationForPatientAdapter.getNoteTricker().get(i));
                    Log.d("check","booleanListcheck        : "+booleanListcheck.get(i));
                    Log.d("check","booleanListCheckHold    : "+booleanListCheckHold.get(i));
                    Log.d("check","listStatusHold          : "+listStatusHold.get(i));
                    Log.d("check","listvalueCheckNoteRadio : "+ listvalueCheckNoteRadio.get(i));
                    Log.d("check","listStatus              : "+ listStatus.get(i));
//15-08-16
                    listdrugName.add(preparationForPatientAdapter.getDrugName().get(i).toString());
                    listdosage.add(preparationForPatientAdapter.getDosage().get(i).toString());
                    listunit.add(preparationForPatientAdapter.getUnit().get(i).toString());
                    listroute.add(preparationForPatientAdapter.getRoute().get(i).toString());
                    Log.d("check","listdrugName : "+listdrugName.get(i));
                    Log.d("check","listdosage   : "+listdosage.get(i));
                    Log.d("check","listunit     : "+listunit.get(i));
                    Log.d("check","listroute    : "+ listroute.get(i));
                    Log.d("check","gettimer     : "+gettimer);
                    Log.d("check","noteTricker  : "+ noteTricker.get(i));
                }

//16-08-59 ลอง save ลง db
                for(int i=0; i<preparationForPatientAdapter.getIsCheckStatus().size();i++){
                    if(booleanListcheck.get(i).trim().equals("false") && noteTricker.get(i) == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("กรุณาเขียนคำอธิบายสำหรับทุกๆตัวยาที่ไม่ได้ใส่เครื่องหมายถูก");
                        builder.setPositiveButton("ตกลง", null);
                        builder.create();
                        builder.show();
                    }
                }

//                dbHelper = new SQLiteManager(getContext());
//                if(tricker == 1) {
//                    for (int i=0; i<listDrugName.size();i++){
//                        dbHelper.addPrepareForPatient(mRN, strtimer, listDrugName.get(i), listDosage.get(i), unit.get(i), type.get(i), route.get(i), listFrequency.get(i), adminTime.get(i));
//                    }
//                    dbHelper.deletePrepareForPatient("1");
//                    dbHelper.getPrepareForPatient(mRN);
//                }
//                else {
//                    for (int i=0; i<listDrugName2.size();i++){
//                        dbHelper.addPrepareForPatient(mRN, strtimer, listDrugName2.get(i), listDosage2.get(i), unit2.get(i), type2.get(i), route2.get(i), listFrequency2.get(i), adminTime2.get(i));
//                    }
//                    dbHelper.deletePrepareForPatient("1");
//                    dbHelper.getPrepareForPatient(mRN);
//                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void saveCache(ListDrugForPatientInTime listDrugForPatientInTime){
        String json = new Gson().toJson(listDrugForPatientInTime);
        SharedPreferences prefs = getContext().getSharedPreferences("drugforpatientintime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("drugforpatientintime",json);
        editor.apply();
    }

    private void saveCache2(ListDrugForPatientInTime2 listDrugForPatientInTime2){
        String json = new Gson().toJson(listDrugForPatientInTime2);
        SharedPreferences prefs = getContext().getSharedPreferences("drugforpatientintime2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("drugforpatientintime2",json);
        editor.apply();
    }

}
