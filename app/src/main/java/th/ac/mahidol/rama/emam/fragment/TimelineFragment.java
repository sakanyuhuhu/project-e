package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.TimelineAdapter;

public class TimelineFragment extends Fragment{

    protected final String[] listTimeline = new String[] { "01:00", "02:00", "03:00","04:00", "05:00", "06:00", "07:00","08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00" };

    public TimelineFragment() {
        super();
    }

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
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
        Log.d("check", "Welcome to TimelineFragment");
        ListView listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        TimelineAdapter timelineAdapter = new TimelineAdapter(listTimeline);
        listView.setAdapter(timelineAdapter);
//        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, listTimeline);
//        ListView listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
//        listView.setAdapter(arrayAdapter);
//        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Time selected: "+ listTimeline[position]);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
//                        Intent intent = new Intent(getContext(), MainSelectMenuActivity.class);
//                        getActivity().startActivity(intent);
//                        getActivity().finish();
//                    }
//                });
//                builder.setNegativeButton("Cancel", null);
//                builder.create();
//                builder.show();
//            }
//        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
