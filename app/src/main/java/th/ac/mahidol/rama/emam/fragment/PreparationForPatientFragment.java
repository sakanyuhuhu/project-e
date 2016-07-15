package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.PreparationForPatientAdapter;

public class PreparationForPatientFragment extends Fragment {

    private final String[] listMedicalName = new String[]{ "Atenolol 50 mg", "Metformin 500 mg" };
    private String nfcUId;
    private String sdlocId;
    private String patientName;
    private String bedNo;
    private String mRN;
    private TextView tvMedicalName;
    private TextView tvBedNo;
    private TextView tvPatientName;
    private TextView tvHN;
    private TextView tvBirth;

    public PreparationForPatientFragment() {
        super();
    }

    public static PreparationForPatientFragment newInstance(String nfcUId, String sdlocId, String patientName, String bedNo, String mRN) {
        PreparationForPatientFragment fragment = new PreparationForPatientFragment();
        Bundle args = new Bundle();
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
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        patientName = getArguments().getString("patientName");
        bedNo = getArguments().getString("bedNo");
        mRN = getArguments().getString("mRN");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId + " / patientName : " + patientName + " / bedNo : " + bedNo + " / mRN : " + mRN);

        tvMedicalName = (TextView) rootView.findViewById(R.id.tvMedicalName);
        tvBedNo = (TextView) rootView.findViewById(R.id.tvBedNo);
        tvPatientName = (TextView) rootView.findViewById(R.id.tvPatientName);
        tvHN = (TextView) rootView.findViewById(R.id.tvMrn);
        tvBirth = (TextView) rootView.findViewById(R.id.tvBirth);

        tvBedNo.setText("เลขที่เตียง/ห้อง: " + bedNo);
        tvPatientName.setText(patientName);
        tvHN.setText("HN: " + mRN);

        ListView listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        PreparationForPatientAdapter preparationForPatientAdapter = new PreparationForPatientAdapter(listMedicalName);
        listView.setAdapter(preparationForPatientAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Medical selected : " + listMedicalName[position]);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        intent.putExtra("nfcUId", nfcUId);
//                        intent.putExtra("sdlocId", sdlocId);
//                        getActivity().startActivity(intent);
//                    }
//                });
//                builder.setNegativeButton("Cancel", null);
//                builder.create();
//                builder.show();
//            }
//        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
