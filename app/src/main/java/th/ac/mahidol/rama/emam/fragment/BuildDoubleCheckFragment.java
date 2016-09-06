package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.BuildDoubleCheckAdapter;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildDoubleCheckFragment extends Fragment{

    private String nfcUID, sdlocID, wardName, time, RFID, firstName, lastName;
    private ListView listView;
    private TextView tvPreparation, tvUserName, tvTime;
    private BuildDoubleCheckAdapter buildDoubleCheckAdapter;
    private int timeposition, tricker = 0;
    private Button btnLogin;
    private ListDrugCardDao listDrugCardDao;


    public BuildDoubleCheckFragment() {
        super();
    }

    public static BuildDoubleCheckFragment newInstance(String nfcUID, String sdlocID, String wardName, int timeposition, String time) {
        BuildDoubleCheckFragment fragment = new BuildDoubleCheckFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", timeposition);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doublecheck, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("position");
        time = getArguments().getString("time");

        Log.d("check", "BuildDoubleCheckFragment initInstances = " + nfcUID +" / "+ sdlocID + " / " + wardName + " / " + time);

        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        tvTime.setText(getArguments().getString("time"));
        btnLogin.setText("ลงชื่อเข้าใช้ด้วย Username และ Password");


        listView = (ListView) rootView.findViewById(R.id.lvPatientDoubleAdapter);
        buildDoubleCheckAdapter = new BuildDoubleCheckAdapter();

        if(nfcUID != null) {
            loadPersonWard(nfcUID, sdlocID);
        }
        else {
            loadPatientData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadPatientData(){
        SharedPreferences prefs = getContext().getSharedPreferences("patientintdata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientintdata",null);
        if(data != null){
            ListPatientDataDao dao = new Gson().fromJson(data,ListPatientDataDao.class);
            Log.d("check", "ListPatientDataDao = "+ dao);//จะต้องเป็นคนที่ผ่านการเตรียมยาเรียบร้อยแล้วเท่านั้น
            buildDoubleCheckAdapter.setDao(dao, tricker);
            listView.setAdapter(buildDoubleCheckAdapter);
        }
    }

    private void loadPersonWard(String nfcUID, String sdlocID){
        Log.d("check", "BuildDoubleCheckFragment loadPersonWard = "+ nfcUID + " / " + sdlocID);
        Call<CheckPersonWardDao> call = HttpManager.getInstance().getService().getPersonWard(nfcUID, sdlocID);
        call.enqueue(new PersonWardLoadCallback());

    }




    class PersonWardLoadCallback implements Callback<CheckPersonWardDao> {

        @Override
        public void onResponse(Call<CheckPersonWardDao> call, Response<CheckPersonWardDao> response) {
            CheckPersonWardDao dao = response.body();
            Log.d("check", "CheckPersonWardDao = "+dao.getRFID()+" "+dao.getFirstName()+" "+dao.getLastName());
            RFID = dao.getRFID();
            firstName = dao.getFirstName();
            lastName = dao.getLastName();
            tvUserName.setText("ตรวจสอบยาโดย  " + firstName + " " + lastName);
        }

        @Override
        public void onFailure(Call<CheckPersonWardDao> call, Throwable t) {
            Log.d("check", "BuildDoubleCheckFragment PersonWardLoadCallback Failure " + t);
        }
    }
}
