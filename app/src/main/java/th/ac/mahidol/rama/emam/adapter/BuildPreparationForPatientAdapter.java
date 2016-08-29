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
        Log.d("check", "setDao "+dao);

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
        final CheckBox checkBox = buildPreparationForPatientListView.isCheck();

        checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete()!= null ? dao.getListDrugCardDao().get(position).getComplete().equals("0")?true : false : false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                dao.getListDrugCardDao().get(position).setComplete( b ? "0":"-1");

            }
        });

        ImageView imageViewNote = buildPreparationForPatientListView.imageViewNote();
        imageViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check", "drugNoteDialog = "+ position);
                drugNoteDialog(position);
            }
        });


        return buildPreparationForPatientListView;
    }

    private void drugNoteDialog(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custom_dialog_prepare, null);
        dialogView.setBackgroundResource(R.color.colorLemonChiffon);

        tvDrugName = (TextView)dialogView.findViewById(R.id.tvDrugName);
        chkHold = (CheckBox)dialogView.findViewById(R.id.chkHold);
        txtStatusHold = (EditText)dialogView.findViewById(R.id.txtStatusHold);
        txtStatus = (EditText)dialogView.findViewById(R.id.txtStatus);
        radioGroup = (RadioGroup)dialogView.findViewById(R.id.radiogroup);

        tvDrugName.setText(" "+dao.getListDrugCardDao().get(position).getTradeName()
                + " " + dao.getListDrugCardDao().get(position).getDose()
                + " "+dao.getListDrugCardDao().get(position).getUnit());


        chkHold.setChecked( dao.getListDrugCardDao().get(position).getComplete()!=null ? dao.getListDrugCardDao().get(position).getComplete().equals("1")? true : false : true );
        

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
                if(radioButton.isChecked()) {
                    if (radioButton.getId() == R.id.rdb1 ) {
//                        isCheckNoteRadio.set(position, radioButton.getId());
                        Log.d("check", "radioButton1 = "+radioButton.getId());


                    }else{
//                        isCheckNoteRadio.set(position, radioButton.getId());
                        Log.d("check", "radioButton = "+radioButton.getId());

                        notifyDataSetChanged();
                    }
                }
            }
        });
        builder.setNegativeButton("ยกเลิก",null);

        builder.create();
        builder.show();
    }
}
