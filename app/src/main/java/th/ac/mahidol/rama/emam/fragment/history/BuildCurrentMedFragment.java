package th.ac.mahidol.rama.emam.fragment.history;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.history.PatientAllActivity;
import th.ac.mahidol.rama.emam.adapter.history.BuildCurrentMedAdapter;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CurrentMedDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListCurrentMedDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.PatientDataDao;
import th.ac.mahidol.rama.emam.manager.SearchCurrentMedManager;
import th.ac.mahidol.rama.emam.manager.SoapManager;
import th.ac.mahidol.rama.emam.view.history.BuildHistoryHeaderPatientDataView;

public class BuildCurrentMedFragment extends Fragment {
    private String nfcUID, wardID, sdlocID, wardName, toDayDate;
    private int position;
    private ListView listView;
    private BuildCurrentMedAdapter buildCurrentMedAdapter;
    private BuildHistoryHeaderPatientDataView buildHistoryHeaderPatientDataView;
    private PatientDataDao patient;
    private Date datetoDay;


    public BuildCurrentMedFragment() {
        super();
    }

    public static BuildCurrentMedFragment newInstance(String nfcUID, String wardID, String sdlocID, String wardName, int position, PatientDataDao patient) {
        BuildCurrentMedFragment fragment = new BuildCurrentMedFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("wardId", wardID);
        args.putString("sdlocId", sdlocID);
        args.putString("wardname", wardName);
        args.putInt("position", position);
        args.putParcelable("patient", patient);
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
        wardID = getArguments().getString("wardId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        position = getArguments().getInt("position");
        patient = getArguments().getParcelable("patient");

        listView = (ListView) rootView.findViewById(R.id.lvCurrentMed);

        buildHistoryHeaderPatientDataView = (BuildHistoryHeaderPatientDataView) rootView.findViewById(R.id.headerPatientAdapter);
        buildCurrentMedAdapter = new BuildCurrentMedAdapter();

        datetoDay = new Date();
        SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        toDayDate = sdfForDrugUseDate.format(datetoDay);

        if (patient != null) {
            buildHistoryHeaderPatientDataView.setData(patient, position);
        }

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
                    intent.putExtra("wardId", wardID);
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
        private String[] specTime1;
        private String specTime2;

        @Override
        protected void onPostExecute(List<CurrentMedDao> currentMedDaos) {
            super.onPostExecute(currentMedDaos);
            Log.d("check", "*****CurrentMedDao Callservice = " + currentMedDaos.size());
            ListCurrentMedDao listCurrentMedDao = new ListCurrentMedDao();
            List<CurrentMedDao> medDaoList = new ArrayList<CurrentMedDao>();
            Date dateStart, dateStop;
            SimpleDateFormat sdfForDrugUseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (CurrentMedDao c : currentMedDaos) {
                if (!c.getTakeaction().trim().equals("O") | c.getOffpid().equals("")) {
                    try {
                        dateStart = sdfForDrugUseDate.parse(c.getStartdt() + " " + c.getStarttm());
                        dateStop = sdfForDrugUseDate.parse(c.getStopdt() + " " + c.getStoptm());
                        datetoDay = sdfForDrugUseDate.parse(toDayDate);
                        if (datetoDay.compareTo(dateStart) > 0 & datetoDay.compareTo(dateStop) > 0) {
                            if (c.getSpectime() != null) {
                                specTime1 = c.getSpectime().split(",");
                                String[] arrSpectime = new String[specTime1.length];
                                for (int i = 0; i < specTime1.length; i++) {
                                    if(!specTime1[i].equals("")) {
                                        if (specTime1[i].substring(0, 1).equals("0")) {
                                            specTime2 = specTime1[i].substring(1, 2);
                                            arrSpectime[i] = specTime2;
                                        } else {
                                            specTime2 = specTime1[i].substring(0, 2);
                                            arrSpectime[i] = specTime2;
                                        }
                                    }
                                }

                                if (arrSpectime.length == 1) {
                                    c.setSpectime(arrSpectime[0]);
                                } else if (arrSpectime.length == 2) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1]);
                                } else if (arrSpectime.length == 3) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1] + "-" + arrSpectime[2]);
                                } else if (arrSpectime.length == 4) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1] + "-" + arrSpectime[2] + "-" + arrSpectime[3]);
                                } else if (arrSpectime.length == 5) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1] + "-" + arrSpectime[2] + "-" + arrSpectime[3] + "-" + arrSpectime[4]);
                                } else if (arrSpectime.length == 6) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1] + "-" + arrSpectime[2] + "-" + arrSpectime[3] + "-" + arrSpectime[4] + "-" + arrSpectime[5]);
                                } else if (arrSpectime.length == 7) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1] + "-" + arrSpectime[2] + "-" + arrSpectime[3] + "-" + arrSpectime[4] + "-" + arrSpectime[5] + "-" + arrSpectime[6]);
                                } else if (arrSpectime.length == 8) {
                                    c.setSpectime(arrSpectime[0] + "-" + arrSpectime[1] + "-" + arrSpectime[2] + "-" + arrSpectime[3] + "-" + arrSpectime[4] + "-" + arrSpectime[5] + "-" + arrSpectime[6] + "-" + arrSpectime[7]);
                                }
                            }

                            medDaoList.add(c);
                        }
                    } catch (ParseException e) {
                        e.getStackTrace();
                    }
                }
            }
            listCurrentMedDao.setCurrentMedDaoList(medDaoList);
            Log.d("check", "*****CurrentMedDao New = " + medDaoList.size());
            buildCurrentMedAdapter.setDao(listCurrentMedDao);
            listView.setAdapter(buildCurrentMedAdapter);
        }

        @Override
        protected List<CurrentMedDao> doInBackground(Void... params) {
            List<CurrentMedDao> itemsList = new ArrayList<CurrentMedDao>();
            SoapManager soapManager = new SoapManager();

            if (patient != null) {
                itemsList = parseXML(soapManager.getDrugIPD("get_drug_IPD", patient.getMRN()));
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

            } catch (Exception e) {
//                Log.w("AndroidParseXMLActivity", e);
            }
            return (ArrayList<CurrentMedDao>) itemsList;
        }

    }
}
