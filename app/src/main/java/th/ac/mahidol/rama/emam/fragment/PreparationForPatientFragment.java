package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.PreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.ListMedicalCardCollectionDao;
import th.ac.mahidol.rama.emam.dao.patientinfoforperson.PatientInfoForPersonCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

public class PreparationForPatientFragment extends Fragment {
    private String nfcUId, sdlocId, gettimer, patientName, bedNo, mRN, strtimer, strdf2, dateToday, formatedDate, stryear;
    private TextView tvTimer, tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private int tricker, yearstr;
    private TextView tvDrugAllergy, tvDate;
    private Button btnCancel, btnSave;
    private Spinner spinner1;
    private ListMedicalCardCollectionDao listMedicalCardCollectionDao;
    private SQLiteManager dbHelper;
    private  PreparationForPatientAdapter preparationForPatientAdapter;
    private List<String> booleanListcheck;
    private List<String> listDrugName, listDosage, unit, listFrequency, type, route, adminTime, site;
    private List<String> listDrugName2, listDosage2, unit2, listFrequency2, type2, route2, adminTime2, site2;
    private List<String> listDrugNamePO, listDosagePO, unitPO, listFrequencyPO, typePO, routePO, adminTimePO, sitePO;
    private List<String> listDrugNameIV, listDosageIV, unitIV, listFrequencyIV, typeIV, routeIV, adminTimeIV, siteIV;
    private List<String> listDrugNameOther, listDosageOther, unitOther, listFrequencyOther, typeOther, routeOther, adminTimeOther, siteOther;
    private List<String> listDrugName2PO, listDosage2PO, unit2PO, listFrequency2PO, type2PO, route2PO, adminTime2PO, site2PO;
    private List<String> listDrugName2IV, listDosage2IV, unit2IV, listFrequency2IV, type2IV, route2IV, adminTime2IV, site2IV;
    private List<String> listDrugName2Other, listDosage2Other, unit2Other, listFrequency2Other, type2Other, route2Other, adminTime2Other, site2Other;

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
        booleanListcheck = new ArrayList<String>();
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

