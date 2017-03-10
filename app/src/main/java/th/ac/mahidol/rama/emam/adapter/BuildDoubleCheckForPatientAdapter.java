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
import android.widget.TextView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildDoubleCheckForPatientListView;

public class BuildDoubleCheckForPatientAdapter extends BaseAdapter{
    private Context context;
    private ListDrugCardDao dao;
    private EditText txtStatus, txtStatusSize, txtStatusForget, txtStatusType;
    private CheckBox chkSize, chkForget, chkType;
    private TextView tvDrugName, tvCancel, tvSave;
    private String statusPatient;
    private BuildDoubleCheckForPatientListView buildDoubleCheckForPatientListView;
    int lastPosition = -1;


    public void setDao(Context context, ListDrugCardDao dao, String statusPatient){
        this.dao = dao;
        this.context = context;
        this.statusPatient = statusPatient;
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
//            buildDoubleCheckForPatientListView = (BuildDoubleCheckForPatientListView) convertView;
//        else {
            buildDoubleCheckForPatientListView = new BuildDoubleCheckForPatientListView(viewGroup.getContext());
            buildDoubleCheckForPatientListView.setDrugName(dao.getListDrugCardDao().get(position), statusPatient);

            CheckBox checkBox = buildDoubleCheckForPatientListView.isCheck();

            /////////////////////
            //start
            ////////////////////
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
            /////////////////////
            //stop
            ////////////////////

            checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete() != null ? dao.getListDrugCardDao().get(position).getComplete().equals("1") ? true : false : false);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    /////////////////////
                    //start
                    ////////////////////
                    if (isChecked == true) {
                        buildDoubleCheckForPatientListView.setChangeNote("0");
                    }
                    /////////////////////
                    //stop
                    ////////////////////
                    dao.getListDrugCardDao().get(position).setComplete(isChecked ? "1" : "0");
                    dao.getListDrugCardDao().get(position).setStatus("normal");
                    dao.getListDrugCardDao().get(position).setCheckNote("0");
                    dao.getListDrugCardDao().get(position).setDescriptionTemplate("");
                    dao.getListDrugCardDao().get(position).setDescription("");
                    dao.getListDrugCardDao().get(position).setStrRadio("");
                    dao.getListDrugCardDao().get(position).setCheckType("Second Check");
                    dao.getListDrugCardDao().get(position).setStrType(null);
                    dao.getListDrugCardDao().get(position).setStrSize(null);
                    dao.getListDrugCardDao().get(position).setStrForget(null);
                }
            });

            ImageView imageViewNote = buildDoubleCheckForPatientListView.imageViewNote(statusPatient, dao.getListDrugCardDao().get(position).getComplete());
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
            buildDoubleCheckForPatientListView.startAnimation(anim);
            lastPosition = position;
        }

        return buildDoubleCheckForPatientListView;
    }

    private void drugNoteDialog(final int position) {

        final AlertDialog mainClose;
        AlertDialog.Builder mainBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_doublecheck, null);

        tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
        chkSize = (CheckBox) dialogView.findViewById(R.id.chkSize);
        chkForget = (CheckBox) dialogView.findViewById(R.id.chkForget);
        chkType = (CheckBox) dialogView.findViewById(R.id.chkType);
        txtStatusSize = (EditText) dialogView.findViewById(R.id.txtStatusSize);
        txtStatusForget = (EditText) dialogView.findViewById(R.id.txtStatusForget);
        txtStatusType = (EditText) dialogView.findViewById(R.id.txtStatusType);
        txtStatus = (EditText) dialogView.findViewById(R.id.txtStatus);
        tvCancel = (TextView) dialogView.findViewById(R.id.tvCancel);
        tvSave = (TextView) dialogView.findViewById(R.id.tvSave);

        tvDrugName.setText(" " + dao.getListDrugCardDao().get(position).getTradeName() +
                " " + dao.getListDrugCardDao().get(position).getDose() +
                " " + dao.getListDrugCardDao().get(position).getUnit());

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


        /////////////////////
        //start
        ////////////////////

        if(statusPatient != null & dao.getListDrugCardDao().get(position).getComplete() != null){
            if(statusPatient.equals("1") & dao.getListDrugCardDao().get(position).getComplete().equals("1")){
                dao.getListDrugCardDao().get(position).setComplete("1");
                chkType.setEnabled(false);
                chkForget.setEnabled(false);
                chkSize.setEnabled(false);
                txtStatusType.setEnabled(false);
                txtStatusForget.setEnabled(false);
                txtStatusSize.setEnabled(false);
                txtStatus.setEnabled(false);
                tvSave.setEnabled(false);
            }
            else{
                if(statusPatient.equals("0") & dao.getListDrugCardDao().get(position).getComplete().equals("1")){
                    dao.getListDrugCardDao().get(position).setComplete("1");
                    chkType.setEnabled(false);
                    chkForget.setEnabled(false);
                    chkSize.setEnabled(false);
                    txtStatusType.setEnabled(false);
                    txtStatusForget.setEnabled(false);
                    txtStatusSize.setEnabled(false);
                    txtStatus.setEnabled(false);
                    tvSave.setEnabled(false);
                }
            }
        }

        /////////////////////
        //stop
        ////////////////////


        if(dao.getListDrugCardDao().get(position).getStatus() != null){
            if(dao.getListDrugCardDao().get(position).getStrType() != null){
                chkType.setChecked(true);
                txtStatusType.setEnabled(true);
                txtStatusType.setText(dao.getListDrugCardDao().get(position).getStrType());
            }
            if(dao.getListDrugCardDao().get(position).getStrSize() != null){
                chkSize.setChecked(true);
                txtStatusSize.setEnabled(true);
                txtStatusSize.setText(dao.getListDrugCardDao().get(position).getStrSize());
            }
            if(dao.getListDrugCardDao().get(position).getStrForget() != null){
                chkForget.setChecked(true);
                txtStatusForget.setEnabled(true);
                txtStatusForget.setText(dao.getListDrugCardDao().get(position).getStrForget());
            }
        }

        if(dao.getListDrugCardDao().get(position).getDescription() != null){
            dao.getListDrugCardDao().get(position).setComplete("0");
            txtStatus.setText(dao.getListDrugCardDao().get(position).getDescription());
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
                        dao.getListDrugCardDao().get(position).setDescription(txtStatus.getText().toString());
                        dao.getListDrugCardDao().get(position).setCheckType("Second Check");
                        if(chkType.isChecked() == true){
                            dao.getListDrugCardDao().get(position).setStrType(txtStatusType.getText().toString());
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setStatus("normal");
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                        }
                        if(chkSize.isChecked() == true){
                            dao.getListDrugCardDao().get(position).setStrSize(txtStatusSize.getText().toString());
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setStatus("normal");
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                        }
                        if(chkForget.isChecked() == true){
                            dao.getListDrugCardDao().get(position).setStrForget(txtStatusForget.getText().toString());
                            dao.getListDrugCardDao().get(position).setComplete("0");
                            dao.getListDrugCardDao().get(position).setStatus("normal");
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                        }
                        if(chkType.isChecked() == false)
                            dao.getListDrugCardDao().get(position).setStrType(null);

                        if(chkSize.isChecked() == false)
                            dao.getListDrugCardDao().get(position).setStrSize(null);

                        if(chkForget.isChecked() == false)
                            dao.getListDrugCardDao().get(position).setStrForget(null);

                        if(!dao.getListDrugCardDao().get(position).getDescription().equals("")){
                            dao.getListDrugCardDao().get(position).setCheckNote("1");
                            dao.getListDrugCardDao().get(position).setStatus("normal");
                            dao.getListDrugCardDao().get(position).setComplete("0");
                        }
                        if((chkType.isChecked() == false)&(chkSize.isChecked() == false)&(chkForget.isChecked() == false) & dao.getListDrugCardDao().get(position).getDescription().equals("")){
                            dao.getListDrugCardDao().get(position).setCheckNote("0");
                            dao.getListDrugCardDao().get(position).setStatus("normal");
                            dao.getListDrugCardDao().get(position).setComplete("1");
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
