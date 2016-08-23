package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildstatusCheckDAO.ListStatusCheckDao;
import th.ac.mahidol.rama.emam.dao.buildstatusCheckDAO.StatusCheckDao;
import th.ac.mahidol.rama.emam.view.BuildPreparationForPatientListView;

public class BuildPreparationForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;
    private ListStatusCheckDao daoCheck;
    private List<Boolean> isCheck;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName;


    public BuildPreparationForPatientAdapter() {
        SharedPreferences prefs = context.getSharedPreferences("DrugStatus", Context.MODE_PRIVATE);
        String data = prefs.getString("DrugStatus",null);
        if(data == null)
            return;
        daoCheck = new Gson().fromJson(data,ListStatusCheckDao.class);
    }

    public void setDao(Context context, ListDrugCardDao dao){
        this.dao = dao;
        this.context = context;

        isCheck = new ArrayList<Boolean>();

        for(int i=0; i<dao.getListDrugCardDao().size();i++){
            isCheck.add(i, false);
        }

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
        BuildPreparationForPatientListView preparationForPatientListView = new BuildPreparationForPatientListView(viewGroup.getContext());
        preparationForPatientListView.setDrugNameBuild(dao.getListDrugCardDao().get(position));

        for(StatusCheckDao d : daoCheck.getStatusCheckDaoList()){
            if(dao.getListDrugCardDao().get(position).getDrugID().equals(d.getDrugID())){
                preparationForPatientListView.setCheck(d.isCheckDrug());
            }
        }
        preparationForPatientListView.setCheck(dao.getListDrugCardDao().get(position).getCheckDrug() !=null ? dao.getListDrugCardDao().get(position).getCheckDrug() : false);

        final CheckBox checkBox =  preparationForPatientListView.isCheck();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dao.getListDrugCardDao().get(position).setCheckDrug(b);
            }
        });

        ImageView imageViewNote = preparationForPatientListView.imageViewNote();
        imageViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.custom_dialog_prepare, null);
                dialogView.setBackgroundResource(R.color.colorLemonChiffon);

                tvDrugName = (TextView)dialogView.findViewById(R.id.tvDrugName);
                chkHold = (CheckBox)dialogView.findViewById(R.id.chkHold);
                txtStatusHold = (EditText)dialogView.findViewById(R.id.txtStatusHold);
                radioGroup = (RadioGroup)dialogView.findViewById(R.id.radiogroup);
                txtStatus = (EditText)dialogView.findViewById(R.id.txtStatus);

                tvDrugName.setText(" "+dao.getListDrugCardDao().get(position).getTradeName()
                       + " " + dao.getListDrugCardDao().get(position).getDose()
                       + " "+dao.getListDrugCardDao().get(position).getUnit());


                builder.setView(dialogView);
                builder.setTitle("บันทึกข้อความสำหรับเตรียมยา");
                builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
