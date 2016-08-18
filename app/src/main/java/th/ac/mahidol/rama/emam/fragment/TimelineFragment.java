package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.TimelineAdapter;
import th.ac.mahidol.rama.emam.dao.listpatienttime.ListPatientTimeCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class TimelineFragment extends Fragment {

    protected final String[] listTimeline = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00"};
    private int[] patientTime = new int[listTimeline.length];
    private int l = 0;
    private String strdf, nfcUId;
    private static String sdlocId;
    private ListPatientTimeCollectionDao listPatientTimeCollectionDao;

    public TimelineFragment() {
        super();
    }

    public static TimelineFragment newInstance(String nfcUId, String sdlocId) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
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
        View rootView = inflater.inflate(R.layout.fragment_tmeline, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        strdf = df.format(today);
        Log.d("check", "dateformat : " + strdf);
        final ListView listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId);

        Call<ListPatientTimeCollectionDao> call = HttpManager.getInstance().getService().loadListPatientTime(sdlocId);
        call.enqueue(new Callback<ListPatientTimeCollectionDao>() {
            @Override
            public void onResponse(Call<ListPatientTimeCollectionDao> call, Response<ListPatientTimeCollectionDao> response) {
                listPatientTimeCollectionDao = response.body();
                saveCache(listPatientTimeCollectionDao);
                List<String> listTimeToday = new ArrayList<String>();
                List<String> listTimeNext = new ArrayList<String>();

                for (int i = 0; i < listPatientTimeCollectionDao.getListPatientTimeBean().size(); i++) {
                    if (response.body().getListPatientTimeBean().get(i).getDrugUseDate().trim().equals(strdf)) {
                        listTimeToday.add(listPatientTimeCollectionDao.getListPatientTimeBean().get(i).getAdminTimeHour());
                    } else  {
                        listTimeNext.add(listPatientTimeCollectionDao.getListPatientTimeBean().get(i).getAdminTimeHour());
                    }
                }
                for (int i=0; i<patientTime.length; i++) {
                    if (i <= 23) {
                        patientTime[i] = Collections.frequency(listTimeToday, "" + i);
                    } else {
                        patientTime[i] = Collections.frequency(listTimeNext, "" + l);
                        l++;
                    }
                }

                TimelineAdapter timelineAdapter = new TimelineAdapter(listTimeline, patientTime);
                listView.setAdapter(timelineAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Time selected: " + listTimeline[position]+" "+patientTime[position]);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int positionBtn) {

                                Intent intent = new Intent(getContext(), PreparationActivity.class);
                                intent.putExtra("timer", listTimeline[position]);
                                intent.putExtra("numPatient", patientTime[position]);
                                intent.putExtra("position", position);
                                intent.putExtra("nfcUId", nfcUId);
                                intent.putExtra("sdlocId", sdlocId);
                                getActivity().startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.create();
                        builder.show();
                    }
                });
            }
            @Override
            public void onFailure(Call<ListPatientTimeCollectionDao> call, Throwable t) {
                Log.d("check", "TimelineFragment Failure " + t);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void saveCache(ListPatientTimeCollectionDao dao){
        String json = new Gson().toJson(dao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientintime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patienttime",json);
        editor.apply();
    }


}
