package th.ac.mahidol.rama.emam.fragment.addmedication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.addmedication.AddMedicationPatientAllActivity;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.AdminTimeSelectionSpinner;
import th.ac.mahidol.rama.emam.view.history.BuildHistoryHeaderPatientDataView;

public class BuildAddMedicationForPatientFragment extends Fragment implements View.OnClickListener {
    private String nfcUID, wardID, sdlocID, wardName, mrn, selectedItem, dateSelectStart, dateSelectEnd, toDayDate;
    private int position;
    private BuildHistoryHeaderPatientDataView buildHistoryHeaderPatientDataView;
    private PatientDataDao patient;
    private Button btnCancel, btnAdd;
    private Spinner spinnerRoute;
    private EditText edtDrugName, edtDrugID, edtDosage, edtUnit, edtFrequency, edtMedthod;
    private TextView tvStartDate, tvEndDate;
    private ImageView imgCalendarStart, imgCalendarEnd;
    private Date datetoDay;
    long startMillis = 0;
    long endMillis = 0;
    String[] timeArrays = {"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private AdminTimeSelectionSpinner adminTimeSelectionSpinner;

    public BuildAddMedicationForPatientFragment() {
        super();
    }

    public static BuildAddMedicationForPatientFragment newInstance(String nfcUID, String wardID, String sdlocID, String wardName, int position, PatientDataDao patient) {
        BuildAddMedicationForPatientFragment fragment = new BuildAddMedicationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", position);
        args.putParcelable("patient", patient);
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
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        position = getArguments().getInt("position");
        patient = getArguments().getParcelable("patient");

        edtDrugName = (EditText) rootView.findViewById(R.id.edtDrugName);
        edtDrugID = (EditText) rootView.findViewById(R.id.edtDrugID);
        edtDosage = (EditText) rootView.findViewById(R.id.edtDosage);
        edtUnit = (EditText) rootView.findViewById(R.id.edtUnit);
        edtFrequency = (EditText) rootView.findViewById(R.id.edtFrequency);
        edtMedthod = (EditText) rootView.findViewById(R.id.edtMedthod);
        spinnerRoute = (Spinner) rootView.findViewById(R.id.spinnerRoute);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        tvStartDate = (TextView) rootView.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) rootView.findViewById(R.id.tvEndDate);
        imgCalendarStart = (ImageView) rootView.findViewById(R.id.imgCalendarStart);
        imgCalendarEnd = (ImageView) rootView.findViewById(R.id.imgCalendarEnd) ;

        adminTimeSelectionSpinner = (AdminTimeSelectionSpinner) rootView.findViewById(R.id.adminTimeSpinner);
        buildHistoryHeaderPatientDataView = (BuildHistoryHeaderPatientDataView) rootView.findViewById(R.id.headerPatientAdapter);

        adminTimeSelectionSpinner.set_items(timeArrays);


        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("dd/MM/yyyy");
        toDayDate = sdfForDrugUseDate.format(datetoDay);
        dateSelectStart = toDayDate;
        dateSelectEnd = toDayDate;
        tvStartDate.setText(dateSelectStart);
        tvEndDate.setText(dateSelectEnd);

        if (patient != null) {
            buildHistoryHeaderPatientDataView.setData(patient, position);
        }

        getSpinnerRoute();
        btnCancel.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        imgCalendarStart.setOnClickListener(this);
        imgCalendarEnd.setOnClickListener(this);

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
            intent.putExtra("wardId", wardID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else if(view.getId() == R.id.btnAdd){
            if(edtDrugName.getText().toString().equals(""))
                Toast.makeText(getActivity(), "กรุณาใส่ชื่อยา", Toast.LENGTH_LONG).show();
            else if(edtDosage.getText().toString().equals(""))
                Toast.makeText(getActivity(), "กรุณาใส่ Dosage", Toast.LENGTH_LONG).show();
            else if(edtUnit.getText().toString().equals(""))
                Toast.makeText(getActivity(), "กรุณาใส่ Unit", Toast.LENGTH_LONG).show();

            String getTimeItems = adminTimeSelectionSpinner.get_items().toString();
            String getTimeItem = getTimeItems.substring(1,getTimeItems.lastIndexOf("]"));
            String[] arrayTime = getTimeItem.split(", ");
            for(String s : arrayTime){
                if(s.equals("")){
                    Toast.makeText(getActivity(), "กรุณาใส่ Admin Time", Toast.LENGTH_LONG).show();
                }
            }

            Log.d("check", "spinnerRoute = "+selectedItem);
            Log.d("check", "Drug name = "+edtDrugName.getText().toString());
            Log.d("check", "Dosage = "+edtDosage.getText().toString());
            Log.d("check", "Unit = "+edtUnit.getText().toString());
            Log.d("check", "DateStart = "+tvStartDate.getText().toString());
            Log.d("check", "DateEnd = "+tvEndDate.getText().toString());

        }
        else if(view.getId() == R.id.imgCalendarStart){
            final View dialogViewDate = View.inflate(getActivity(), R.layout.custom_dialog_set_date, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            dialogViewDate.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = (DatePicker) dialogViewDate.findViewById(R.id.date_picker);
                    TimePicker timePicker = (TimePicker) dialogViewDate.findViewById(R.id.time_picker);

                    int pickYear, pickMonth, pickDay, pickHour = 0, pickMinute = 0;
                    Calendar calendar = null;
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getHour(),
                                pickMinute = timePicker.getMinute());
                        dateSelectStart = pickDay+"/"+(pickMonth+1)+"/"+pickYear;
                        tvStartDate.setText(dateSelectStart);
                    } else {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getCurrentHour(),
                                pickMinute = timePicker.getCurrentMinute());
                        dateSelectStart = pickDay+"/"+(pickMonth+1)+"/"+pickYear;
                        tvStartDate.setText(dateSelectStart);
                    }

                    Calendar timePick = Calendar.getInstance();
                    timePick.set(pickYear, pickMonth, pickDay, pickHour, pickMinute);
                    startMillis = timePick.getTimeInMillis();
                    endMillis = startMillis + 60 * 60 * 1000;
                    alertDialog.dismiss();
                }
            });
            alertDialog.setView(dialogViewDate);
            alertDialog.show();
        }
        else if(view.getId() == R.id.imgCalendarEnd){
            final View dialogViewDate = View.inflate(getActivity(), R.layout.custom_dialog_set_date, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            dialogViewDate.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = (DatePicker) dialogViewDate.findViewById(R.id.date_picker);
                    TimePicker timePicker = (TimePicker) dialogViewDate.findViewById(R.id.time_picker);

                    int pickYear, pickMonth, pickDay, pickHour = 0, pickMinute = 0;
                    Calendar calendar = null;
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getHour(),
                                pickMinute = timePicker.getMinute());
                        dateSelectEnd = pickDay+"/"+(pickMonth+1)+"/"+pickYear;
                        tvEndDate.setText(dateSelectEnd);
                    } else {
                        calendar = new GregorianCalendar(
                                pickYear = datePicker.getYear(),
                                pickMonth = datePicker.getMonth(),
                                pickDay = datePicker.getDayOfMonth(),
                                pickHour = timePicker.getCurrentHour(),
                                pickMinute = timePicker.getCurrentMinute());
                        dateSelectEnd = pickDay+"/"+(pickMonth+1)+"/"+pickYear;
                        tvEndDate.setText(dateSelectEnd);
                    }

                    Calendar timePick = Calendar.getInstance();
                    timePick.set(pickYear, pickMonth, pickDay, pickHour, pickMinute);
                    startMillis = timePick.getTimeInMillis();
                    endMillis = startMillis + 60 * 60 * 1000;
                    alertDialog.dismiss();
                }
            });
            alertDialog.setView(dialogViewDate);
            alertDialog.show();
        }
        else if(view.getId() == R.id.adminTimeSpinner){
            Log.d("check", "adminTimeSelectionSpinner = "+adminTimeSelectionSpinner.getId());
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
}
