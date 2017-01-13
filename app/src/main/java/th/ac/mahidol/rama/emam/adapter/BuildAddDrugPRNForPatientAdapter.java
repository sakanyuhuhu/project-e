package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CheckLastPRNDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CheckLastPRNListDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.view.BuildAddDrugPRNForPatientListView;

public class BuildAddDrugPRNForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;
    private String mrn;
    private int drugPosition;
    private TextView tvDrugName;
    private CheckLastPRNListDao checkLastPRNListDao;
    private BuildAddDrugPRNForPatientListView buildAddDrugPRNForPatientListView;


    public void setDao(Context context, ListDrugCardDao dao, String mrn){
        this.dao = dao;
        this.context = context;
        this.mrn = mrn;
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
        buildAddDrugPRNForPatientListView = new BuildAddDrugPRNForPatientListView(viewGroup.getContext());
        buildAddDrugPRNForPatientListView.setDrugPRNName(dao.getListDrugCardDao().get(position));

        CheckBox checkBox = buildAddDrugPRNForPatientListView.isCheck();
        checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete()!= null ? dao.getListDrugCardDao().get(position).getComplete().equals("1")?true : false : false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dao.getListDrugCardDao().get(position).setComplete(isChecked ? "1":"0");
                checkLastPRNbyMRNDrugID(position);
            }
        });

        notifyDataSetChanged();
        return buildAddDrugPRNForPatientListView;
    }

    private void checkLastPRNbyMRNDrugID(int position){
        drugPosition = position;
        Call<CheckLastPRNListDao> call = HttpManager.getInstance().getService().getCheckLastPRNbyMRNDrugID(mrn, dao.getListDrugCardDao().get(position).getDrugID());
        call.enqueue(new CheckLastPRNbyMRNDrugIDLoadCallback());
    }


    class CheckLastPRNbyMRNDrugIDLoadCallback implements Callback<CheckLastPRNListDao> {
        @Override
        public void onResponse(Call<CheckLastPRNListDao> call, Response<CheckLastPRNListDao> response) {
            checkLastPRNListDao = response.body();
            if(checkLastPRNListDao != null) {
                for (CheckLastPRNDao chPRNLast : checkLastPRNListDao.getCheckLastPRNDao()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater.inflate(R.layout.custom_dialog_lastprn, null);
                    tvDrugName = (TextView) dialogView.findViewById(R.id.tvDrugName);
                    tvDrugName.setText(Html.fromHtml("<b>"+dao.getListDrugCardDao().get(drugPosition).getTradeName()+"</b><br> มีการเตรียมยาล่าสุดวันที่: <b>"+chPRNLast.getDrugUseDate()+"</b><br> เวลาล่าสุด: <b>"+chPRNLast.getActivityHour()+":00 น."+
                            "</b><br>"+"  โดย: <b>"+chPRNLast.getFirstName()+" "+chPRNLast.getLastName()+"</b>"));
                    builder.setView(dialogView);
                    builder.create();
                    builder.show();
                }
            }
        }

        @Override
        public void onFailure(Call<CheckLastPRNListDao> call, Throwable t) {

        }
    }
}
