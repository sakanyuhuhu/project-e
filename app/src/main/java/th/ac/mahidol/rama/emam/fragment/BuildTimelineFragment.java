package th.ac.mahidol.rama.emam.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.AddPatientPRNActivity;
import th.ac.mahidol.rama.emam.activity.MainSelectMenuActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.BuildTimelineAdapter;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildTimelineFragment extends Fragment {
    private String nfcUID, wardID, sdlocID, wardName, focustimer;
    private String[] listTimeline = {"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "0:00", "1:00", "2:00"};
    private ListView listView;
    private BuildTimelineAdapter buildTimelineAdapter;
    private String currentTime[];
    private ProgressDialog progressDialog;
    private TimelineDao exc, in;

    public BuildTimelineFragment() {
        super();
    }

    public static BuildTimelineFragment newInstance(String nfcUID, String sdlocID, String wardName, String wardID) {
        BuildTimelineFragment fragment = new BuildTimelineFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");

        progressDialog = ProgressDialog.show(getContext(), "", "Loading", true);

        listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        buildTimelineAdapter = new BuildTimelineAdapter();
        currentTime = DateFormat.getTimeInstance().format(new Date()).split(":");

        if (currentTime[0].equals("00") | currentTime[0].equals("01") | currentTime[0].equals("02") | currentTime[0].equals("03") | currentTime[0].equals("04") | currentTime[0].equals("05") |
                currentTime[0].equals("06") | currentTime[0].equals("07") | currentTime[0].equals("08") | currentTime[0].equals("09"))
            focustimer = currentTime[0].substring(1);
        else
            focustimer = currentTime[0];

        loadTimelineExclude();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }


    private void loadTimelineExclude() {
        Call<TimelineDao> call = HttpManager.getInstance().getService().getListCountPatientExcPRN(sdlocID);
        call.enqueue(new TimelineExcLoadCallback());

    }

    private void loadTimelineInclude() {
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
                if (event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(getContext(), MainSelectMenuActivity.class);
                    intent.putExtra("wardId", wardID);
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

    class TimelineExcLoadCallback implements Callback<TimelineDao> {
        @Override
        public void onResponse(Call<TimelineDao> call, Response<TimelineDao> response) {
            exc = response.body();
            loadTimelineInclude();
        }

        @Override
        public void onFailure(Call<TimelineDao> call, Throwable t) {
            Log.d("check", "TimelineExcLoadCallback Failure " + t);
        }
    }


    class TimelineInLoadCallback implements Callback<TimelineDao> {

        @Override
        public void onResponse(Call<TimelineDao> call, Response<TimelineDao> response) {
            in = response.body();

            buildTimelineAdapter.setDao(listTimeline, exc, focustimer, in);
            listView.setAdapter(buildTimelineAdapter);
            progressDialog.dismiss();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    Intent intent = new Intent(getContext(), PreparationActivity.class);
                    intent.putExtra("wardId", wardID);
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
                    final TimelineDao newTimelineDao = new TimelineDao();
                    List<MrnTimelineDao> mrnTimelineDaoList = new ArrayList<MrnTimelineDao>();
                    mrnTimelineDaoList.add(in.getTimelineDao().get(position));
                    newTimelineDao.setTimelineDao(mrnTimelineDaoList);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("เพิ่มยา PRN");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), AddPatientPRNActivity.class);
                            intent.putExtra("wardId", wardID);
                            intent.putExtra("nfcUId", nfcUID);
                            intent.putExtra("sdlocId", sdlocID);
                            intent.putExtra("wardname", wardName);
                            intent.putExtra("position", position);
                            intent.putExtra("time", listTimeline[position]);
                            intent.putExtra("include", newTimelineDao);
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

        @Override
        public void onFailure(Call<TimelineDao> call, Throwable t) {
            Log.d("check", "TimelineInLoadCallback Failure " + t);
        }
    }
}
