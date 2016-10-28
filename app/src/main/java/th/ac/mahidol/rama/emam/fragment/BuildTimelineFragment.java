package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AddPatientPRNActivity;
import th.ac.mahidol.rama.emam.activity.MainSelectMenuActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildTimelineAdapter;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildTimelineFragment extends Fragment {
    private String nfcUID, sdlocID, wardName, focustimer;
    private String[] listTimeline = {"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "0:00", "1:00", "2:00"};
    private ListView listView;
    private BuildTimelineAdapter buildTimelineAdapter;
    private String currentTime[];

    public BuildTimelineFragment() {
        super();
    }

    public static BuildTimelineFragment newInstance(String nfcUID, String sdlocID, String wardName) {
        BuildTimelineFragment fragment = new BuildTimelineFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");

        listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        buildTimelineAdapter = new BuildTimelineAdapter();
        currentTime = DateFormat.getTimeInstance().format(new Date()).split(":");

        if(currentTime[0].equals("00")|currentTime[0].equals("01")|currentTime[0].equals("02")|currentTime[0].equals("03")|currentTime[0].equals("04")|currentTime[0].equals("05")|
                currentTime[0].equals("06")|currentTime[0].equals("07")|currentTime[0].equals("08")|currentTime[0].equals("09"))
            focustimer = currentTime[0].substring(1);
        else
            focustimer = currentTime[0];

        loadTimelineExclude();
        loadTimelineInclude();
        loadTimeline();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadTimeline(){
        SharedPreferences prefs1 = getContext().getSharedPreferences("patientexclude", Context.MODE_PRIVATE);
        String patientexclude = prefs1.getString("patientexclude",null);

        SharedPreferences prefs2 = getContext().getSharedPreferences("patientinclude", Context.MODE_PRIVATE);
        String patientinclude = prefs2.getString("patientinclude",null);

        if(patientexclude != null & patientinclude != null) {
            TimelineDao exc = new Gson().fromJson(patientexclude, TimelineDao.class);
            final TimelineDao in = new Gson().fromJson(patientinclude, TimelineDao.class);

            buildTimelineAdapter.setDao(listTimeline, exc, focustimer, in);
            listView.setAdapter(buildTimelineAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    Intent intent = new Intent(getContext(), PreparationActivity.class);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    intent.putExtra("position", position);
                    intent.putExtra("time", listTimeline[position]);
                    getActivity().startActivity(intent);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("เพิ่มยา PRN");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), AddPatientPRNActivity.class);
                            intent.putExtra("nfcUId", nfcUID);
                            intent.putExtra("sdlocId", sdlocID);
                            intent.putExtra("wardname", wardName);
                            intent.putExtra("position", position);
                            intent.putExtra("time", listTimeline[position]);
                            getActivity().startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    builder.create();
                    builder.show().getWindow().setLayout(1200, 250);
                    return true;
                }
            });
        }
    }

    private void saveCachePatientExclude(TimelineDao timelineDao){
        String json = new Gson().toJson(timelineDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientexclude", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientexclude",json);
        editor.apply();
    }

    private void saveCachePatientInclude(TimelineDao timelineDao){
        String json = new Gson().toJson(timelineDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientinclude", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patientinclude",json);
        editor.apply();
    }

    private void loadTimelineExclude(){
        Call<TimelineDao> call = HttpManager.getInstance().getService().getListCountPatientExcPRN(sdlocID);
        call.enqueue(new TimelineExcLoadCallback());

    }

    private void loadTimelineInclude(){
        Call<TimelineDao> call = HttpManager.getInstance().getService().getListCountPatientPRN(sdlocID);
        call.enqueue(new TimelineInLoadCallback());
    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK){
                    Intent intent = new Intent(getContext(), MainSelectMenuActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    class TimelineExcLoadCallback implements Callback<TimelineDao>{
        @Override
        public void onResponse(Call<TimelineDao> call, Response<TimelineDao> response) {
            TimelineDao dao = response.body();
//            String json = new Gson().toJson(dao);
//            Log.d("check", "TimelineExcLoadCallback = "+json);
            saveCachePatientExclude(dao);
        }

        @Override
        public void onFailure(Call<TimelineDao> call, Throwable t) {
            Log.d("check", "TimelineExcLoadCallback Failure " + t);
        }
    }



    class TimelineInLoadCallback implements Callback<TimelineDao>{

        @Override
        public void onResponse(Call<TimelineDao> call, Response<TimelineDao> response) {
            TimelineDao dao = response.body();
//            String json = new Gson().toJson(dao);
//            Log.d("check", "getListCountPatientPRN = "+json);
            saveCachePatientInclude(dao);
        }

        @Override
        public void onFailure(Call<TimelineDao> call, Throwable t) {
            Log.d("check", "TimelineInLoadCallback Failure " + t);
        }
    }
}
