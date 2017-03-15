package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildDrugForPatientListView;

public class BuildAdministrationForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;
    private EditText txtStatus, txtStatusHoldBP, txtStatusHoldHR, txtStatusHoldCBG;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName, tvCancel, tvSave;
    private String statusPatient;
    private List<String> listHAD;
    private BuildDrugForPatientListView buildDrugForPatientListView;
    int lastPosition = -1;

    public void setDao(Context context, ListDrugCardDao dao, String statusPatient, List<String> listHAD){
        this.dao = dao;
        this.context = context;
        this.statusPatient = statusPatient;
        this.listHAD = listHAD;
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

//        if (convertView != null)
//            buildDrugForPatientListView = (BuildDrugForPatientListView) convertView;
//        else {
            buildDrugForPatientListView = new BuildDrugForPatientListView(viewGroup.getContext());
            buildDrugForPatientListView.setDrugName(dao.getListDrugCardDao().get(position), statusPatient, listHAD);

            CheckBox checkBox = buildDrugForPatientListView.isCheck();

            if (statusPatient != null) {
                if (statusPatient.equals("1")) {
                    dao.getListDrugCardDao().get(position).setComplete("1");
                    checkBox.isChecked();
                    checkBox.setEnabled(false);
                } else {
                    if (dao.getListDrugCardDao().get(position).getComplete() != null) {
                        if (dao.getListDrugCardDao().get(position).getComplete().equals("1")) {
                            dao.getListDrugCardDao().get(position).setComplete("1");
                            checkBox.isChecked();
                            checkBox.setEnabled(false);
                        }
                    }
                }
            }

            checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete() != null ? dao.getListDrugCardDao().get(position).getComplete().equals("1") ? true : false : false);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked == true) {
                        buildDrugForPatientListView.setChangeNote("0");
                    }

                    dao.getListDrugCardDao().get(position).setComplete(isChecked ? "1" : "0");
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setCheckNote("0");
                    dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                    dao.getListDrugCardDao().get(position).setDescription("");
                    dao.getListDrugCardDao().get(position).setIdRadio(R.id.rdb1);
                    dao.getListDrugCardDao().get(position).setStrRadio("");
                    dao.getListDrugCardDao().get(position).setCheckType("Administration");

                }
            });

            ImageView imageViewNote = buildDrugForPatientListView.imageViewNote(statusPatient, dao.getListDrugCardDao().get(position).getComplete());
            imageViewNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drugNoteDialog(position);
                }
            });

