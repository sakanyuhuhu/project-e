package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildDrugNotPreapareAdapter;

public class BuildAddDrugNotPrepareFragment extends Fragment{
    private String nfcUID, sdlocID, wardName, time, RFID, firstName, lastName, prn, mrn, checkType, date;
    private int timeposition;
    private ListView listView;
    private TextView tvTime;
    private BuildDrugNotPreapareAdapter buildDrugNotPreapareAdapter;


    public BuildAddDrugNotPrepareFragment() {
        super();
    }

    public static BuildAddDrugNotPrepareFragment newInstance(String nfcUID, String sdlocID, String wardName, int timeposition, String time, String RFID, String firstName, String lastName, String prn, String mrn, String checkType, String date) {
        BuildAddDrugNotPrepareFragment fragment = new BuildAddDrugNotPrepareFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        args.putString("RFID", RFID);
        args.putString("firstname", firstName);
        args.putString("lastname", lastName);
        args.putString("prn", prn);
        args.putString("mrn", mrn);
        args.putString("checkType", checkType);
        args.putString("date", date);
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
        View rootView = inflater.inflate(R.layout.fragment_prepare_not_drug_add, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");
        RFID = getArguments().getString("RFID");
        firstName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        prn = getArguments().getString("prn");
        mrn = getArguments().getString("mrn");
        checkType = getArguments().getString("checkType");
        date = getArguments().getString("date");

        Log.d("check", "BuildPreparationForPatientFragment nfcUId = "+nfcUID+" /sdlocId = " + sdlocID + " /wardName = " + wardName+
                " /timeposition = " +timeposition +" /position = " + timeposition+" /time = "+time+" /RFID = "+RFID+ " /firstName = " + firstName + " /lastName = " + lastName+
                " /prn = "+prn+" /mrn = "+mrn+" /checkType = "+checkType+" /date = "+date);

        listView = (ListView) rootView.findViewById(R.id.lvDrugNotPreapareAdapter);
        buildDrugNotPreapareAdapter = new BuildDrugNotPreapareAdapter();

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvTime.setText("เลือกยาเพื่อนำมาบริหารในมื้อนี้ ("+time+")");

        loadDrugNotPrepare(mrn, checkType, date);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadDrugNotPrepare(String mrn, String checkType, String date){

    }
}
