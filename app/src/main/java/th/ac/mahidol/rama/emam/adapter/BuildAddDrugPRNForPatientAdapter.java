package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.view.BuildAddDrugPRNForPatientListView;

public class BuildAddDrugPRNForPatientAdapter extends BaseAdapter {
    private Context context;
    private ListDrugCardDao dao;

    private BuildAddDrugPRNForPatientListView buildAddDrugPRNForPatientListView;


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
        buildAddDrugPRNForPatientListView = new BuildAddDrugPRNForPatientListView(viewGroup.getContext());
        buildAddDrugPRNForPatientListView.setDrugPRNName(dao.getListDrugCardDao().get(position));

        CheckBox checkBox = buildAddDrugPRNForPatientListView.isCheck();
        checkBox.setChecked(dao.getListDrugCardDao().get(position).getComplete()!= null ? dao.getListDrugCardDao().get(position).getComplete().equals("1")?true : false : false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dao.getListDrugCardDao().get(position).setComplete(isChecked ? "1":"0");
            }
        });
        notifyDataSetChanged();
        return buildAddDrugPRNForPatientListView;
    }
}
