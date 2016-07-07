package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.CustomAdapter;

/**
 * Created by mi- on 5/7/2559.
 */
public class SelectWardFragment extends Fragment{

    private ListView listViewAdapter;
    private List<String> listWards = new ArrayList<String>();
    private CustomAdapter customAdapter;


    public SelectWardFragment() {
        super();
    }

    public static SelectWardFragment newInstance() {
        SelectWardFragment fragment = new SelectWardFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_select_ward, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        listWards.add("Ward1");
        listWards.add("Ward2");
        listWards.add("Ward3");
        listWards.add("Ward4");
        listWards.add("Ward5");
        listWards.add("Ward6");
        listWards.add("Ward7");
        Log.d("check","Size "+listWards.size());
        listViewAdapter = (ListView) rootView.findViewById(R.id.listViewAdapter);
        customAdapter = new CustomAdapter(getContext(), listWards);
        listViewAdapter.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewAdapter.setAdapter(customAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