        tvTimer = (TextView) rootView.findViewById(R.id.tvTimer);
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
        tvTimer.setText(gettimer);
        tvDrugAllergy.setText("  การแพ้ยา: ");

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
                for (int i = 0; i < listMedicalCardCollectionDao.getListMedicalCardBean().size(); i++) {
                    formatedDate = sDateFormat.format(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDrugUseDate());
                    if(strtimer.equals(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour())&& formatedDate.equals(strdf2)) {
//                        Log.d("check","1/formatedDate : " + formatedDate);
                        listDrugName.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName());
                        listDosage.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDose());
                        unit.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getUnit());
                        listFrequency.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getFrequency());
                        type.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminType());
                        route.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getRoute());
                        adminTime.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTime());
                        site.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getSite());
                    }
                    else if(strtimer.equals(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour()) && (!(formatedDate.equals(strdf2)))){
//                        Log.d("check","2/formatedDate : " + formatedDate +" strdf2: "+strdf2);
                        listDrugName2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName());
                        listDosage2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDose());
                        unit2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getUnit());
                        listFrequency2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getFrequency());
                        type2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminType());
                        route2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getRoute());
                        adminTime2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTime());
                        site2.add(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getSite());
                    }
                }

                if(tricker == 1) {
                    Log.d("check","listDrugName : " + listDrugName.size());
//  edit below 11/08/2559 date today ยากิน
                    listDrugNamePO = new ArrayList<String>();
                    listDosagePO = new ArrayList<String>();
                    unitPO = new ArrayList<String>();
                    listFrequencyPO = new ArrayList<String>();
                    typePO = new ArrayList<String>();
                    routePO = new ArrayList<String>();
                    adminTimePO = new ArrayList<String>();
                    sitePO = new ArrayList<String>();
//  ยาฉีด
                    listDrugNameIV = new ArrayList<String>();
                    listDosageIV = new ArrayList<String>();
                    unitIV = new ArrayList<String>();
                    listFrequencyIV = new ArrayList<String>();
                    typeIV = new ArrayList<String>();
                    routeIV = new ArrayList<String>();
                    adminTimeIV = new ArrayList<String>();
                    siteIV = new ArrayList<String>();
//  ยาอื่นๆ
                    listDrugNameOther = new ArrayList<String>();
                    listDosageOther = new ArrayList<String>();
                    unitOther = new ArrayList<String>();
                    listFrequencyOther = new ArrayList<String>();
                    typeOther = new ArrayList<String>();
                    routeOther = new ArrayList<String>();
                    adminTimeOther = new ArrayList<String>();
                    siteOther = new ArrayList<String>();
                    for(int i=0; i<route.size();i++) {
                        if (route.get(i).equals("PO")) {
                            listDrugNamePO.add(listDrugName.get(i));
                            listDosagePO.add(listDosage.get(i));
                            unitPO.add(unit.get(i));
                            listFrequencyPO.add(listFrequency.get(i));
                            typePO.add(type.get(i));
                            routePO.add(route.get(i));
                            adminTimePO.add(adminTime.get(i));
                            sitePO.add(site.get(i));
                        }
                        else if (route.get(i).equals("IV")) {
                            listDrugNameIV.add(listDrugName.get(i));
                            listDosageIV.add(listDosage.get(i));
                            unitIV.add(unit.get(i));
                            listFrequencyIV.add(listFrequency.get(i));
                            typeIV.add(type.get(i));
                            routeIV.add(route.get(i));
                            adminTimeIV.add(adminTime.get(i));
                            siteIV.add(site.get(i));

                        }
                        else {
                            listDrugNameOther.add(listDrugName.get(i));
                            listDosageOther.add(listDosage.get(i));
                            unitOther.add(unit.get(i));
                            listFrequencyOther.add(listFrequency.get(i));
                            typeOther.add(type.get(i));
                            routeOther.add(route.get(i));
                            adminTimeOther.add(adminTime.get(i));
                            siteOther.add(site.get(i));
                        }
                    }
                    tvDate.setText(dateToday + " (จำนวนยา " + listDrugName.size() + ")");
                    ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                    preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName, listDosage, type, route, listFrequency, unit, adminTime, site);
                    listView.setAdapter(preparationForPatientAdapter);

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            if(selectedItem.equals("ทั้งหมด")) {
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugName.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName, listDosage, type, route, listFrequency, unit, adminTime, site);
                                listView.setAdapter(preparationForPatientAdapter);
                            }
                            else if(selectedItem.equals("กิน")) {
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugNamePO.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugNamePO, listDosagePO, typePO, routePO, listFrequencyPO, unitPO, adminTimePO, sitePO);
                                listView.setAdapter(preparationForPatientAdapter);
                            }
                            else if(selectedItem.equals("ฉีด")) {
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugNameIV.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugNameIV, listDosageIV, typeIV, routeIV, listFrequencyIV, unitIV, adminTimeIV, siteIV);
                                listView.setAdapter(preparationForPatientAdapter);
                            }
                            else{
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugNameOther.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugNameOther, listDosageOther, typeOther, routeOther, listFrequencyOther, unitOther, adminTimeOther, siteOther);
                                listView.setAdapter(preparationForPatientAdapter);

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
//  date not today ยากิน
                    listDrugName2PO = new ArrayList<String>();
                    listDosage2PO = new ArrayList<String>();
                    unit2PO = new ArrayList<String>();
                    listFrequency2PO = new ArrayList<String>();
                    type2PO = new ArrayList<String>();
                    route2PO = new ArrayList<String>();
                    adminTime2PO = new ArrayList<String>();
                    site2PO = new ArrayList<String>();
//  ยาฉีด
                    listDrugName2IV = new ArrayList<String>();
                    listDosage2IV = new ArrayList<String>();
                    unit2IV = new ArrayList<String>();
                    listFrequency2IV = new ArrayList<String>();
                    type2IV = new ArrayList<String>();
                    route2IV = new ArrayList<String>();
                    adminTime2IV = new ArrayList<String>();
                    site2IV = new ArrayList<String>();
