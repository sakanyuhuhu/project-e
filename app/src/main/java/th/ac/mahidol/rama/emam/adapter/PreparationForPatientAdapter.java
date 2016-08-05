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
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.view.PreparationForPatientListView;

public class PreparationForPatientAdapter extends BaseAdapter {
    private List<String> strDrugName, strDosage, unit, type, route, strFrequency, adminTime, site;
    private List<Boolean> isCheck = new ArrayList<Boolean>();
    private Context context;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private CheckBox chkHold;

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

        for (int i=0;i<strDrugName.size();i++){
            isCheck.add(i,false);
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

    public List getCheckStatus(){
        return isCheck;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custom_dialog_prepare, null);
                dialogView.setBackgroundResource(R.color.colorPeachPuff);

                chkHold = (CheckBox)dialogView.findViewById(R.id.chkHold);
                txtStatusHold = (EditText)dialogView.findViewById(R.id.txtStatusHold);
                radioGroup = (RadioGroup)dialogView.findViewById(R.id.radiogroup);
                txtStatus = (EditText)dialogView.findViewById(R.id.txtStatus);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId ) {
                        if(checkedId == R.id.rdb1){
                            Log.d("check", "rdb1 : "+checkedId);
                        } else if(checkedId == R.id.rdb2){
                            Log.d("check", "rdb2 : "+checkedId);
                        }else if(checkedId == R.id.rdb3){
                            Log.d("check", "rdb3 : "+checkedId);
                        }else if(checkedId == R.id.rdb4){
                            Log.d("check", "rdb4 : "+checkedId);
                        }else if(checkedId == R.id.rdb5){
                            Log.d("check", "rdb5 : "+checkedId);
                        }else{
                            Log.d("check", "rdb6 : "+checkedId);
                        }
                    }
                });

                builder.setView(dialogView);
                builder.setTitle("บันทึกข้อความสำหรับเตรียมยา");
                builder.setPositiveButton("บันทึก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("ยกเลิก", null);
                builder.create();
                builder.show();
            }
        });
        return preparationForPatientListView;
    }

}
