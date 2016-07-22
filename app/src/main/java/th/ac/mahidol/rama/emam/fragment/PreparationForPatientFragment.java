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

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.PreparationForPatientAdapter;

public class PreparationForPatientFragment extends Fragment {

    private final String[] listDrugName = new String[]{ "Atenolol 50 mg", "Metformin 500 mg" };
    private String nfcUId;
    private String sdlocId;
    private String gettimer;
    private String patientName;
    private String bedNo;
    private String mRN;
    private TextView tvDrugName;
    private TextView tvBedNo;
    private TextView tvPatientName;
    private TextView tvHN;
    private TextView tvBirth;
    private TextView tvDrugAllergy;
    private TextView tvDate;
    private ImageView ivCheckMed;
    private ImageView ivNote;

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

    private void initInstances(View rootView, Bundle savedInstanceState) {
        gettimer = getArguments().getString("timer");
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        patientName = getArguments().getString("patientName");
        bedNo = getArguments().getString("bedNo");
        mRN = getArguments().getString("mRN");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId + " / patientName : " + patientName + " / bedNo : " + bedNo + " / mRN : " + mRN);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateToday = simpleDateFormat.format(date);
        Log.d("check", "Date : " + dateToday  + " (" + gettimer + ")");

        tvDrugName = (TextView) rootView.findViewById(R.id.tvDrugName);
        tvBedNo = (TextView) rootView.findViewById(R.id.tvBedNo);
        tvPatientName = (TextView) rootView.findViewById(R.id.tvPatientName);
        tvHN = (TextView) rootView.findViewById(R.id.tvMrn);
        tvBirth = (TextView) rootView.findViewById(R.id.tvBirth);
        tvDrugAllergy = (TextView) rootView.findViewById(R.id.tvDrugAllergy);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        ivCheckMed = (ImageView) rootView.findViewById(R.id.ivCheckMed);
        ivNote = (ImageView) rootView.findViewById(R.id.ivNote);

        tvBedNo.setText("เลขที่เตียง/ห้อง: " + bedNo);
        tvPatientName.setText(patientName);
        tvHN.setText("HN: " + mRN);
        tvBirth.setText("วันเกิด: ");
        tvDrugAllergy.setText("การแพ้ยา: ");
        tvDate.setText(dateToday + " (" + gettimer + ")");

        ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        PreparationForPatientAdapter preparationForPatientAdapter = new PreparationForPatientAdapter(listDrugName);
        listView.setAdapter(preparationForPatientAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
