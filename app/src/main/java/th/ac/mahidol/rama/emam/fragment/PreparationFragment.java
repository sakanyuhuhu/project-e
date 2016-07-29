package th.ac.mahidol.rama.emam.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.DoubleCheckActivity;
import th.ac.mahidol.rama.emam.activity.PreparationActivity;
import th.ac.mahidol.rama.emam.activity.PreparationForPatientActivity;
import th.ac.mahidol.rama.emam.adapter.PreparationAdapter;
import th.ac.mahidol.rama.emam.dao.ListPatientInfoCollectionDao;
import th.ac.mahidol.rama.emam.dao.ListPatientTimeCollectionDao;
import th.ac.mahidol.rama.emam.dao.MRNBean;
import th.ac.mahidol.rama.emam.dao.MRNListBean;
import th.ac.mahidol.rama.emam.dao.PatientInfoForPersonCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class PreparationFragment extends Fragment {

    private String[] listPatient, listBedNo, listMrn;
    private String patientPerson, gettimer, strtimer, nfcUId, strdf;
    private int numPatient;
    private static String sdlocId;
    private TextView tvUserName, tvTimer, tvPreparation, tvDoublecheck, tvAdministration;
    private ListView listView;
    private PreparationAdapter preparationAdapter;
    private Button btnCancel, btnSave;
    private ListPatientInfoCollectionDao listPatientInfoCollectionDao;
    private PatientInfoForPersonCollectionDao patientInfoForPersonCollectionDao;

    public PreparationFragment() {
        super();
    }

    public static PreparationFragment newInstance(String gettimer, int numPatient, String nfcUId, String sdlocId) {
        PreparationFragment fragment = new PreparationFragment();
        Bundle args = new Bundle();
        args.putString("timer", gettimer);
        args.putInt("numPatient", numPatient);
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
//        new getPatientByWard().execute();

        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        strdf = df.format(today);

        gettimer = getArguments().getString("timer");
        numPatient = getArguments().getInt("numPatient");
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId);
        if(gettimer.substring(0,1).equals("0")){
            strtimer = gettimer.substring(1,2);
        }
        else{
            strtimer = gettimer.substring(0,2);
        }

        SharedPreferences prefs = getContext().getSharedPreferences("patientintime", Context.MODE_PRIVATE);
        String data = prefs.getString("patienttime",null);
        if(data == null)
            return;
        ListPatientTimeCollectionDao dao = new Gson().fromJson(data,ListPatientTimeCollectionDao.class);
        Log.d("check", "PreparationFragment ListPatientTimeCollectionDao size = "+dao.getListPatientTimeBean().size());
        ArrayList<String> listmrnToday = new ArrayList<String>();
        List<String> listmrnNext = new ArrayList<String>();
        List<MRNBean> listmrn = new ArrayList<MRNBean>();
        MRNBean mrnBean = null;
        MRNListBean mrnListBean = null;
//        รับข้อมูลมา แล้วกรองเอาเฉพาะคนที่อยู่ในเวาลา และวันที่นั้น เท่านั้น!(ห้ามลบนะฮะ)
        for(int i=0;i<dao.getListPatientTimeBean().size();i++){
            if(strtimer.equals(dao.getListPatientTimeBean().get(i).getAdminTimeHour()) && dao.getListPatientTimeBean().get(i).getDrugUseDate().trim().equals(strdf)) {
                mrnBean = new MRNBean();
                mrnListBean = new MRNListBean();
                listmrnToday.add(dao.getListPatientTimeBean().get(i).getMRN());
                mrnBean.setMrn(dao.getListPatientTimeBean().get(i).getMRN());
                listmrn.add(mrnBean);
                mrnListBean.setMrnBeans(listmrn);
//                Log.d("check", "IF time : "+strtimer+" MRN: "+dao.getListPatientTimeBean().get(i).getMRN()+" Date : "+dao.getListPatientTimeBean().get(i).getDrugUseDate());
            }else if(!(strtimer.equals(dao.getListPatientTimeBean().get(i).getAdminTimeHour()) && dao.getListPatientTimeBean().get(i).getDrugUseDate().trim().equals(strdf))){
                listmrnNext.add(dao.getListPatientTimeBean().get(i).getMRN());
//                Log.d("check", "ELSE time : "+strtimer+" MRN: "+dao.getListPatientTimeBean().get(i).getMRN()+" Date : "+dao.getListPatientTimeBean().get(i).getDrugUseDate());
            }
        }

        Log.d("check", "listmrnToday : "+listmrnToday.size());
        Log.d("check", "listmrnNext : "+listmrnNext.size());

        if(listmrnToday.size() > 1) {
            Call<ListPatientInfoCollectionDao> callInfos = HttpManager.getInstance().getService().loadListPatientInfoPost(mrnListBean);
            callInfos.enqueue(new Callback<ListPatientInfoCollectionDao>() {
                @Override
                public void onResponse(Call<ListPatientInfoCollectionDao> call, Response<ListPatientInfoCollectionDao> response) {
                    listPatientInfoCollectionDao = response.body();
                    Log.d("check", "listPatientInfoCollectionDao =  " + listPatientInfoCollectionDao);
                    listPatient = new String[numPatient];
                    listBedNo = new String[numPatient];
                    listMrn = new String[numPatient];
                    Log.d("check", "listPatientInfoCollectionDao.getListPatientInfoBean.size = " + listPatientInfoCollectionDao.getListPatientInfoBean().size());
                    for (int i = 0; i < listPatientInfoCollectionDao.getListPatientInfoBean().size(); i++) {
                        listBedNo[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getBedID();
                        listPatient[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getInitialName() + listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getFirstName() + " " + listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getLastName();
                        listMrn[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getMRN();
                    }
                    Log.d("check", "listPatient = " + listPatient.length);
                    Log.d("check", "listBedNo   = " + listBedNo.length);
                    Log.d("check", "listMrn     = " + listMrn.length);
                    preparationAdapter = new PreparationAdapter(listPatient, listBedNo, listMrn);
                    listView.setAdapter(preparationAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Patient selected : " + listPatient[position]);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int positionBtn) {
                                    Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                                    intent.putExtra("timer", gettimer);
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
                public void onFailure(Call<ListPatientInfoCollectionDao> call, Throwable t) {
                    Log.d("check", "PreparationFragment callInfos Failure " + t);
                }
            });
        }else if(listmrnToday.size() == 1){
            patientPerson = listmrnToday.get(0);
            Call<PatientInfoForPersonCollectionDao> callInfoPersonOne = HttpManager.getInstance().getService().loadListPatientInfo(patientPerson);
            callInfoPersonOne.enqueue(new Callback<PatientInfoForPersonCollectionDao>() {
                @Override
                public void onResponse(Call<PatientInfoForPersonCollectionDao> call, Response<PatientInfoForPersonCollectionDao> response) {
                    patientInfoForPersonCollectionDao = response.body();
                    Log.d("check", "patientInfoForPersonCollectionDao =  " + patientInfoForPersonCollectionDao);
                    listPatient = new String[numPatient];
                    listBedNo = new String[numPatient];
                    listMrn = new String[numPatient];
                    Log.d("check", "patientInfoForPersonCollectionDao.getListPatientInfoBean.size = " + patientInfoForPersonCollectionDao.getListPatientInfoBean());
                    listBedNo[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getBedID();
                    listPatient[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getInitialName()+patientInfoForPersonCollectionDao.getListPatientInfoBean().getFirstName()+" "+patientInfoForPersonCollectionDao.getListPatientInfoBean().getLastName();
                    listMrn[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getMRN();

                    Log.d("check", "listPatient = " + listPatient.length);
                    Log.d("check", "listBedNo   = " + listBedNo.length);
                    Log.d("check", "listMrn     = " + listMrn.length);
                    preparationAdapter = new PreparationAdapter(listPatient, listBedNo, listMrn);
                    listView.setAdapter(preparationAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Patient selected : " + listPatient[position]);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int positionBtn) {
                                    Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
                                    intent.putExtra("timer", gettimer);
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
                public void onFailure(Call<PatientInfoForPersonCollectionDao> call, Throwable t) {
                    Log.d("check", "PreparationFragment callInfoPersonOne Failure " + t);
                }
            });

        }

        tvTimer = (TextView) rootView.findViewById(R.id.tvTimer);
        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTimer.setText(getArguments().getString("timer"));
        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        tvPreparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PreparationActivity.class);
                intent.putExtra("timer", gettimer);
                intent.putExtra("nfcUId", nfcUId);
                intent.putExtra("sdlocId", sdlocId);
                getActivity().startActivity(intent);
            }
        });

        tvDoublecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DoubleCheckActivity.class);
                intent.putExtra("timer", gettimer);
                intent.putExtra("nfcUId", nfcUId);
                intent.putExtra("sdlocId", sdlocId);
                getActivity().startActivity(intent);
            }
        });

// ส่วนนี้เป็น get person ward ตอนที่พนักงานแตะบัตรเพื่อจะเตรียมยา(แสดงชื่อของพนักงาน) บันทึกข้อมูลผู้เตรียมยาด้วย(ห้ามลบนะฮะ)
//        Call<CheckPersonWardCollectionDao> call2 = HttpManager.getInstance().getService().loadCheckPersonWard(nfcUId, sdlocId);
//        call2.enqueue(new Callback<CheckPersonWardCollectionDao>() {
//            @Override
//            public void onResponse(Call<CheckPersonWardCollectionDao> call, Response<CheckPersonWardCollectionDao> response) {
//                tvUserName.setText("Prepare by  " + response.body().getCheckPersonWardBean().getFirstName() + "  " + response.body().getCheckPersonWardBean().getLastName());
//            }
//
//            @Override
//            public void onFailure(Call<CheckPersonWardCollectionDao> call, Throwable t) {
//                Log.d("check", "PreparationFragment Failure2 " + t);
//            }
//        });

//        preparationAdapter = new PreparationAdapter(listPatient, listBedNo, listMrn);
//        listView.setAdapter(preparationAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Patient selected : " + listPatient[position]);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int positionBtn) {
//                        Intent intent = new Intent(getContext(), PreparationForPatientActivity.class);
//                        intent.putExtra("timer", gettimer);
//                        intent.putExtra("nfcUId", nfcUId);
//                        intent.putExtra("sdlocId", sdlocId);
//                        intent.putExtra("patientName", listPatient[position]);
//                        intent.putExtra("bedNo", listBedNo[position]);
//                        intent.putExtra("mRN", listMrn[position]);
//                        getActivity().startActivity(intent);
//                    }
//                });
//                builder.setNegativeButton("Cancel", null);
//                builder.create();
//                builder.show();
//            }
//        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

//    private void saveCache(PatientInfoForPersonCollectionDao dao){
//        String json = new Gson().toJson(dao);
//        SharedPreferences prefs = getContext().getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("PatientInfo",json);
//        editor.apply();
//    }

//    public class getPatientByWard extends AsyncTask<Void, Void, List<PatientDao>> {
//
//        @Override
//        protected void onPostExecute(List<PatientDao> patientDaos) {//การทำงานที่ต้องรอการประมวลผลจาก getPatientByWard ให้ย้ายมาทำในนี้
//            super.onPostExecute(patientDaos);
////            Log.d("check", " patientDaos.size() = " +  patientDaos.size());
//            for (int i = 0; i < patientDaos.size(); i++) {
//                listBedNo[i] = patientDaos.get(i).getBedno();
//                listMrn[i] = patientDaos.get(i).getMrn();
////                Log.d("check", i + " listEnc_Id   : " + patientDaos.get(i).getEncId());
////                Log.d("check", i + " listEnc_Type : " + patientDaos.get(i).getEncType());
////                Log.d("check", i + " listBedNo    : " + patientDaos.get(i).getBedno());
////                Log.d("check", i + " listMRN      : " + patientDaos.get(i).getMrn());
////                Log.d("check", i + " listWard     : " + patientDaos.get(i).getWard());
//            }
////            preparationAdapter = new PreparationAdapter(listPatient, listBedNo, listMrn);
////            listView.setAdapter(preparationAdapter);
//
//        }
//
//        @Override
//        protected List<PatientDao> doInBackground(Void... params) {
//            List<PatientDao> itemsList = new ArrayList<PatientDao>();
//            SoapManager soapManager = new SoapManager();
//            //itemsList = parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", sdlocId));
//            itemsList = parseXML(soapManager.getPatientByWard("Ws_SearchPatientByWard", "2TC"));
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
