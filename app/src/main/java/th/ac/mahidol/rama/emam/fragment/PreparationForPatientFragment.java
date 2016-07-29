package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.PreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.ListMedicalCardCollectionDao;
import th.ac.mahidol.rama.emam.dao.PatientInfoForPersonCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class PreparationForPatientFragment extends Fragment {

    private  String[] listDrugName, listDosage, listFrequency, type, route, unit;
    private String nfcUId, sdlocId, gettimer, patientName, bedNo, mRN, strtimer;
    private TextView tvBedNo, tvPatientName, tvPatientID, tvHN, tvBirth, tvAge, tvSex, tvStatus;
    private TextView tvDrugAllergy, tvDate;
    private ImageView ivCheckMed, ivNote;
    private ListMedicalCardCollectionDao listMedicalCardCollectionDao;

    public PreparationForPatientFragment() {
        super();
    }

    public static PreparationForPatientFragment newInstance(String gettimer, String nfcUId, String sdlocId, String patientName, String bedNo, String mRN) {
        PreparationForPatientFragment fragment = new PreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("timer", gettimer);
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("patientName", patientName);
        args.putString("bedNo", bedNo);
        args.putString("mRN", mRN);
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
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId + " / patientName : " + patientName + " / bedNo : " + bedNo + " / mRN : " + mRN);
        if(gettimer.substring(0,1).equals("0")){
            strtimer = gettimer.substring(1,2);
        }
        else{
            strtimer = gettimer.substring(0,2);
        }
        Date date = new Date();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateToday = sDateFormat.format(date);
        String stryear = dateToday.substring(0,4);
        final int yearstr = Integer.parseInt(stryear);
        Log.d("check", "Date : " + dateToday  + " (" + gettimer + ")");

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
        ivCheckMed = (ImageView) rootView.findViewById(R.id.ivCheckMed);
        ivNote = (ImageView) rootView.findViewById(R.id.ivNote);

        Call<PatientInfoForPersonCollectionDao> callInfo = HttpManager.getInstance().getService().loadListPatientInfo(mRN);
        callInfo.enqueue(new Callback<PatientInfoForPersonCollectionDao>() {
            @Override
            public void onResponse(Call<PatientInfoForPersonCollectionDao> call, Response<PatientInfoForPersonCollectionDao> response) {
                String sex, strAge, stryear;
                int year, age;
                if(response.body().getListPatientInfoBean().getGender().equals("M"))
                    sex = "ชาย";
                else
                    sex = "หญิง";
                strAge = response.body().getListPatientInfoBean().getDob().trim();
                stryear = strAge.substring(6);
                year = Integer.parseInt(stryear);
                age = yearstr - year;
                tvBedNo.setText("เลขที่เตียง/ห้อง: " + bedNo);
                tvPatientName.setText(patientName);
                tvPatientID.setText(response.body().getListPatientInfoBean().getIdCardNo());
                tvHN.setText("HN:" + mRN);
                tvSex.setText("เพศ:"+sex);
                tvBirth.setText("วันเกิด:"+response.body().getListPatientInfoBean().getDob().trim());
                tvAge.setText("อายุ:"+String.valueOf(age)+"ปี,"+" เดือน,"+" วัน");
                tvStatus.setText("สถานะภาพ:"+response.body().getListPatientInfoBean().getMaritalstatus());

            }

            @Override
            public void onFailure(Call<PatientInfoForPersonCollectionDao> call, Throwable t) {
                Log.d("check", "PreparationForPatientFragment callInfo Failure " + t);
            }
        });

        tvDrugAllergy.setText("การแพ้ยา: ");
        tvDate.setText(dateToday + " (" + gettimer + ")");

        Call<ListMedicalCardCollectionDao> callDrug = HttpManager.getInstance().getService().loadListMedicalCard(mRN);
        callDrug.enqueue(new Callback<ListMedicalCardCollectionDao>() {
            @Override
            public void onResponse(Call<ListMedicalCardCollectionDao> call, Response<ListMedicalCardCollectionDao> response) {
                listMedicalCardCollectionDao = response.body();
                Log.d("check", "SIZE listMedical " + listMedicalCardCollectionDao.getListMedicalCardBean().size());
                listDrugName = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                unit = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                listDosage = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                listFrequency = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                type = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                route = new String[listMedicalCardCollectionDao.getListMedicalCardBean().size()];
                int j=0;
                for (int i = 0; i < listMedicalCardCollectionDao.getListMedicalCardBean().size(); i++) {
                    if(listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminTimeHour().equals(strtimer)) {
                        listDrugName[j] = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getTradeName();
                        listDosage[j] = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getDose();
                        unit[j] = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getUnit();
                        listFrequency[j] = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getFrequency();
                        type[j] = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getAdminType();
                        route[j] = listMedicalCardCollectionDao.getListMedicalCardBean().get(i).getRoute();
                        j++;
                    }
                }

                ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
                PreparationForPatientAdapter preparationForPatientAdapter = new PreparationForPatientAdapter(listDrugName, listDosage, type, route, listFrequency, unit);
                listView.setAdapter(preparationForPatientAdapter);
            }

            @Override
            public void onFailure(Call<ListMedicalCardCollectionDao> call, Throwable t) {
                Log.d("check", "PreparationForPatientFragment callDrug Failure " + t);
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
