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
    private List<String> strDrugName, strDosage, unit, type, route, strFrequency, adminTime, site, strNoteRadio, strStatusHold, strStatus;
    private List<Boolean> isCheck, isCheckHold;
    private List<Integer> isCheckNoteRadio;
    private Context context;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName;

    public PreparationForPatientAdapter(Context context,List<String> listDrugName, List<String> listDosage, List<String> type, List<String> route, List<String> listFrequency, List<String> unit, List<String> adminTime, List<String> site) {
        this.strDrugName = listDrugName;
        this.strDosage = listDosage;
        this.unit = unit;
        this.type = type;
        this.route = route;
        this.strFrequency = listFrequency;
        this.adminTime = adminTime;
        this.site = site;
        this.context = context;

        isCheck = new ArrayList<Boolean>();
        isCheckHold = new ArrayList<Boolean>();
        strStatusHold = new ArrayList<String>();
        strStatus = new ArrayList<String>();
        strNoteRadio = new ArrayList<String>();
        isCheckNoteRadio = new ArrayList<Integer>();

        for (int i=0;i<strDrugName.size();i++){
            isCheckHold.add(i, false);
            isCheck.add(i, false);
            isCheckNoteRadio.add(i, 0);//2131492952 เป็น id แรกของ RadioButton 2131558488
            strNoteRadio.add(i, "ไม่ระบุสาเหตุตามหัวข้อข้างล่าง");
            strStatusHold.add(i, "");
            strStatus.add(i, "");
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

    @Override
    public View getView(final int position, final View convertView, ViewGroup viewGroup) {
        final PreparationForPatientListView preparationForPatientListView = new PreparationForPatientListView(viewGroup.getContext());
        preparationForPatientListView.setDrugName(strDrugName.get(position), strDosage.get(position), type.get(position), route.get(position), strFrequency.get(position), unit.get(position), adminTime.get(position), site.get(position));
//            Log.d("check", position+ " / strDrugName  : " + strDrugName.get(position) + "strDosage    : " + strDosage.get(position) + " type         : " + type.get(position) + " route        : " + route.get(position) + " strFrequency : " + strFrequency.get(position) + " unit         : " + unit.get(position));
        preparationForPatientListView.setCheck(isCheck.get(position));
        final CheckBox checkBox =  preparationForPatientListView.isCheck();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isCheck.set(position,checkBox.isChecked());
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
                tvDrugName.setText(" "+strDrugName.get(position)+" "+strDosage.get(position)+" "+unit.get(position));
                chkHold = (CheckBox)dialogView.findViewById(R.id.chkHold);
                txtStatusHold = (EditText)dialogView.findViewById(R.id.txtStatusHold);
                radioGroup = (RadioGroup)dialogView.findViewById(R.id.radiogroup);
                txtStatus = (EditText)dialogView.findViewById(R.id.txtStatus);

                if(isCheck.get(position) == true ){
                    isCheckNoteRadio.set(position, R.id.rdb1);
                    Log.d("check", "isCheckNoteRadio = "+R.id.rdb1);
                    chkHold.setChecked(false);
                    isCheckHold.set(position, false);
                }
                if(isCheckNoteRadio.get(position) == 0)
                    isCheckNoteRadio.add(position, radioGroup.getId()+1);

                if(isCheckNoteRadio.get(position) != 0)
                    radioGroup.check(isCheckNoteRadio.get(position));

                if(isCheckHold.get(position) == true){
                    isCheck.set(position, false);
                    chkHold.setChecked(true);
                    txtStatusHold.setText(strStatusHold.get(position));
                    Log.d("check", "txtStatusHold = "+txtStatusHold.getText());
                }
//ส่วนของ status ซึ่งยัง handle ไม่ได้
                if(!(txtStatus.getText().equals(""))){
                    Log.d("check", "txtStatus = "+txtStatus.getText());
                    strStatus.set(position, String.valueOf(txtStatus.getText()));
                    txtStatus.setText(strStatus.get(position));
                    Log.d("check", "if strStatus = "+strStatus.get(position));
                }
                else {
                    Log.d("check", "else txtStatus = "+txtStatus.getText());
                }

                builder.setView(dialogView);
                builder.setTitle("บันทึกข้อความสำหรับเตรียมยา");
                builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int iBuilder) {
                        notifyDataSetChanged();
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton)dialogView.findViewById(selectedId);
                        if(radioButton.isChecked()){
                            if(radioButton.getId() == R.id.rdb1){
                                isCheckNoteRadio.set(position, radioButton.getId());
                                isCheck.set(position, true);
                                preparationForPatientListView.setCheck(true);
                                strNoteRadio.set(position, String.valueOf(radioButton.getText()));

                                if(chkHold.isChecked() == true){
                                    isCheck.set(position, false);
                                    isCheckHold.set(position, true);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                }
                                else{
                                    isCheckHold.set(position, false);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                }
//
//                                if(strStatus.get(position).equals("")) {
//                                    isCheck.set(position, false);
//                                    preparationForPatientListView.setCheck(false);
//                                    strStatus.set(position, String.valueOf(txtStatus.getText()));
//                                }
//                                else{
//                                    isCheck.set(position, true);
//                                    preparationForPatientListView.setCheck(true);
//                                    strStatus.set(position, String.valueOf(txtStatus.getText()));
//                                }
                            }
                            else{
                                isCheckNoteRadio.set(position, radioButton.getId());
                                isCheck.set(position, false);
                                preparationForPatientListView.setCheck(false);
                                strNoteRadio.set(position, String.valueOf(radioButton.getText()));
                                strStatus.set(position, String.valueOf(txtStatus.getText()));
//                                Log.d("check", "2 strStatus = "+strStatus.get(position));

                                if(chkHold.isChecked() == true){
                                    isCheckHold.set(position, true);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                }
                                else{
                                    isCheckHold.set(position, false);
                                    strStatusHold.set(position, String.valueOf(txtStatusHold.getText()));
                                }
//
//                                if(strStatus.get(position).equals("")) {
//                                    isCheck.set(position, false);
//                                    preparationForPatientListView.setCheck(false);
//                                    strStatus.set(position, String.valueOf(txtStatus.getText()));
//                                }
//                                else{
//                                    isCheck.set(position, true);
//                                    preparationForPatientListView.setCheck(true);
//                                    strStatus.set(position, String.valueOf(txtStatus.getText()));
//                                }
                            }
                        }

//                        AlertDialog.Builder builderSave = new AlertDialog.Builder(context);
//                        builderSave.setTitle("คุณตองการจะบันทึกใช่หรือไม่?");
//                        builderSave.setPositiveButton("ใช่",new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int iBuilderSave) {
//
//                            }
//                        });
//                        builderSave.setNegativeButton("ไม่ใช่", null);
//                        builderSave.create();
//                        builderSave.show();

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
