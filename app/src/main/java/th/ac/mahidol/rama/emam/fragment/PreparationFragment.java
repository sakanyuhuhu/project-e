package th.ac.mahidol.rama.emam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
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
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.PreparationAdapter;
import th.ac.mahidol.rama.emam.dao.CheckPersonWardCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

public class PreparationFragment extends Fragment {

    private final String[] listPatient = new String[]{"น.ส.กรรณิการ์ สำเนียงเสนาะ", "นายยุทธศักดิ์ ฤทธิศรธนู", "นายบุญนะ อินทรตุล", "นายสันติ ตันฑิสุวรรณ", "นายประเสริฐ พิมพ์พาพร"};
    private String gettimer;
    private String nfcUId;
    private String sdlocId;
    private SQLiteManager dbHelper;
    private NfcAdapter mNfcAdapter;
    private String action;
    private boolean getregnfc;
    private TextView tvUserName;
    private TextView tvTimer;
    private String nfcTagId;

    public PreparationFragment() {
        super();
    }

    public static PreparationFragment newInstance(String gettimer, String nfcUId, String sdlocId) {
        PreparationFragment fragment = new PreparationFragment();
        Bundle args = new Bundle();
        args.putString("timer", gettimer);
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
        View rootView = inflater.inflate(R.layout.fragment_preparation, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
//        Toast.makeText(getActivity(), "Please tab NFC tag!", Toast.LENGTH_LONG).show();
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId);

        tvTimer = (TextView) rootView.findViewById(R.id.tvTimer);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTimer.setText(getArguments().getString("timer"));
        ListView listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        PreparationAdapter preparationAdapter = new PreparationAdapter(listPatient);
        listView.setAdapter(preparationAdapter);
//New Text Document.txt
//        action = getActivity().getIntent().getAction();
//        Log.d("check", "initInstances: " + action);
//        Log.d("check", "NfcAdapter: " + NfcAdapter.ACTION_TAG_DISCOVERED);
//        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
//            Tag nfcTag = getActivity().getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            nfcTagId = ByteArrayToHexString(nfcTag.getId());
//            Log.d("check", "myTag.getId : " + nfcTag.getId().toString());
//            Toast.makeText(getActivity(), nfcTagId, Toast.LENGTH_LONG).show();
//
//            dbHelper = new SQLiteManager(getActivity());
////            dbHelper.addNFCRegister(nfcTagId);
//            getregnfc = dbHelper.getNFCRegister(nfcTagId);
//            Log.d("check", "getreg: " + getregnfc);
//            if (getregnfc == true) {
//                nfcUId = nfcTagId;
                Call<CheckPersonWardCollectionDao> call = HttpManager.getInstance().getService().loadCheckPersonWard(nfcUId, sdlocId);
                call.enqueue(new Callback<CheckPersonWardCollectionDao>() {
                    @Override
                    public void onResponse(Call<CheckPersonWardCollectionDao> call, Response<CheckPersonWardCollectionDao> response) {
                        tvUserName.setText("Prepare by  "+response.body().getCheckPersonWardBean().getFirstName()+"  "+ response.body().getCheckPersonWardBean().getLastName());
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
                        builder.setTitle("Patient selected : " + listPatient[position]);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int positionBtn) {
                                Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
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
//            } else {
//                Toast.makeText(getActivity(), "Not found NFC tag!", Toast.LENGTH_LONG).show();
//            }
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
