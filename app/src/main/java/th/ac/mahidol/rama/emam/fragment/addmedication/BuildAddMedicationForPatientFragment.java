package th.ac.mahidol.rama.emam.fragment.addmedication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.addmedication.AddMedicationPatientAllActivity;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.view.history.BuildHistoryHeaderPatientDataView;

public class BuildAddMedicationForPatientFragment extends Fragment implements View.OnClickListener{
    private String nfcUID, sdlocID, wardName, mrn, selectedItem;
    private int position;
    private BuildHistoryHeaderPatientDataView buildHistoryHeaderPatientDataView;
    private Button btnCancel, btnAdd;
    private Spinner spinnerRoute;
    private EditText edtDrugName, edtDrugID, edtDosage, edtUnit, edtFrequency, edtMedthod;

    public BuildAddMedicationForPatientFragment() {
        super();
    }

    public static BuildAddMedicationForPatientFragment newInstance(String nfcUID, String sdlocID, String wardName, int position) {
        BuildAddMedicationForPatientFragment fragment = new BuildAddMedicationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_for_addmedication, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        position = getArguments().getInt("position");
        Log.d("check", "BuildAddMedicationForPatientFragment nfcUID = "+nfcUID+" /sdlocID = "+sdlocID+" /wardName = "+wardName+" /position = "+position);

        edtDrugName = (EditText) rootView.findViewById(R.id.edtDrugName);
        edtDrugID = (EditText) rootView.findViewById(R.id.edtDrugID);
        edtDosage = (EditText) rootView.findViewById(R.id.edtDosage);
        edtUnit = (EditText) rootView.findViewById(R.id.edtUnit);
        edtFrequency = (EditText) rootView.findViewById(R.id.edtFrequency);
        edtMedthod = (EditText) rootView.findViewById(R.id.edtMedthod);
        spinnerRoute = (Spinner) rootView.findViewById(R.id.spinnerRoute);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        buildHistoryHeaderPatientDataView = (BuildHistoryHeaderPatientDataView) rootView.findViewById(R.id.headerPatientAdapter);

        SharedPreferences prefs = getContext().getSharedPreferences("addmedpatientalldata", Context.MODE_PRIVATE);
        String data = prefs.getString("addmedpatientalldata",null);
        if(data != null) {
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data, ListPatientDataDao.class);
            mrn = listPatientDataDao.getPatientDao().get(position).getMRN();
            Log.d("check", "data size = " + listPatientDataDao.getPatientDao().size() + " position = " + position + " mrn = " + mrn);
            buildHistoryHeaderPatientDataView.setData(listPatientDataDao, position);
        }

        getSpinnerRoute();
        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK){
                    Intent intent = new Intent(getContext(), AddMedicationPatientAllActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void getSpinnerRoute() {
        spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCancel){
            Intent intent = new Intent(getContext(), AddMedicationPatientAllActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else if(view.getId() == R.id.btnAdd){
            Log.d("check", "spinnerRoute = "+selectedItem);
            Log.d("check", "Drug name = "+edtDrugName.getText().toString());
        }
    }
}