//        }

        if(position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(viewGroup.getContext(),
                    R.anim.up_from_bottom);
            buildDrugForPatientListView.startAnimation(anim);
            lastPosition = position;
        }

        return buildDrugForPatientListView;
    }

    private void drugNoteDialog(final int position) {

        final AlertDialog mainClose;
        AlertDialog.Builder mainBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_administration, null);

        tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
        chkHold = (CheckBox) dialogView.findViewById(R.id.chkHold);
        txtStatusHoldBP = (EditText) dialogView.findViewById(R.id.txtStatusHoldBP);
        txtStatusHoldHR = (EditText) dialogView.findViewById(R.id.txtStatusHoldHR);
        txtStatusHoldCBG = (EditText) dialogView.findViewById(R.id.txtStatusHoldCBG);
        txtStatus = (EditText) dialogView.findViewById(R.id.txtStatus);
        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup);
        tvCancel = (TextView) dialogView.findViewById(R.id.tvCancel);
        tvSave = (TextView) dialogView.findViewById(R.id.tvSave);

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

        /////////////////////
        //start
        ////////////////////
        if(statusPatient != null & dao.getListDrugCardDao().get(position).getComplete() != null){
            if(statusPatient.equals("1") & dao.getListDrugCardDao().get(position).getComplete().equals("1")){
                chkHold.setEnabled(false);
                txtStatusHoldBP.setEnabled(false);
                txtStatusHoldHR.setEnabled(false);
                txtStatusHoldCBG.setEnabled(false);
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    radioGroup.getChildAt(i).setEnabled(false);
                }
                txtStatus.setEnabled(false);
                tvSave.setEnabled(false);
            }
            else{
                if(statusPatient.equals("0") & dao.getListDrugCardDao().get(position).getComplete().equals("1")){
                    dao.getListDrugCardDao().get(position).setComplete("1");
                    chkHold.setEnabled(false);
                    txtStatusHoldBP.setEnabled(false);
                    txtStatusHoldHR.setEnabled(false);
                    txtStatusHoldCBG.setEnabled(false);
                    txtStatus.setEnabled(false);
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        radioGroup.getChildAt(i).setEnabled(false);
                    }
                    tvSave.setEnabled(false);
                }
            }
        }
        /////////////////////
        //stop
        ////////////////////

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


        mainBuilder.setView(dialogView);
        mainBuilder.create();
        mainClose = mainBuilder.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("คุณต้องการจะยกเลิกใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainClose.dismiss();
                    }
                });
                builder.setNegativeButton("ไม่ใช่", null);
                builder.create();
                builder.show();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("คุณต้องการจะบันทึกข้อมูลใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton)dialogView.findViewById(selectedId);
                        dao.getListDrugCardDao().get(position).setDescription(txtStatus.getText().toString());
                        dao.getListDrugCardDao().get(position).setCheckType("Administration");
                        if(chkHold.isChecked() == true) {
//                            buildDrugForPatientListView.setChangeNote("1");
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
//                                    buildDrugForPatientListView.setChangeNote("1");
                                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                                    dao.getListDrugCardDao().get(position).setComplete("0");
                                    dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                                    dao.getListDrugCardDao().get(position).setStrRadio("NPO");
                                }
                                else if (radioButton.getId() == R.id.rdb3){
//                                    buildDrugForPatientListView.setChangeNote("1");
                                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                                    dao.getListDrugCardDao().get(position).setComplete("0");
                                    dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                                    dao.getListDrugCardDao().get(position).setStrRadio("คนไข้ปฏิเสธ");
                                }
                                else if (radioButton.getId() == R.id.rdb4){
//                                    buildDrugForPatientListView.setChangeNote("1");
                                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                                    dao.getListDrugCardDao().get(position).setComplete("0");
                                    dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                                    dao.getListDrugCardDao().get(position).setStrRadio("คนไข้มีอาการคลื่นไส้อาเจียน");
                                }
                                else if (radioButton.getId() == R.id.rdb5){
//                                    buildDrugForPatientListView.setChangeNote("1");
                                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                                    dao.getListDrugCardDao().get(position).setComplete("0");
                                    dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                                    dao.getListDrugCardDao().get(position).setStrRadio("แทงเส้นเลือดไม่ได้");
                                }
                                else if (radioButton.getId() == R.id.rdb6){
//                                    buildDrugForPatientListView.setChangeNote("1");
                                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                                    dao.getListDrugCardDao().get(position).setComplete("0");
                                    dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                                    dao.getListDrugCardDao().get(position).setStrRadio("แพทย์สั่ง off ณ เวลานั้น");
                                }
                                else {
//                                    buildDrugForPatientListView.setChangeNote("1");
                                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                                    dao.getListDrugCardDao().get(position).setComplete("0");
                                    dao.getListDrugCardDao().get(position).setIdRadio(radioButton.getId());
                                    dao.getListDrugCardDao().get(position).setStrRadio("ผู้ป่วยไปทำหัตการ");
                                }
                            }
                            if(!dao.getListDrugCardDao().get(position).getDescription().equals("")){
//                                buildDrugForPatientListView.setChangeNote("1");
                                dao.getListDrugCardDao().get(position).setCheckNote("1");
                                dao.getListDrugCardDao().get(position).setComplete("0");
                            }
                        }
                        mainClose.dismiss();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("ไม่ใช่", null);
                builder.create();
                builder.show();
            }
        });
    }
}
