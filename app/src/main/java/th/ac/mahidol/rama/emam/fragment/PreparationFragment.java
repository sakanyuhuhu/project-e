package th.ac.mahidol.rama.emam.fragment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.PreparationAdapter;
import th.ac.mahidol.rama.emam.dao.CheckPersonWardCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SQLiteManager;

public class PreparationFragment extends Fragment{

    private final String[] listPatient = new String[] {"น.ส.กรรณิการ์ สำเนียงเสนาะ", "นายยุทธศักดิ์ ฤทธิศรธนู", "นายบุญนะ อินทรตุล", "นายสันติ ตันฑิสุวรรณ", "นายประเสริฐ พิมพ์พาพร"};
    private String gettimer;
    private String nfcUId;
    private String sdlocId;
    private SQLiteManager dbHelper;
    private NfcAdapter mNfcAdapter;

    public PreparationFragment() {
        super();
    }

    public static PreparationFragment newInstance(String gettimer, String sdlocId) {
        PreparationFragment fragment = new PreparationFragment();
        Bundle args = new Bundle();
        args.putString("time",gettimer);
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

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if(mNfcAdapter == null){
            Toast.makeText(getActivity(), "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            getActivity().finish();
            return;
        }
        if(!mNfcAdapter.isEnabled()){
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preparation, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        Log.d("check", "Welcome to PreparationFragment ");
        sdlocId = getArguments().getString("sdlocId");
        Log.d("check", "nfcUId : "+ nfcUId+ " / sdlocId : "+sdlocId);
        boolean getregnfc;
        String action = getActivity().getIntent().getAction();
        Log.d("check", "initInstances: " + action);
        if ( NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Log.d("check", "NfcAdapter.ACTION_TAG_DISCOVERED: " + action);
            Tag nfcTag = getActivity().getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String nfcTagId = ByteArrayToHexString(nfcTag.getId());
            Log.d("check", "myTag.getId : " + nfcTag.getId().toString());
            Toast.makeText(getActivity(), nfcTagId, Toast.LENGTH_LONG).show();

            dbHelper = new SQLiteManager(getActivity());
            dbHelper.addNFCRegister("HN1", nfcTagId);
            getregnfc = dbHelper.getNFCRegister(nfcTagId);
            Log.d("check", "getreg " + getregnfc);
            if(getregnfc == true){
                nfcUId = nfcTagId;
//                Log.d("check", "nfcUId : "+ nfcUId+ "sdlocId : "+sdlocId);
                Call<CheckPersonWardCollectionDao> call = HttpManager.getInstance().getService().loadCheckPersonWard(nfcUId, sdlocId);
                call.enqueue(new Callback<CheckPersonWardCollectionDao>() {
                    @Override
                    public void onResponse(Call<CheckPersonWardCollectionDao> call, Response<CheckPersonWardCollectionDao> response) {

                    }

                    @Override
                    public void onFailure(Call<CheckPersonWardCollectionDao> call, Throwable t) {

                    }
                });
            }
            else{
                Toast.makeText(getActivity(),"Not found NFC tag!", Toast.LENGTH_LONG).show();
            }
        }


        /////////////////////////////////////////////////
        TextView tvTimer = (TextView) rootView.findViewById(R.id.tvTimer);
        tvTimer.setText(getArguments().getString("time"));
        ListView listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        PreparationAdapter preparationAdapter = new PreparationAdapter(listPatient);
        listView.setAdapter(preparationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Patient selected : "+ listPatient[position]);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
                        Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
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

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private void enableForegroundDispatchSystem(){
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, intentFilters, null);

    }

    private void disableForegroundDispatchSystem(){

        mNfcAdapter.disableForegroundDispatch(getActivity());
    }

    @Override
    public void onResume() {
        enableForegroundDispatchSystem();
        super.onResume();
    }

    @Override
    public void onPause() {
        disableForegroundDispatchSystem();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
