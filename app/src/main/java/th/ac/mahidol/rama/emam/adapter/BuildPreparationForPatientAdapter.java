package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import th.ac.mahidol.rama.emam.view.BuildPreparationForPatientListView;

public class BuildPreparationForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName;
    private BuildPreparationForPatientListView buildPreparationForPatientListView;


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
        buildPreparationForPatientListView = new BuildPreparationForPatientListView(viewGroup.getContext());
        buildPreparationForPatientListView.setDrugName(dao.getListDrugCardDao().get(position));

        CheckBox checkBox = buildPreparationForPatientListView.isCheck();
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
                dao.getListDrugCardDao().get(position).setCheckType("First Check");
            }
        });

        ImageView imageViewNote = buildPreparationForPatientListView.imageViewNote();
        imageViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drugNoteDialog(position);
            }
        });

        return buildPreparationForPatientListView;
    }

    private void drugNoteDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_prepare, null);
        dialogView.setBackgroundResource(R.color.colorLemonChiffon);

        tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
        chkHold = (CheckBox) dialogView.findViewById(R.id.chkHold);
        txtStatusHold = (EditText) dialogView.findViewById(R.id.txtStatusHold);
        txtStatus = (EditText) dialogView.findViewById(R.id.txtStatus);
        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup);

        tvDrugName.setText(" " + dao.getListDrugCardDao().get(position).getTradeName() +
                " " + dao.getListDrugCardDao().get(position).getDose() +
                " " + dao.getListDrugCardDao().get(position).getUnit());

        chkHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkHold.isChecked() == true) {
                    txtStatusHold.setEnabled(true);
                    dao.getListDrugCardDao().get(position).setComplete("0");
                } else {
                    txtStatusHold.setEnabled(false);
                    txtStatusHold.setText("");
                }
            }
        });

        if(dao.getListDrugCardDao().get(position).getStatus() != null){
            if(dao.getListDrugCardDao().get(position).getStatus().equals("hold")) {
                chkHold.setChecked(true);
                txtStatusHold.setEnabled(true);
                txtStatusHold.setText(dao.getListDrugCardDao().get(position).getDescriptionTemplate());
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
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("ไม่มียา"))
                radioGroup.check(R.id.rdb3);
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("ห้องยาส่งยาผิด"))
                radioGroup.check(R.id.rdb4);
            else if(dao.getListDrugCardDao().get(position).getStrRadio().equals("ยาตก/แตก"))
                radioGroup.check(R.id.rdb5);
            else if (dao.getListDrugCardDao().get(position).getStrRadio().equals("ผู้ป่วยไปทำหัตการ"))
                radioGroup.check(R.id.rdb6);
        }
        builder.setView(dialogView);
        builder.setTitle("บันทึกข้อความสำหรับเตรียมยา");
        builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)dialogView.findViewById(selectedId);
                dao.getListDrugCardDao().get(position).setDescription(txtStatus.getText().toString());
                dao.getListDrugCardDao().get(position).setCheckType("First Check");
                if(chkHold.isChecked() == true) {
                    buildPreparationForPatientListView.setChangeNote();
                    dao.getListDrugCardDao().get(position).setComplete("0");
                    dao.getListDrugCardDao().get(position).setStatus("hold");
                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                    if (radioButton.isChecked()) {
                        dao.getListDrugCardDao().get(position).setDescriptionTemplate(txtStatusHold.getText().toString());
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
                            dao.getListDrugCardDao().get(position).setStrRadio("ไม่มียา");
                        }
                        else if (radioButton.getId() == R.id.rdb4){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ห้องยาส่งยาผิด");
                        }
                        else if (radioButton.getId() == R.id.rdb5){
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ยาตก/แตก");
                        }
                        else {
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ผู้ป่วยไปทำหัตการ");
                        }
                    }

                } else{
                    if (radioButton.isChecked()) {
                        dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                        dao.getListDrugCardDao().get(position).setStatus("normal");
                        if (radioButton.getId() == R.id.rdb1) {
                            dao.getListDrugCardDao().get(position).setCheckNote("0");
                            dao.getListDrugCardDao().get(position).setComplete("1");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("");
                        }
                        else if (radioButton.getId() == R.id.rdb2){
                            buildPreparationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("NPO");
                        }
                        else if (radioButton.getId() == R.id.rdb3){
                            buildPreparationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ไม่มียา");
                        }
                        else if (radioButton.getId() == R.id.rdb4){
                            buildPreparationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ห้องยาส่งยาผิด");
                        }
                        else if (radioButton.getId() == R.id.rdb5){
                            buildPreparationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ยาตก/แตก");
                        }
                        else {
                            buildPreparationForPatientListView.setChangeNote();
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                            dao.getListDrugCardDao().get(position).setStrRadio("ผู้ป่วยไปทำหัตการ");
                        }
                    }
                    if(!dao.getListDrugCardDao().get(position).getDescription().equals("")){
                        buildPreparationForPatientListView.setChangeNote();
                        dao.getListDrugCardDao().get(position).setCheckNote("1");
                        dao.getListDrugCardDao().get(position).setStatus("normal");
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
