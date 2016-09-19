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

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildAdministrationForPatientListView;

public class BuildAdministrationForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;
    private EditText txtStatus, txtStatusHoldBP, txtStatusHoldHR, txtStatusHoldCBG;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName;
    private BuildAdministrationForPatientListView buildAdministrationForPatientListView;


    public void setDao(Context context, ListDrugCardDao dao){
        this.dao = dao;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(dao == null)
            return 0;
        if(dao.getListDrugCardDao() == null)
            return 0;
        return dao.getListDrugCardDao().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        buildAdministrationForPatientListView = new BuildAdministrationForPatientListView(viewGroup.getContext());
        buildAdministrationForPatientListView.setDrugName(dao.getListDrugCardDao().get(position));

        CheckBox checkBox = buildAdministrationForPatientListView.isCheck();
        checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete()!= null ? dao.getListDrugCardDao().get(position).getComplete().equals("1")?true : false : false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dao.getListDrugCardDao().get(position).setComplete(isChecked ? "1":"0");
                dao.getListDrugCardDao().get(position).setStatus("normal");
                dao.getListDrugCardDao().get(position).setCheckNote("0");
                dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                dao.getListDrugCardDao().get(position).setDescription("");
                dao.getListDrugCardDao().get(position).setIdRadio(R.id.rdb1);
                dao.getListDrugCardDao().get(position).setStrRadio("");
                dao.getListDrugCardDao().get(position).setCheckType("Administration");
            }
        });

        ImageView imageViewNote = buildAdministrationForPatientListView.imageViewNote();
        imageViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drugNoteDialog(position);
            }
        });
        return buildAdministrationForPatientListView;
    }

    private void drugNoteDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_administration, null);
        dialogView.setBackgroundResource(R.color.colorLemonChiffon);

        tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
        chkHold = (CheckBox) dialogView.findViewById(R.id.chkHold);
        txtStatusHoldBP = (EditText) dialogView.findViewById(R.id.txtStatusHoldBP);
        txtStatusHoldHR = (EditText) dialogView.findViewById(R.id.txtStatusHoldHR);
        txtStatusHoldCBG = (EditText) dialogView.findViewById(R.id.txtStatusHoldCBG);
        txtStatus = (EditText) dialogView.findViewById(R.id.txtStatus);
        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup);

        tvDrugName.setText(" " + dao.getListDrugCardDao().get(position).getTradeName() +
                " " + dao.getListDrugCardDao().get(position).getDose() +
                " " + dao.getListDrugCardDao().get(position).getUnit());

        chkHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkHold.isChecked() == true) {
                    txtStatusHoldBP.setEnabled(true);
                    txtStatusHoldHR.setEnabled(true);
                    txtStatusHoldCBG.setEnabled(true);
                    dao.getListDrugCardDao().get(position).setComplete("0");
                } else {
                    txtStatusHoldBP.setEnabled(false);
                    txtStatusHoldHR.setEnabled(false);
                    txtStatusHoldCBG.setEnabled(false);
                    txtStatusHoldBP.setText("");
                    txtStatusHoldHR.setText("");
                    txtStatusHoldCBG.setText("");
                }
            }
        });
        Log.d("check", "Status = "+dao.getListDrugCardDao().get(position).getStatus());
        Log.d("check", "strRadio = "+dao.getListDrugCardDao().get(position).getStrRadio());
        Log.d("check", "getDescription = "+dao.getListDrugCardDao().get(position).getDescription());
        Log.d("check", "BP = "+dao.getListDrugCardDao().get(position).getStrBP()+" Heart Rate = "+dao.getListDrugCardDao().get(position).getStrHR()+" CBG = "+dao.getListDrugCardDao().get(position).getStrCBG());
        if(dao.getListDrugCardDao().get(position).getStatus() != null){
            if(dao.getListDrugCardDao().get(position).getStatus().equals("hold")) {
                chkHold.setChecked(true);
                txtStatusHoldBP.setEnabled(true);
                txtStatusHoldHR.setEnabled(true);
                txtStatusHoldCBG.setEnabled(true);
                txtStatusHoldBP.setText(dao.getListDrugCardDao().get(position).getStrBP());
                txtStatusHoldHR.setText(dao.getListDrugCardDao().get(position).getStrHR());
                txtStatusHoldCBG.setText(dao.getListDrugCardDao().get(position).getStrCBG());
            }
        }

        if(dao.getListDrugCardDao().get(position).getDescription() != null){
            dao.getListDrugCardDao().get(position).setComplete("0");
            txtStatus.setText(dao.getListDrugCardDao().get(position).getDescription());
        }

        if(dao.getListDrugCardDao().get(position).getStrRadio() != null){
            if(dao.getListDrugCardDao().get(position).getStrRadio().equals(""))
                radioGroup.check(R.id.rdb1);
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("NPO"))
                radioGroup.check(R.id.rdb2);
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("คนไข้ปฏิเสธ"))
                radioGroup.check(R.id.rdb3);
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("คนไข้มีอาการคลื่นไส้อาเจียน"))
                radioGroup.check(R.id.rdb4);
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("แทงเส้นเลือดไม่ได้"))
                radioGroup.check(R.id.rdb5);
            else if (dao.getListDrugCardDao().get(position).getStrRadio().equals("แพทย์สั่ง off ณ เวลานั้น"))
                radioGroup.check(R.id.rdb6);
            else if (dao.getListDrugCardDao().get(position).getStrRadio().equals("ผู้ป่วยไปทำหัตการ"))
                radioGroup.check(R.id.rdb7);
        }

        builder.setView(dialogView);
        builder.setTitle("บันทึกข้อความสำหรับบริหารยา");
        builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)dialogView.findViewById(selectedId);
                dao.getListDrugCardDao().get(position).setDescription(txtStatus.getText().toString());
                dao.getListDrugCardDao().get(position).setCheckType("Administration");
                if(chkHold.isChecked() == true) {
                    buildAdministrationForPatientListView.setChangeNote();
                    dao.getListDrugCardDao().get(position).setComplete("0");
                    dao.getListDrugCardDao().get(position).setStatus("hold");
                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                    dao.getListDrugCardDao().get(position).setStrBP(txtStatusHoldBP.getText().toString());
                    dao.getListDrugCardDao().get(position).setStrHR(txtStatusHoldHR.getText().toString());
                    dao.getListDrugCardDao().get(position).setStrCBG(txtStatusHoldCBG.getText().toString());
                    if (radioButton.isChecked()) {
                        if (radioButton.getId() == R.id.rdb1) {
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("");
                        }
                        else if (radioButton.getId() == R.id.rdb2){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("NPO");
                        }
                        else if (radioButton.getId() == R.id.rdb3){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("คนไข้ปฏิเสธ");
                        }
                        else if (radioButton.getId() == R.id.rdb4){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("คนไข้มีอาการคลื่นไส้อาเจียน");
                        }
                        else if (radioButton.getId() == R.id.rdb5){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("แทงเส้นเลือดไม่ได้");
                        }
                        else if (radioButton.getId() == R.id.rdb6){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("แพทย์สั่ง off ณ เวลานั้น");
                        }
                        else {
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ผู้ป่วยไปทำหัตการ");
                        }
                    }

                } else{
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    if (radioButton.isChecked()) {
                        dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                        if (radioButton.getId() == R.id.rdb1) {
                            dao.getListDrugCardDao().get(position).setCheckNote("0");
                            dao.getListDrugCardDao().get(position).setComplete("1");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("");
                        }
                        else if (radioButton.getId() == R.id.rdb2){
                            buildAdministrationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("NPO");
                        }
                        else if (radioButton.getId() == R.id.rdb3){
                            buildAdministrationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("คนไข้ปฏิเสธ");
                        }
                        else if (radioButton.getId() == R.id.rdb4){
                            buildAdministrationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("คนไข้มีอาการคลื่นไส้อาเจียน");
                        }
                        else if (radioButton.getId() == R.id.rdb5){
                            buildAdministrationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("แทงเส้นเลือดไม่ได้");
                        }
                        else if (radioButton.getId() == R.id.rdb6){
                            buildAdministrationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("แพทย์สั่ง off ณ เวลานั้น");
                        }
                        else {
                            buildAdministrationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ผู้ป่วยไปทำหัตการ");
                        }
                    }
                    if(!dao.getListDrugCardDao().get(position).getDescription().equals("")){
                        buildAdministrationForPatientListView.setChangeNote();
                        dao.getListDrugCardDao().get(position).setCheckNote("1");
                        dao.getListDrugCardDao().get(position).setComplete("0");
                    }
                }
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("ยกเลิก",null);
        builder.create();
        builder.show().getWindow().setLayout(1200,1250);
    }
}
