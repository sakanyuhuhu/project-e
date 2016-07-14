package th.ac.mahidol.rama.emam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.adapter.TimelineAdapter;
import th.ac.mahidol.rama.emam.dao.PatientDao;
import th.ac.mahidol.rama.emam.manager.SearchPatientByWardManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;

public class TimelineFragment extends Fragment {

    protected final String[] listTimeline = new String[]{"01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00"};
    private String sdlocId;
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
        new getPatientByWard().execute();
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
    public static class getPatientByWard extends AsyncTask<Void , Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SoapManager soapManager = new SoapManager();
            parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", "SDIPD83"));
            return null;
        }

        private void parseXML(String soap) {

            try {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                SearchPatientByWardManager myXMLHandler = new SearchPatientByWardManager();
                xmlReader.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource();

                inStream.setCharacterStream(new StringReader(soap));

                xmlReader.parse(inStream);


                ArrayList<PatientDao> itemsList = myXMLHandler.getItemsList();
                for (int i = 0; i < itemsList.size(); i++) {
                    PatientDao item = itemsList.get(i);
                    Log.d("check", "Patien Mrn" + item.getMrn());
                }
                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
                Log.w("AndroidParseXMLActivity", e);
            }
        }
    }

}
