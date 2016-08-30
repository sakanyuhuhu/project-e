package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildTimelineAdapter;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildTimelineFragment extends Fragment {
    private String sdlocID, wardName;
    private String[] listTimeline = {"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "0:00", "1:00", "2:00"};
    private ListView listView;
    private BuildTimelineAdapter buildTimelineAdapter;

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

        listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        buildTimelineAdapter = new BuildTimelineAdapter();

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

    private void saveCache(TimelineDao timelineDao){
        String json = new Gson().toJson(timelineDao);
        SharedPreferences prefs = getContext().getSharedPreferences("patientintime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("patienttime",json);
        editor.apply();
    }


    class TimelineLoadCallback implements Callback<TimelineDao>{
        @Override
        public void onResponse(Call<TimelineDao> call, Response<TimelineDao> response) {
            TimelineDao dao = response.body();
            saveCache(dao);

            wardName = getArguments().getString("wardname");
            buildTimelineAdapter.setDao(listTimeline, dao);
            listView.setAdapter(buildTimelineAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
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
            Log.d("check", "BuildTimelineFragment Failure " + t);
        }
    }
}