//  ยาอื่นๆ
                    listDrugName2Other = new ArrayList<String>();
                    listDosage2Other = new ArrayList<String>();
                    unit2Other = new ArrayList<String>();
                    listFrequency2Other = new ArrayList<String>();
                    type2Other = new ArrayList<String>();
                    route2Other = new ArrayList<String>();
                    adminTime2Other = new ArrayList<String>();
                    site2Other = new ArrayList<String>();
                    for(int i=0; i<route.size();i++) {
                        if (route.get(i).equals("PO")) {
                            listDrugName2PO.add(listDrugName.get(i));
                            listDosage2PO.add(listDosage.get(i));
                            unit2PO.add(unit.get(i));
                            listFrequency2PO.add(listFrequency.get(i));
                            type2PO.add(type.get(i));
                            route2PO.add(route.get(i));
                            adminTime2PO.add(adminTime.get(i));
                            site2PO.add(site.get(i));
                        }
                        else if (route.get(i).equals("IV")) {
                            listDrugName2IV.add(listDrugName.get(i));
                            listDosage2IV.add(listDosage.get(i));
                            unit2IV.add(unit.get(i));
                            listFrequency2IV.add(listFrequency.get(i));
                            type2IV.add(type.get(i));
                            route2IV.add(route.get(i));
                            adminTime2IV.add(adminTime.get(i));
                            site2IV.add(site.get(i));

                        }
                        else {
                            listDrugName2Other.add(listDrugName.get(i));
                            listDosage2Other.add(listDosage.get(i));
                            unit2Other.add(unit.get(i));
                            listFrequency2Other.add(listFrequency.get(i));
                            type2Other.add(type.get(i));
                            route2Other.add(route.get(i));
                            adminTime2Other.add(adminTime.get(i));
                            site2Other.add(site.get(i));
                        }
                    }
                    tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2.size() + ")");
                    ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                    preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2, listDosage2, type2, route2, listFrequency2, unit2, adminTime2, site2);
                    listView.setAdapter(preparationForPatientAdapter);

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            if(selectedItem.equals("ทั้งหมด")) {
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2, listDosage2, type2, route2, listFrequency2, unit2, adminTime2, site2);
                                listView.setAdapter(preparationForPatientAdapter);
                            }
                            else if(selectedItem.equals("กิน")) {
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2PO.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2PO, listDosage2PO, type2PO, route2PO, listFrequency2PO, unit2PO, adminTime2PO, site2PO);
                                listView.setAdapter(preparationForPatientAdapter);
                            }
                            else if(selectedItem.equals("ฉีด")) {
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2IV.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2IV, listDosage2IV, type2IV, route2IV, listFrequency2IV, unit2IV, adminTime2IV, site2IV);
                                listView.setAdapter(preparationForPatientAdapter);
                            }
                            else{
                                tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2Other.size() + ")");
                                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                                preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2Other, listDosage2Other, type2Other, route2Other, listFrequency2Other, unit2Other, adminTime2Other, site2Other);
                                listView.setAdapter(preparationForPatientAdapter);

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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> booleanListCheckHold = new ArrayList<String>();
                List<String> booleanListcheck = new ArrayList<String>();//ย้ายไป new ใน init แล้ว ได้ค่ากลับมาเป็น false ทั้งหมดเลย
                List<String> listvalueCheckNoteRadio = new ArrayList<String>();
                List<String> listStatusHold = new ArrayList<String>();
                List<String> listStatus = new ArrayList<String>();
                for(int i=0; i<preparationForPatientAdapter.getIsCheckStatus().size();i++){
                    booleanListCheckHold.add(preparationForPatientAdapter.getIsCheckHold().get(i).toString());
                    listStatusHold.add(preparationForPatientAdapter.getStrStatusHold().get(i).toString());
                    booleanListcheck.add(preparationForPatientAdapter.getIsCheckStatus().get(i).toString());
                    listvalueCheckNoteRadio.add(preparationForPatientAdapter.getValueCheckNoteRadio().get(i).toString());
                    listStatus.add(preparationForPatientAdapter.getStrStatus().get(i).toString());
                    Log.d("check","booleanListcheck        : "+booleanListcheck.get(i));
                    Log.d("check","booleanListCheckHold    : "+booleanListCheckHold.get(i));
                    Log.d("check","listStatusHold          : "+listStatusHold.get(i));
                    Log.d("check","listvalueCheckNoteRadio : "+ listvalueCheckNoteRadio.get(i));
                    Log.d("check","listStatus              : "+ listStatus.get(i));
                }

                dbHelper = new SQLiteManager(getContext());
                if(tricker == 1) {
//                    for (int i=0; i<listDrugName.size();i++){
//                        dbHelper.addPrepareForPatient(mRN, strtimer, listDrugName.get(i), listDosage.get(i), unit.get(i), type.get(i), route.get(i), listFrequency.get(i), adminTime.get(i));
//                    }
//                    dbHelper.deletePrepareForPatient("1");
//                    dbHelper.getPrepareForPatient(mRN);
                }
                else {
//                    for (int i=0; i<listDrugName2.size();i++){
//                        dbHelper.addPrepareForPatient(mRN, strtimer, listDrugName2.get(i), listDosage2.get(i), unit2.get(i), type2.get(i), route2.get(i), listFrequency2.get(i), adminTime2.get(i));
//                    }
//                    dbHelper.deletePrepareForPatient("1");
//                    dbHelper.getPrepareForPatient(mRN);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
