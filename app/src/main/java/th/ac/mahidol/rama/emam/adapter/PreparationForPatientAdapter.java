package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.PreparationForPatientListView;

public class PreparationForPatientAdapter extends BaseAdapter {
    private List<String> strDrugName, strDosage, strUnit, strType, strRoute, strFrequency, strAdminTime, strSite, strNoteRadio, strStatusHold, strStatus;
    private List<String> drugName, dosage, unit, route;
    private List<Boolean> isCheck, isCheckHold;
    private List<Integer> isCheckNoteRadio, noteTricker;
    private Context context;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName;

    public PreparationForPatientAdapter(Context context,List<String> listDrugName, List<String> listDosage, List<String> listType, List<String> listRoute, List<String> listFrequency, List<String> listUnit, List<String> listAdminTime, List<String> listSite) {
        this.strDrugName = listDrugName;
        this.strDosage = listDosage;
        this.strUnit = listUnit;
        this.strType = listType;
        this.strRoute = listRoute;
        this.strFrequency = listFrequency;
        this.strAdminTime = listAdminTime;
        this.strSite = listSite;
        this.context = context;

        isCheck = new ArrayList<Boolean>();
        isCheckHold = new ArrayList<Boolean>();
        strStatusHold = new ArrayList<String>();
        strStatus = new ArrayList<String>();
        strNoteRadio = new ArrayList<String>();
        isCheckNoteRadio = new ArrayList<Integer>();
        noteTricker = new ArrayList<Integer>();
//15-08-16
        drugName = new ArrayList<String>();
        dosage = new ArrayList<String>();
        unit = new ArrayList<String>();
        route = new ArrayList<String>();


        for (int i=0;i<strDrugName.size();i++){
            isCheckHold.add(i, false);
            isCheck.add(i, false);
            isCheckNoteRadio.add(i, 0);
            strNoteRadio.add(i, "ไม่ระบุสาเหตุตามหัวข้อข้างล่าง");
            strStatusHold.add(i, "");
            strStatus.add(i, "");
            noteTricker.add(i, 0);
//15-08-16
            drugName.add(i, "");
            dosage.add(i, "");
            unit.add(i, "");
            route.add(i, "");
        }
    }

