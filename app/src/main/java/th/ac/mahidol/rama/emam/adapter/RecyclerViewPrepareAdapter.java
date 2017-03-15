package th.ac.mahidol.rama.emam.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;

public class RecyclerViewPrepareAdapter extends RecyclerView.Adapter{
    private ListDrugCardDao dao;
    private Context context;
    private EditText txtStatus, txtStatusHold;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox chkHold;
    private TextView tvDrugName, tvCancel, tvSave;
    private String statusPatient;
    private List<String> listHAD;
    int lastPosition = -1;

    @Override
    public ViewHolderPrepare onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_list_drug_for_patient, viewGroup, false);
        return new ViewHolderPrepare(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (dao == null)
            return 0;
        if (dao.getListDrugCardDao() == null)
            return 0;
        return dao.getListDrugCardDao().size();
    }


    public static class ViewHolderPrepare extends RecyclerView.ViewHolder{

        private TextView tvDrugName, tvDosage, tvType, tvRoute, tvFrequency, tvSite, tvComplete;
        private CheckBox chkCheckDrug;
        private ImageView imgvNote, ivPhotoMed;
        private LinearLayout bg;

        public ViewHolderPrepare(View v) {
            super(v);
            initInstances(v);
        }

        private void initInstances(View v){
            bg = (LinearLayout) v.findViewById(R.id.bg);
            tvDrugName = (TextView) v.findViewById(R.id.tvDrugName);
            tvDosage = (TextView) v.findViewById(R.id.tvDosage);
            tvType = (TextView) v.findViewById(R.id.tvType);
            tvRoute = (TextView) v.findViewById(R.id.tvRoute);
            tvFrequency = (TextView) v.findViewById(R.id.tvFrequency);
            tvSite = (TextView) v.findViewById(R.id.tvSite);
            chkCheckDrug = (CheckBox) v.findViewById(R.id.chkCheckDrug);
            imgvNote = (ImageView) v.findViewById(R.id.imgvNote);
            tvComplete = (TextView) v.findViewById(R.id.tvComplete);
            ivPhotoMed = (ImageView) v.findViewById(R.id.ivPhotoMed);
        }
    }
}
