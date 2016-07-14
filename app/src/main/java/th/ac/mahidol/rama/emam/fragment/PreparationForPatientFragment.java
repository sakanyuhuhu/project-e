package th.ac.mahidol.rama.emam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.MainActivity;
import th.ac.mahidol.rama.emam.adapter.PreparationAdapter;
import th.ac.mahidol.rama.emam.dao.CheckPersonWardCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class PreparationForPatientFragment extends Fragment {

    private final String[] listPreparePatient = new String[]{ "Atenolol 50 mg", "Metformin 500 mg" };
    private String nfcUId;
    private String sdlocId;
    private TextView tvMedical;

    public PreparationForPatientFragment() {
        super();
    }

    public static PreparationForPatientFragment newInstance(String nfcUId, String sdlocId) {
        PreparationForPatientFragment fragment = new PreparationForPatientFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation_for_patient, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {;
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId);

        tvMedical = (TextView) rootView.findViewById(R.id.tvMedical);
        ListView listView = (ListView) rootView.findViewById(R.id.lvPreparePatientAdapter);
        PreparationAdapter preparationAdapter = new PreparationAdapter(listPreparePatient);
        listView.setAdapter(preparationAdapter);

        Call<CheckPersonWardCollectionDao> call = HttpManager.getInstance().getService().loadCheckPersonWard(nfcUId, sdlocId);
        call.enqueue(new Callback<CheckPersonWardCollectionDao>() {
            @Override
            public void onResponse(Call<CheckPersonWardCollectionDao> call, Response<CheckPersonWardCollectionDao> response) {
                tvMedical.setText("Prepare by  "+response.body().getCheckPersonWardBean().getFirstName()+"  "+ response.body().getCheckPersonWardBean().getLastName());
            }

            @Override
            public void onFailure(Call<CheckPersonWardCollectionDao> call, Throwable t) {
                Log.d("check", "Failure " + t);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Medical selected : " + listPreparePatient[position]);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
