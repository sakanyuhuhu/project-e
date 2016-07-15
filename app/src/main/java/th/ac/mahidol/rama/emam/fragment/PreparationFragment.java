package th.ac.mahidol.rama.emam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.PreparationAdapter;
import th.ac.mahidol.rama.emam.dao.CheckPersonWardCollectionDao;
import th.ac.mahidol.rama.emam.dao.PatientDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;
import th.ac.mahidol.rama.emam.manager.SearchPatientByWardManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;

public class PreparationFragment extends Fragment {

    private final String[] listPatient = new String[]{"น.ส.กรรณิการ์ สำเนียงเสนาะ", "นายยุทธศักดิ์ ฤทธิศรธนู", "นายบุญนะ อินทรตุล", "นายสันติ ตันฑิสุวรรณ", "นายประเสริฐ พิมพ์พาพร", "น.ส.กรรณิการ์ สำเนียงเสนาะ", "นายยุทธศักดิ์ ฤทธิศรธนู", "นายบุญนะ อินทรตุล", "นายสันติ ตันฑิสุวรรณ", "นายประเสริฐ พิมพ์พาพร", "นายสันติ ตันฑิสุวรรณ"};
    private String[] listBedNo = new String []{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private String[] listMrn = new String []{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private String gettimer;
    private String nfcUId;
    private static String sdlocId;
    private TextView tvUserName;
    private TextView tvTimer;
    private ListView listView;
    private  PreparationAdapter preparationAdapter;

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
        new getPatientByWard().execute();
//        Toast.makeText(getActivity(), "Please tab NFC tag!", Toast.LENGTH_LONG).show();
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId);

        tvTimer = (TextView) rootView.findViewById(R.id.tvTimer);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTimer.setText(getArguments().getString("timer"));
        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);

        Call<CheckPersonWardCollectionDao> call = HttpManager.getInstance().getService().loadCheckPersonWard(nfcUId, sdlocId);
        call.enqueue(new Callback<CheckPersonWardCollectionDao>() {
            @Override
            public void onResponse(Call<CheckPersonWardCollectionDao> call, Response<CheckPersonWardCollectionDao> response) {
                tvUserName.setText("Prepare by  " + response.body().getCheckPersonWardBean().getFirstName() + "  " + response.body().getCheckPersonWardBean().getLastName());
            }

            @Override
            public void onFailure(Call<CheckPersonWardCollectionDao> call, Throwable t) {
                Log.d("check", "Failure " + t);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Patient selected : " + listPatient[position]);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
                        Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                        intent.putExtra("nfcUId", nfcUId);
                        intent.putExtra("sdlocId", sdlocId);
                        intent.putExtra("patientName", listPatient[position]);
                        intent.putExtra("bedNo", listBedNo[position]);
                        intent.putExtra("mRN", listMrn[position]);
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

    public class getPatientByWard extends AsyncTask<Void, Void, List<PatientDao>> {

        @Override
        protected void onPostExecute(List<PatientDao> patientDaos) {//การทำงานที่ต้องรอการประมวลผลจาก getPatientByWard ให้ย้ายมาทำในนี้
            super.onPostExecute(patientDaos);

            for (int i = 0; i < patientDaos.size(); i++) {
                listBedNo[i] = patientDaos.get(i).getBedNo();
                listMrn[i] = patientDaos.get(i).getMrn();
            }
            preparationAdapter = new PreparationAdapter(listPatient, listBedNo, listMrn);
            listView.setAdapter(preparationAdapter);

        }

        @Override
        protected List<PatientDao> doInBackground(Void... params) {
            List<PatientDao> itemsList = new ArrayList<PatientDao>();
            SoapManager soapManager = new SoapManager();
            itemsList = parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", sdlocId));
            return itemsList;
        }

        private   ArrayList<PatientDao>  parseXML(String soap) {
            List<PatientDao> itemsList = new ArrayList<PatientDao>();
            try {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                SearchPatientByWardManager searchPatientXMLHandler = new SearchPatientByWardManager();
                xmlReader.setContentHandler(searchPatientXMLHandler);
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(soap));
                xmlReader.parse(inStream);
                itemsList = searchPatientXMLHandler.getItemsList();

                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
                Log.w("AndroidParseXMLActivity", e);
            }

            return (ArrayList<PatientDao>) itemsList;
        }
    }
}
