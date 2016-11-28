package th.ac.mahidol.rama.emam.fragment.history;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.history.PatientAllActivity;
import th.ac.mahidol.rama.emam.adapter.history.BuildCurrentMedAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CurrentMedDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListCurrentMedDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.manager.SearchCurrentMedManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.history.BuildHistoryHeaderPatientDataView;

public class BuildCurrentMedFragment extends Fragment {
    private String nfcUID, sdlocID, wardName, mrn;
    private int position;
    private ListView listView;
    private Spinner spinner1;
    private BuildCurrentMedAdapter buildCurrentMedAdapter;
    private BuildHistoryHeaderPatientDataView buildHistoryHeaderPatientDataView;


    public BuildCurrentMedFragment() {
        super();
    }

    public static BuildCurrentMedFragment newInstance(String nfcUID, String sdlocID, String wardName, int position) {
        BuildCurrentMedFragment fragment = new BuildCurrentMedFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", position);
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
        View rootView = inflater.inflate(R.layout.fragment_currentmed, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        new getDrugIPD().execute();
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        position = getArguments().getInt("position");
        Log.d("check", "BuildCurrentMedFragment nfcUID = " + nfcUID + " /sdlocID = " + sdlocID + " /wardName = " + wardName + " /position = " + position);

        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        listView = (ListView) rootView.findViewById(R.id.lvCurrentMed);

        buildHistoryHeaderPatientDataView = (BuildHistoryHeaderPatientDataView) rootView.findViewById(R.id.headerPatientAdapter);
        buildCurrentMedAdapter = new BuildCurrentMedAdapter();

        SharedPreferences prefs = getContext().getSharedPreferences("patientalldata", Context.MODE_PRIVATE);
        String data = prefs.getString("patientalldata", null);
        if (data != null) {
            ListPatientDataDao listPatientDataDao = new Gson().fromJson(data, ListPatientDataDao.class);
            mrn = listPatientDataDao.getPatientDao().get(position).getMRN();
            Log.d("check", "data size = " + listPatientDataDao.getPatientDao().size() + " position = " + position + " mrn = " + mrn);
            buildHistoryHeaderPatientDataView.setData(listPatientDataDao, position);
        }

        getOnClickSpinner();
    }

    private void getOnClickSpinner() {
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("All")) {
                    Log.d("check", "All");
                } else if (selectedItem.equals("Active")) {
                    Log.d("check", "Active");
                } else if (selectedItem.equals("Discontinue")) {
                    Log.d("check", "Discontinue");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(getContext(), PatientAllActivity.class);
                    intent.putExtra("nfcUId", nfcUID);
                    intent.putExtra("sdlocId", sdlocID);
                    intent.putExtra("wardname", wardName);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public class getDrugIPD extends AsyncTask<Void, Void, List<CurrentMedDao>> {
        private int i = 0;

        @Override
        protected void onPostExecute(List<CurrentMedDao> currentMedDaos) {
            super.onPostExecute(currentMedDaos);
            Log.d("check", "*****CurrentMedDao onPostExecute = " + currentMedDaos.size());
            ListCurrentMedDao listCurrentMedDao = new ListCurrentMedDao();
            List<CurrentMedDao> medDaoList = new ArrayList<CurrentMedDao>();
            for (CurrentMedDao c : currentMedDaos) {
                Log.d("check", i + " Route = (" + c.getRoute() + ") ***** OFF = ('" + c.getOffdt() + "')***** PRN = " + c.isPrn() + " ***** type = " + c.getTakeaction());
                i++;
                medDaoList.add(c);
            }
            listCurrentMedDao.setCurrentMedDaoList(medDaoList);
            buildCurrentMedAdapter.setDao(listCurrentMedDao);
            listView.setAdapter(buildCurrentMedAdapter);
        }

        @Override
        protected List<CurrentMedDao> doInBackground(Void... params) {
            List<CurrentMedDao> itemsList = new ArrayList<CurrentMedDao>();
            SoapManager soapManager = new SoapManager();

            SharedPreferences prefs = getContext().getSharedPreferences("patientalldata", Context.MODE_PRIVATE);
            String data = prefs.getString("patientalldata", null);
            if (data != null) {
                ListPatientDataDao listPatientDataDao = new Gson().fromJson(data, ListPatientDataDao.class);
                Log.d("check", "*****doInBackground data = " + listPatientDataDao.getPatientDao().get(position).getMRN());
                itemsList = parseXML(soapManager.getDrugIPD("get_drug_IPD", listPatientDataDao.getPatientDao().get(position).getMRN()));
            }
            return itemsList;
        }

        private ArrayList<CurrentMedDao> parseXML(String soap) {
            List<CurrentMedDao> itemsList = new ArrayList<CurrentMedDao>();
            try {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();

                SearchCurrentMedManager searchCurrentMedXMLHandler = new SearchCurrentMedManager();
                xmlReader.setContentHandler(searchCurrentMedXMLHandler);
                InputSource inStream = new InputSource();
                inStream.setCharacterStream(new StringReader(soap));
                xmlReader.parse(inStream);
                itemsList = searchCurrentMedXMLHandler.getItemsList();

                Log.w("AndroidParseXMLActivity", "Done");
            } catch (Exception e) {
                Log.w("AndroidParseXMLActivity", e);
            }

            return (ArrayList<CurrentMedDao>) itemsList;
        }

    }
}
