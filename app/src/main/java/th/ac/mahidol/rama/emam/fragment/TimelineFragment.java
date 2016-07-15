package th.ac.mahidol.rama.emam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.TimelineAdapter;

public class TimelineFragment extends Fragment {

    protected final String[] listTimeline = new String[]{"01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00"};
    private static String sdlocId;
    private String nfcUId;

    public TimelineFragment() {
        super();
    }

    public static TimelineFragment newInstance(String nfcUId, String sdlocId) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUId);
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
//        new getPatientByWard().execute();

        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        ListView listView = (ListView) rootView.findViewById(R.id.lvTimelineAdapter);
        TimelineAdapter timelineAdapter = new TimelineAdapter(listTimeline);
        listView.setAdapter(timelineAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Time selected: " + listTimeline[position]);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
                        Intent intent = new Intent(getContext(), PreparationActivity.class);
                        intent.putExtra("timer", listTimeline[position]);
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

//    public static class getPatientByWard extends AsyncTask<Void, Void, List<PatientDao>> {
//
//        @Override
//        protected void onPostExecute(List<PatientDao> patientDaos) {//การทำงานที่ต้องรอการประมวลผลจาก getPatientByWard ให้ย้ายมาทำในนี้
//            super.onPostExecute(patientDaos);
//            Log.d("check","Size "+patientDaos.size());
//            for (int i = 0; i < patientDaos.size(); i++) {
//                Log.d("check", "Patien Mrn" + patientDaos.get(i).getMrn());
//            }
//        }
//
//        @Override
//        protected List<PatientDao> doInBackground(Void... params) {
//            List<PatientDao> itemsList = new ArrayList<PatientDao>();
//            SoapManager soapManager = new SoapManager();
//            itemsList = parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", sdlocId));
//            return itemsList;
//        }
//
//        private   ArrayList<PatientDao>  parseXML(String soap) {
//            List<PatientDao> itemsList = new ArrayList<PatientDao>();
//            try {
//
//                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//                SAXParser saxParser = saxParserFactory.newSAXParser();
//                XMLReader xmlReader = saxParser.getXMLReader();
//
//                SearchPatientByWardManager searchPatientXMLHandler = new SearchPatientByWardManager();
//                xmlReader.setContentHandler(searchPatientXMLHandler);
//                InputSource inStream = new InputSource();
//                inStream.setCharacterStream(new StringReader(soap));
//                xmlReader.parse(inStream);
//                itemsList = searchPatientXMLHandler.getItemsList();
//
//                Log.w("AndroidParseXMLActivity", "Done");
//            } catch (Exception e) {
//                Log.w("AndroidParseXMLActivity", e);
//            }
//
//            return (ArrayList<PatientDao>) itemsList;
//        }
//    }

}