    @Override
    public int getCount() {
        return strDrugName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public List getIsCheckStatus(){
        return isCheck;
    }

    public List getValueCheckNoteRadio(){
        return strNoteRadio;
    }

    public List getStrStatusHold(){
        return strStatusHold;
    }

    public List getStrStatus(){
        return strStatus;
    }

    public List getIsCheckHold(){
        return isCheckHold;
    }

    public List getDrugName(){
        return drugName;
    }

    public List getDosage(){
        return dosage;
    }

    public List getUnit(){
        return unit;
    }

    public List getRoute(){
        return route;
    }

    public List getNoteTricker(){
        return noteTricker;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup viewGroup) {
        final PreparationForPatientListView preparationForPatientListView = new PreparationForPatientListView(viewGroup.getContext());
        preparationForPatientListView.setDrugName(strDrugName.get(position), strDosage.get(position), strType.get(position), strRoute.get(position),
                strFrequency.get(position), strUnit.get(position), strAdminTime.get(position), strSite.get(position));
//15-08-16
        drugName.add(position, strDrugName.get(position));
        dosage.add(position, strDosage.get(position));
        unit.add(position, strUnit.get(position));
        route.add(position, strRoute.get(position));

        preparationForPatientListView.setCheck(isCheck.get(position));
        final CheckBox checkBox =  preparationForPatientListView.isCheck();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheck.set(position,checkBox.isChecked());
                noteTricker.set(position, 0);
            }
        });

        final ImageView imageView = preparationForPatientListView.imageViewNote();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custom_dialog_prepare, null);
                dialogView.setBackgroundResource(R.color.colorPeachPuff);

                tvDrugName = (TextView)dialogView.findViewById(R.id.tvDrugName);
                tvDrugName.setText(" "+strDrugName.get(position)+" "+strDosage.get(position)+" "+strUnit.get(position));
                chkHold = (CheckBox)dialogView.findViewById(R.id.chkHold);
                txtStatusHold = (EditText)dialogView.findViewById(R.id.txtStatusHold);
                radioGroup = (RadioGroup)dialogView.findViewById(R.id.radiogroup);
                txtStatus = (EditText)dialogView.findViewById(R.id.txtStatus);

                if(isCheck.get(position) == true ){
                    isCheckNoteRadio.set(position, R.id.rdb1);
                    chkHold.setChecked(false);
                    isCheckHold.set(position, false);
                    noteTricker.set(position, 0);
                    txtStatus.setText("");
                    strStatus.add(position, String.valueOf(txtStatus.getText()));
                    Log.d("check", "str = "+txtStatus.getText());
                }

                if(isCheckNoteRadio.get(position) == 0)
                    isCheckNoteRadio.add(position, radioGroup.getId()+1);

                if(isCheckNoteRadio.get(position) != 0)
                    radioGroup.check(isCheckNoteRadio.get(position));

                if(isCheckHold.get(position) == true){
                    isCheck.set(position, false);
                    chkHold.setChecked(true);
                    txtStatusHold.setText(strStatusHold.get(position));
                    txtStatus.setText(strStatus.get(position));
                    Log.d("check", "CheckHold txtStatus = "+txtStatus.getText());
                }

                if(!(strStatus.get(position).equals(""))){
                    txtStatus.setText(strStatus.get(position));
                    isCheck.set(position, false);
                    Log.d("check", "txt = "+txtStatus.getText());
                }

                builder.setView(dialogView);
                builder.setTitle("บันทึกข้อความสำหรับเตรียมยา");
                builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyDataSetChanged();
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton)dialogView.findViewById(selectedId);
                        strStatus.add(position, String.valueOf(txtStatus.getText()));
                        Log.d("check", "strStatus = "+strStatus.get(position));
                        if(radioButton.isChecked()){
                            if(radioButton.getId() == R.id.rdb1 && strStatus.get(position).equals("")){
                                isCheckNoteRadio.set(position, radioButton.getId());
                                isCheck.set(position, true);
                                preparationForPatientListView.setCheck(true);
                                strNoteRadio.set(position, String.valueOf(radioButton.getText()));
                                noteTricker.set(position, 0);

                                if(chkHold.isChecked() == true){
                                    isCheck.set(position, false);
                                    isCheckHold.set(position, true);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                    noteTricker.set(position, 1);
                                } else {
                                    isCheckHold.set(position, false);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                    noteTricker.set(position, 0);
                                }
                            }
                            else if(radioButton.getId() == R.id.rdb1 && !(strStatus.get(position).equals(""))){
                                isCheckNoteRadio.set(position, radioButton.getId());
                                isCheck.set(position, false);
                                preparationForPatientListView.setCheck(false);
                                strNoteRadio.set(position, String.valueOf(radioButton.getText()));
                                noteTricker.set(position, 1);

                                if(chkHold.isChecked() == true){
                                    isCheck.set(position, false);
                                    isCheckHold.set(position, true);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                } else {
                                    isCheckHold.set(position, false);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                }
                            }
                            else {
                                isCheckNoteRadio.set(position, radioButton.getId());
                                isCheck.set(position, false);
                                preparationForPatientListView.setCheck(false);
                                strNoteRadio.set(position, String.valueOf(radioButton.getText()));
                                strStatus.set(position, String.valueOf(txtStatus.getText()));
                                noteTricker.set(position, 1);

                                if(chkHold.isChecked() == true){
                                    isCheck.set(position, false);
                                    isCheckHold.set(position, true);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                } else {
                                    isCheckHold.set(position, false);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                }
                            }
                        }
                    }
                });
                builder.setNegativeButton("ยกเลิก",null);
                builder.create();
                builder.show();
            }
        });
        return preparationForPatientListView;
    }
}
