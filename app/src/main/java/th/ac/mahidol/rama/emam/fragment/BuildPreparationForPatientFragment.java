package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildPreparationForPatientAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.view.BuildHeaderPatientDataView;

public class BuildPreparationForPatientFragment extends Fragment {
    private String nfcUID, sdlocID, strdate, time;
    private int position, yearNow;
    private ListView listView;
    private TextView tvDate, tvTime, tvDrugAllergy;
    private Date date;
    private Button btnCancel, btnSave;
    private BuildHeaderPatientDataView buildHeaderPatientDataView;
    private BuildPreparationForPatientAdapter buildPreparationForPatientAdapter;

    public BuildPreparationForPatientFragment() {
        super();
    }

    public static BuildPreparationForPatientFragment newInstance(String nfcUId, String sdlocId, int position, String time) {
        BuildPreparationForPatientFragment fragment = new BuildPreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putInt("position", position);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        position = getArguments().getInt("position");
        time = getArguments().getString("time");

        listView = (ListView) rootView.findViewById(R.id.lvPrepareForPatientAdapter);
        buildHeaderPatientDataView = (BuildHeaderPatientDataView)rootView.findViewById(R.id.headerPatientAdapter);
        buildPreparationForPatientAdapter = new BuildPreparationForPatientAdapter();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        strdate = simpleDateFormat.format(date);

        tvTime = (TextView) rootView.findViewById(R.id.tvTimer);
        tvDate = (TextView) rootView.findViewById(R.id.tvDate);
        tvDrugAllergy = (TextView) rootView.findViewById(R.id.tvDrugAllergy);
        tvDate.setText("  " + strdate );
        tvTime.setText(time);
        tvDrugAllergy.setText("   การแพ้ยา:แตะเพื่อดูข้อมูล");

        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data,ListPatientDataDao.class);
            Log.d("check", "dao size = "+listPatientDataDao.getPatientDao().size()+ " position = "+position);
            buildHeaderPatientDataView.setData(listPatientDataDao.getPatientDao().get(position).getBedID(),listPatientDataDao.getPatientDao().get(position).getInitialName()+
                    listPatientDataDao.getPatientDao().get(position).getFirstName()+" "+listPatientDataDao.getPatientDao().get(position).getLastName(),
                    listPatientDataDao.getPatientDao().get(position).getIdCardNo(), listPatientDataDao.getPatientDao().get(position).getMRN(),
                    listPatientDataDao.getPatientDao().get(position).getGender(), listPatientDataDao.getPatientDao().get(position).getDob(),
                    listPatientDataDao.getPatientDao().get(position).getMaritalstatus());

            DrugCardDao drugCardDao = new DrugCardDao();
            drugCardDao.setId(0);
            drugCardDao.setAdminTimeHour(String.valueOf(position));
            drugCardDao.setDrugUseDate(strdate);
            drugCardDao.setMRN(listPatientDataDao.getPatientDao().get(position).getMRN());

            loadMedicalData(drugCardDao);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void saveCache(ListDrugCardDao listDrugCardDao){
        String json = new Gson().toJson(listDrugCardDao);
        SharedPreferences prefs = getContext().getSharedPreferences("medicaldataperson", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("medicaldataperson",json);
        editor.apply();
    }

    private void loadMedicalData(DrugCardDao drugCardDao){
        Call<ListDrugCardDao> call = HttpManager.getInstance().getService().getDrugData(drugCardDao);
        call.enqueue(new DrugLoadCallback());
    }

    class DrugLoadCallback implements Callback<ListDrugCardDao>{

        @Override
        public void onResponse(Call<ListDrugCardDao> call, Response<ListDrugCardDao> response) {
            ListDrugCardDao dao = response.body();
//            saveCache(dao);
            Log.d("check", "DAO MED = " + dao.getListDrugCardDao().size());
            buildPreparationForPatientAdapter.setDao(dao);
            listView.setAdapter(buildPreparationForPatientAdapter);
        }

        @Override
        public void onFailure(Call<ListDrugCardDao> call, Throwable t) {
            Log.d("check", "BuildPreparationForPatientFragment Failure " + t);
        }
    }

}
