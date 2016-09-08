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
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildDoubleCheckForPatientListView;

public class BuildDoubleCheckForPatientAdapter extends BaseAdapter{
    private Context context;
    private ListDrugCardDao dao;
    private EditText txtStatus, txtStatusSize, txtStatusForget, txtStatusType;
    private CheckBox chkSize, chkForget, chkType;
    private TextView tvDrugName;
    private BuildDoubleCheckForPatientListView buildDoubleCheckForPatientListView;


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
        buildDoubleCheckForPatientListView = new BuildDoubleCheckForPatientListView(viewGroup.getContext());
        buildDoubleCheckForPatientListView.setDrugName(dao.getListDrugCardDao().get(position));

        CheckBox checkBox = buildDoubleCheckForPatientListView.isCheck();
        checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete()!= null ? dao.getListDrugCardDao().get(position).getComplete().equals("1")?true : false : false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dao.getListDrugCardDao().get(position).setComplete(isChecked ? "1":"0");
                dao.getListDrugCardDao().get(position).setStatus("normal");
                dao.getListDrugCardDao().get(position).setCheckNote("0");
                dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                dao.getListDrugCardDao().get(position).setDescription("");
                dao.getListDrugCardDao().get(position).setStrRadio("");
                dao.getListDrugCardDao().get(position).setCheckType("Second Check");
            }
        });

        ImageView imageViewNote = buildDoubleCheckForPatientListView.imageViewNote();
        imageViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drugNoteDialog(position);
            }
        });


        return buildDoubleCheckForPatientListView;
    }

    private void drugNoteDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_doublecheck, null);
        dialogView.setBackgroundResource(R.color.colorLemonChiffon);

        tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
        chkSize = (CheckBox) dialogView.findViewById(R.id.chkSize);
        chkForget = (CheckBox) dialogView.findViewById(R.id.chkForget);
        chkType = (CheckBox) dialogView.findViewById(R.id.chkType);
        txtStatusSize = (EditText) dialogView.findViewById(R.id.txtStatusSize);
        txtStatusForget = (EditText) dialogView.findViewById(R.id.txtStatusForget);
        txtStatusType = (EditText) dialogView.findViewById(R.id.txtStatusType);
        txtStatus = (EditText) dialogView.findViewById(R.id.txtStatus);

        tvDrugName.setText(" " + dao.getListDrugCardDao().get(position).getTradeName() + " " + dao.getListDrugCardDao().get(position).getDose() + " " + dao.getListDrugCardDao().get(position).getUnit());
        chkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkType.isChecked() == true) {
                    txtStatusType.setEnabled(true);
                    dao.getListDrugCardDao().get(position).setComplete("0");
                } else {
                    txtStatusType.setEnabled(false);
                    txtStatusType.setText("");
                }
            }
        });
        chkForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkForget.isChecked() == true) {
                    txtStatusForget.setEnabled(true);
                    dao.getListDrugCardDao().get(position).setComplete("0");
                } else {
                    txtStatusForget.setEnabled(false);
                    txtStatusForget.setText("");
                }
            }
        });
        chkSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkSize.isChecked() == true) {
                    txtStatusSize.setEnabled(true);
                    dao.getListDrugCardDao().get(position).setComplete("0");
                } else {
                    txtStatusSize.setEnabled(false);
                    txtStatusSize.setText("");
                }
            }
        });

        Log.d("check", "aaaaaa = "+dao.getListDrugCardDao().get(position).getDescriptionTemplate());
        if(dao.getListDrugCardDao().get(position).getStatus() != null){
            if(dao.getListDrugCardDao().get(position).getStatus().equals("normal")) {
                dao.getListDrugCardDao().get(position).setComplete("0");
                Log.d("check", "CHECK = "+chkType.isChecked()+chkType.isChecked()+chkType.isChecked());
                if(!txtStatusType.getText().equals("")) {
                    chkType.setChecked(true);
                    txtStatusType.setEnabled(true);
                    txtStatusType.setText(dao.getListDrugCardDao().get(position).getDescriptionTemplate());
                }
//                    chkSize.setChecked(true);
//                    txtStatusSize.setEnabled(true);
//                    txtStatusSize.setText(dao.getListDrugCardDao().get(position).getDescriptionTemplate());
//
//                    chkForget.setChecked(true);
//                    txtStatusForget.setEnabled(true);
//                    txtStatusForget.setText(dao.getListDrugCardDao().get(position).getDescriptionTemplate());

            }
        }

        if(dao.getListDrugCardDao().get(position).getDescription() != null){
            dao.getListDrugCardDao().get(position).setComplete("0");
            txtStatus.setText(dao.getListDrugCardDao().get(position).getDescription());
        }

        builder.setView(dialogView);
        builder.setTitle("บันทึกข้อความสำหรับตรวจสอบยา");
        builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.getListDrugCardDao().get(position).setDescription(txtStatus.getText().toString());
                dao.getListDrugCardDao().get(position).setCheckType("Second Check");
                dao.getListDrugCardDao().get(position).setDescriptionTemplate("1:"+txtStatusType.getText().toString() +",2:"+txtStatusForget.getText().toString()+",3:"+txtStatusForget.getText().toString());
                if(chkType.isChecked() == true){
//                    dao.getListDrugCardDao().get(position).setDescriptionTemplate(txtStatusType.getText().toString());
                    dao.getListDrugCardDao().get(position).setComplete("0");
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                }
                if(chkSize.isChecked() == true){
//                    dao.getListDrugCardDao().get(position).setDescriptionTemplate(txtStatusSize.getText().toString());
                    dao.getListDrugCardDao().get(position).setComplete("0");
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                }
                if(chkForget.isChecked() == true){
//                    dao.getListDrugCardDao().get(position).setDescriptionTemplate(txtStatusForget.getText().toString());
                    dao.getListDrugCardDao().get(position).setComplete("0");
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                }
                if(!dao.getListDrugCardDao().get(position).getDescription().equals("")){
                    dao.getListDrugCardDao().get(position).setCheckNote("1");
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setComplete("0");
                }
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("ยกเลิก",null);
        builder.create();
        builder.show();
    }
}
