package th.ac.mahidol.rama.emam.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.MainSelectMenuActivity;
import th.ac.mahidol.rama.emam.dao.ListWardCollectionDao;
import th.ac.mahidol.rama.emam.dao.ListWardDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class SelectWardFragment extends Fragment {

    private String[] nameWards;
    private ListWardCollectionDao dao;
    ListWardDao list;


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

    private void initInstances(final View rootView, Bundle savedInstanceState) {
        Call<ListWardCollectionDao> call = HttpManager.getInstance().getService().loadListWard();
        call.enqueue(new Callback<ListWardCollectionDao>() {
            @Override
            public void onResponse(Call<ListWardCollectionDao> call, Response<ListWardCollectionDao> response) {
                dao = response.body();

                final List<String> testDaos = new ArrayList<String>();
                nameWards = new String[dao.getListwardBean().size()];
                for(int i=0; i<dao.getListwardBean().size();i++) {
                    testDaos.add(dao.getListwardBean().get(i).getWardName());
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,testDaos );
                ListView listView = (ListView) rootView.findViewById(R.id.listViewAdapter);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Ward selected: "+ testDaos.get(position));
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int positionBtn) {
                                SharedPreferences prefs = getContext().getSharedPreferences("SETWARD", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("sdloc", dao.getListwardBean().get(position).getSdlocId());
                                editor.apply();

                                Intent intent = new Intent(getContext(), MainSelectMenuActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.create();
                        builder.show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ListWardCollectionDao> call, Throwable t) {
                Log.d("check","onFailure "+t);
            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
