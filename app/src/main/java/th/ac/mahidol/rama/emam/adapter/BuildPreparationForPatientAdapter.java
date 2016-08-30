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
import th.ac.mahidol.rama.emam.view.BuildPreparationForPatientListView;

public class BuildPreparationForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName;


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
        final BuildPreparationForPatientListView buildPreparationForPatientListView = new BuildPreparationForPatientListView(viewGroup.getContext());
        buildPreparationForPatientListView.setDrugName(dao.getListDrugCardDao().get(position));



        buildPreparationForPatientListView.setCheck(dao.getListDrugCardDao().get(position).getCheckDrug() != null ? dao.getListDrugCardDao().get(position).getCheckDrug() : false);
        CheckBox checkBox = buildPreparationForPatientListView.isCheck();
        checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete()!= null ? dao.getListDrugCardDao().get(position).getComplete().equals("1")?true : false : false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dao.getListDrugCardDao().get(position).setComplete( b ? "1":"0");
                dao.getListDrugCardDao().get(position).setStatus(null);
                dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                dao.getListDrugCardDao().get(position).setDescription("");
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

        tvDrugName.setText(" " + dao.getListDrugCardDao().get(position).getTradeName() + " " + dao.getListDrugCardDao().get(position).getDose() + " " + dao.getListDrugCardDao().get(position).getUnit());

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
                Log.d("check", "chkHold = " + chkHold.isChecked() + " /txtStatusHold = " + txtStatusHold.getText());
            }
            else{
//             เป็นของ dao.getListDrugCardDao().get(position).getStatus().equals("normal")   radio button
            }
        }

//        chkHold.setChecked(dao.getListDrugCardDao().get(position).getStatus()!=null ? dao.getListDrugCardDao().get(position).getComplete().equals("hold")? true : false : false);

//        if(dao.getListDrugCardDao().get(position).getStatus()!= null){
//            if(dao.getListDrugCardDao().get(position).getComplete().equals("hold")){
//                chkHold.setChecked(true);
//                txtStatusHold.setEnabled(true);
//            }else{
//                chkHold.setChecked(false);
//                txtStatusHold.setEnabled(false);
//            }
//
//        }

//        if(dao.getListDrugCardDao().get(position).getCheckDrug() == true)
//            isCheckNoteRadio.set(position, R.id.rdb1);
//
//        if(isCheckNoteRadio.get(position) == 0)
//            isCheckNoteRadio.add(position, radioGroup.getId()+1);
//
//        if(isCheckNoteRadio.get(position) != 0)
//            radioGroup.check(isCheckNoteRadio.get(position));

        builder.setView(dialogView);
        builder.setTitle("บันทึกข้อความสำหรับเตรียมยา");
        builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)dialogView.findViewById(selectedId);
                dao.getListDrugCardDao().get(position).setDescription(txtStatus.getText().toString());
                Log.d("check", "DialogInterface Complete = "+dao.getListDrugCardDao().get(position).getComplete());
                Log.d("check", "DialogInterface chkHold = "+chkHold.isChecked());
                Log.d("check", "txtStatus = "+dao.getListDrugCardDao().get(position).getDescription());
                if(chkHold.isChecked() == true) {
                    dao.getListDrugCardDao().get(position).setDescriptionTemplate(txtStatusHold.getText().toString());
                    Log.d("check", "DescriptionTemplate = "+ dao.getListDrugCardDao().get(position).getDescriptionTemplate());
                    if (radioButton.isChecked()) {
                        if (radioButton.getId() == R.id.rdb1) {
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setStatus("hold");
                        } else {
                            dao.getListDrugCardDao().get(position).setDescriptionTemplate(txtStatusHold.getText().toString());
                            Log.d("check", "DescriptionTemplate = "+ dao.getListDrugCardDao().get(position).getDescriptionTemplate());
                            dao.getListDrugCardDao().get(position).setStatus("hold");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            notifyDataSetChanged();
                        }
                    }
                } else{
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setComplete("1");
                }
            }
        });
        builder.setNegativeButton("ยกเลิก",null);
        builder.create();
        builder.show();
    }
}
