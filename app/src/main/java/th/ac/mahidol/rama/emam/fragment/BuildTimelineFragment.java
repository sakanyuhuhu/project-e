package th.ac.mahidol.rama.emam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildTimelineAdapter;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildTimelineFragment extends Fragment {
    private String sdlocID, wardName, focustimer;
    private String[] listTimeline = {"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "0:00", "1:00", "2:00"};
    private ListView listView;
    private BuildTimelineAdapter buildTimelineAdapter;
    private String currentTime[], dateToday[], year[];
    private TextView tvDateToday;

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
        View rootView = inflater.inflate(R.layout.fragment_tmeline, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        tvDateToday = (TextView) rootView.findViewById(R.id.tvDateToday);
        listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        buildTimelineAdapter = new BuildTimelineAdapter();

        dateToday = DateFormat.getDateInstance(0).format(new Date()).split("ที่");
        currentTime = DateFormat.getTimeInstance().format(new Date()).split(":");
        if(currentTime[0].equals("00")|currentTime[0].equals("01")|currentTime[0].equals("02")|currentTime[0].equals("03")|currentTime[0].equals("04")|currentTime[0].equals("05")|
                currentTime[0].equals("06")|currentTime[0].equals("07")|currentTime[0].equals("08")|currentTime[0].equals("09")){
            focustimer = currentTime[0].substring(1);
            Log.d("check", "focustimer = "+ focustimer);
        }
        else {
            focustimer = currentTime[0];
            Log.d("check", "focustimer = "+ focustimer);
        }
        year = dateToday[1].split("ค.ศ.");
        tvDateToday.setText(dateToday[0]+","+year[0]+","+year[1]);

        loadTimeline();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void loadTimeline(){
        sdlocID = getArguments().getString("sdlocId");
        Call<TimelineDao> call = HttpManager.getInstance().getService().getTimeline(sdlocID);
        call.enqueue(new TimelineLoadCallback());
    }


    class TimelineLoadCallback implements Callback<TimelineDao>{
        @Override
        public void onResponse(Call<TimelineDao> call, Response<TimelineDao> response) {
            TimelineDao dao = response.body();
            wardName = getArguments().getString("wardname");
            buildTimelineAdapter.setDao(listTimeline, dao, focustimer);
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
        }

        @Override
        public void onFailure(Call<TimelineDao> call, Throwable t) {
            Log.d("check", "TimelineLoadCallback Failure " + t);
        }
    }
}
