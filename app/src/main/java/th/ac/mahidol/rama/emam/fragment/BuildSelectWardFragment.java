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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.MainActivity;
import th.ac.mahidol.rama.emam.dao.buildListWard.WardCollectionDao;
import th.ac.mahidol.rama.emam.dao.buildListWard.WardDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class BuildSelectWardFragment extends Fragment {
    private WardCollectionDao dao;
    private ListView listView;
    private List<String> wardName;
    private ArrayAdapter arrayAdapter;

    public BuildSelectWardFragment() {
        super();
    }

    public static BuildSelectWardFragment newInstance() {
        BuildSelectWardFragment fragment = new BuildSelectWardFragment();
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

    private void init(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_ward, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }


    private void initInstances(final View rootView, Bundle savedInstanceState) {

        listView = (ListView) rootView.findViewById(R.id.listViewAdapter);
        loadSelectWard();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void setListWardName(final List<String> wardName) {
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice, wardName);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Selected : " + wardName.get(position));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getContext().getSharedPreferences("SETWARD", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("wardId", dao.getListwardBean().get(position).getId());
                        editor.putString("sdlocId", dao.getListwardBean().get(position).getSdlocId());
                        editor.putString("wardname", dao.getListwardBean().get(position).getWardName());
                        editor.apply();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create();
                builder.show();
            }
        });
    }

    private void loadSelectWard() {
        Call<WardCollectionDao> call = HttpManager.getInstance().getService().getListWard();
        call.enqueue(new SelectWardLoadCallback());
    }


    class SelectWardLoadCallback implements Callback<WardCollectionDao> {

        @Override
        public void onResponse(Call<WardCollectionDao> call, Response<WardCollectionDao> response) {
            dao = response.body();
            if (dao != null) {
                wardName = new ArrayList<String>();
                for (WardDao wardDao : dao.getListwardBean()) {
                    wardName.add(wardDao.getWardName());
                }
                setListWardName(wardName);
            }
        }

        @Override
        public void onFailure(Call<WardCollectionDao> call, Throwable t) {
            Log.d("check", "SelectWardLoadCallback Failure " + t);
        }
    }
}
