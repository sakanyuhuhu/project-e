package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
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
    private String nfcUId, sdlocId, gettimer, patientName, bedNo, mRN, strtimer, strdf2, formatedDate;
    private TextView tvTimer, tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private int tricker;
    private TextView tvDrugAllergy, tvDate;
    private ImageView ivNote;
    private CheckBox chkCheckMed;
    private Button btnCancel, btnSave;
    private ListMedicalCardCollectionDao listMedicalCardCollectionDao;
    private SQLiteManager dbHelper;
    private  PreparationForPatientAdapter preparationForPatientAdapter;

    List<String> listDrugName = new ArrayList<String>();
    List<String> listDosage = new ArrayList<String>();
    List<String> unit = new ArrayList<String>();
    List<String> listFrequency = new ArrayList<String>();
    List<String> type = new ArrayList<String>();
    List<String> route = new ArrayList<String>();
    List<String> adminTime = new ArrayList<String>();
    List<String> site = new ArrayList<String>();
    List<String> listDrugName2 = new ArrayList<String>();
    List<String> listDosage2 = new ArrayList<String>();
    List<String> unit2 = new ArrayList<String>();
    List<String> listFrequency2 = new ArrayList<String>();
    List<String> type2 = new ArrayList<String>();
    List<String> route2 = new ArrayList<String>();
    List<String> adminTime2 = new ArrayList<String>();
    List<String> site2 = new ArrayList<String>();

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
        final String dateToday = sDateFormat.format(today);
        String stryear = dateToday.substring(0,4);
        final int yearstr = Integer.parseInt(stryear);

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
        chkCheckMed = (CheckBox) rootView.findViewById(R.id.chkCheckMed);
        ivNote = (ImageView) rootView.findViewById(R.id.ivNote);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

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
//                DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date;

                Log.d("check", "strdf2: "+strdf2);
                for (int i = 0; i < listMedicalCardCollectionDao.getListMedicalCardBean().size(); i++) {
//                    Log.d("check", i+" DATE:  " +listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDrugUseDate());
//                    String dateStr = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDrugUseDate().toString();
//                    Log.d("check", i+" dateStr " +dateStr);
//                    date = (Date)formatter.parse(dateStr);
                    formatedDate = sDateFormat.format(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDrugUseDate());
//                    Log.d("check", i+" formatedDate " +formatedDate);

                    if(strtimer.equals(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour())&& formatedDate.equals(strdf2)) {
                        Log.d("check","1/formatedDate : " + formatedDate);
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
                        Log.d("check","2/formatedDate : " + formatedDate +" strdf2: "+strdf2);
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
                    tvDate.setText(dateToday + " (จำนวนยา " + listDrugName.size() + ")");
                    ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                    preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName, listDosage, type, route, listFrequency, unit, adminTime, site);
                    listView.setAdapter(preparationForPatientAdapter);
                }
                else {
                    Log.d("check","listDrugName2 : " + listDrugName2.size());
                    tvDate.setText(dateToday + " (จำนวนยา " + listDrugName2.size() + ")");
                    ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                    preparationForPatientAdapter = new PreparationForPatientAdapter(getContext(),listDrugName2, listDosage2, type2, route2, listFrequency2, unit2, adminTime2, site2);
                    listView.setAdapter(preparationForPatientAdapter);
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
                List<String> booleanListcheck = new ArrayList<String>();
                for(int i=0; i<preparationForPatientAdapter.getCheckStatus().size();i++){
                    booleanListcheck.add(preparationForPatientAdapter.getCheckStatus().get(i).toString());
                    Log.d("check","booleanListcheck : "+booleanListcheck.get(i));
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
