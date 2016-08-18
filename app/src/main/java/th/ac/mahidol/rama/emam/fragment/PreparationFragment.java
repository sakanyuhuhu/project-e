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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import th.ac.mahidol.rama.emam.dao.listpatientinfo.ListPatientInfoCollectionDao;
import th.ac.mahidol.rama.emam.dao.listpatienttime.ListPatientTimeCollectionDao;
import th.ac.mahidol.rama.emam.dao.mrnforprepare.MRNBean;
import th.ac.mahidol.rama.emam.dao.mrnforprepare.MRNBean2;
import th.ac.mahidol.rama.emam.dao.mrnforprepare.MRNListBean;
import th.ac.mahidol.rama.emam.dao.mrnforprepare.MRNListBean2;
import th.ac.mahidol.rama.emam.dao.patientinfoforperson.PatientInfoForPersonCollectionDao;
import th.ac.mahidol.rama.emam.manager.HttpManager;

public class PreparationFragment extends Fragment {

    private String[] listPatient, listBedNo, listMrn;
    private String patientPerson, gettimer, strtimer, nfcUId, strdf, strdf2;
    private int numPatient, position, tricker;
    private static String sdlocId;
    private TextView tvUserName, tvTimer, tvPreparation, tvDoublecheck, tvAdministration;
    private ListView listView;
    private PreparationAdapter preparationAdapter;
    private ListPatientInfoCollectionDao listPatientInfoCollectionDao;
    private PatientInfoForPersonCollectionDao patientInfoForPersonCollectionDao;

    public PreparationFragment() {
        super();
    }

    public static PreparationFragment newInstance(String gettimer, int numPatient, String nfcUId, String sdlocId, int position) {
        PreparationFragment fragment = new PreparationFragment();
        Bundle args = new Bundle();
        args.putString("timer", gettimer);
        args.putInt("numPatient", numPatient);
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putInt("position", position);
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

        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        strdf = df.format(today);
        strdf2 = df2.format(today);

        gettimer = getArguments().getString("timer");
        numPatient = getArguments().getInt("numPatient");
        nfcUId = getArguments().getString("nfcUId");
        sdlocId = getArguments().getString("sdlocId");
        position = getArguments().getInt("position");
        Log.d("check", "nfcUId : " + nfcUId + " / sdlocId : " + sdlocId +" position: "+position);
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
        ArrayList<String> listmrnNext = new ArrayList<String>();
        List<MRNBean> listmrn = new ArrayList<MRNBean>();
        List<MRNBean2> listmrn2 = new ArrayList<MRNBean2>();
        MRNBean mrnBean;
        MRNListBean mrnListBean = null;
        MRNBean2 mrnBean2;
        MRNListBean2 mrnListBean2 = null;

        for(int i=0;i<dao.getListPatientTimeBean().size();i++){
            if(strtimer.equals(dao.getListPatientTimeBean().get(i).getAdminTimeHour()) && dao.getListPatientTimeBean().get(i).getDrugUseDate().trim().equals(strdf)) {
                mrnBean = new MRNBean();
                mrnListBean = new MRNListBean();
                listmrnToday.add(dao.getListPatientTimeBean().get(i).getMRN());
                mrnBean.setMrn(dao.getListPatientTimeBean().get(i).getMRN());
                listmrn.add(mrnBean);
                mrnListBean.setMrnBeans(listmrn);
            }else if(strtimer.equals(dao.getListPatientTimeBean().get(i).getAdminTimeHour()) && (!(dao.getListPatientTimeBean().get(i).getDrugUseDate().trim().equals(strdf)))){
                mrnBean2 = new MRNBean2();
                mrnListBean2 = new MRNListBean2();
                listmrnNext.add(dao.getListPatientTimeBean().get(i).getMRN());
                mrnBean2.setMrn(dao.getListPatientTimeBean().get(i).getMRN());
                listmrn2.add(mrnBean2);
                mrnListBean2.setMrnBeans(listmrn2);
            }
        }

        Log.d("check", "listmrnToday : "+listmrnToday.size());
        Log.d("check", "listmrnNext : "+listmrnNext.size());
        if(position<=23) {
            if (listmrnToday.size() > 1) {
                Call<ListPatientInfoCollectionDao> callInfos = HttpManager.getInstance().getService().loadListPatientInfoPost(mrnListBean);
                callInfos.enqueue(new Callback<ListPatientInfoCollectionDao>() {
                    @Override
                    public void onResponse(Call<ListPatientInfoCollectionDao> call, Response<ListPatientInfoCollectionDao> response) {
                        listPatientInfoCollectionDao = response.body();
                        Log.d("check", "listmrnToday>1 " + response.body().getListPatientInfoBean().size());
                        listPatient = new String[numPatient];
                        listBedNo = new String[numPatient];
                        listMrn = new String[numPatient];
                        List<String> list = new ArrayList<String>();

                        for (int i = 0; i < listPatientInfoCollectionDao.getListPatientInfoBean().size(); i++) {
                            listBedNo[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getBedID();
                            listPatient[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getInitialName() + listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getFirstName() + " " + listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getLastName();
                            listMrn[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getMRN();
                            list.add(listBedNo[i] + "," + listPatient[i] + "," + listMrn[i]);
                        }
//  sort ตามเลขที่เตียงเรียงจากน้อยไปมาก
                        Collections.sort(list);
                        for (int i = 0; i < list.size(); i++) {
                            String sum = list.get(i);
                            String arr[] = sum.split(",");
                            int j = 0;
                            listBedNo[i] = arr[j];
                            listPatient[i] = arr[j + 1];
                            listMrn[i] = arr[j + 2];
                        }
                        tricker = 1;
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
                                        intent.putExtra("strdf", strdf2);
                                        intent.putExtra("tricker", tricker);
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
            } else if (listmrnToday.size() == 1) {
                Log.d("check","listmrnToday=1 ");
                patientPerson = listmrnToday.get(0);
                Call<PatientInfoForPersonCollectionDao> callInfoPersonOne = HttpManager.getInstance().getService().loadListPatientInfo(patientPerson);
                callInfoPersonOne.enqueue(new Callback<PatientInfoForPersonCollectionDao>() {
                    @Override
                    public void onResponse(Call<PatientInfoForPersonCollectionDao> call, Response<PatientInfoForPersonCollectionDao> response) {
                        patientInfoForPersonCollectionDao = response.body();
                        listPatient = new String[numPatient];
                        listBedNo = new String[numPatient];
                        listMrn = new String[numPatient];
                        listBedNo[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getBedID();
                        listPatient[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getInitialName() + patientInfoForPersonCollectionDao.getListPatientInfoBean().getFirstName() + " " + patientInfoForPersonCollectionDao.getListPatientInfoBean().getLastName();
                        listMrn[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getMRN();

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
                                        intent.putExtra("strdf", strdf2);
                                        intent.putExtra("tricker", tricker);
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
        }else{
            if (listmrnNext.size() > 1) {
                Call<ListPatientInfoCollectionDao> callInfos = HttpManager.getInstance().getService().loadListPatientInfoPost2(mrnListBean2);
                callInfos.enqueue(new Callback<ListPatientInfoCollectionDao>() {
                    @Override
                    public void onResponse(Call<ListPatientInfoCollectionDao> call, Response<ListPatientInfoCollectionDao> response) {
                        listPatientInfoCollectionDao = response.body();
                        Log.d("check","listmrnNext>1 "+response.body().getListPatientInfoBean().size());
                        listPatient = new String[numPatient];
                        listBedNo = new String[numPatient];
                        listMrn = new String[numPatient];
                        List<String> list = new ArrayList<String>();

                        for (int i = 0; i < listPatientInfoCollectionDao.getListPatientInfoBean().size(); i++) {
                            listBedNo[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getBedID();
                            listPatient[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getInitialName() + listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getFirstName() + " " + listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getLastName();
                            listMrn[i] = listPatientInfoCollectionDao.getListPatientInfoBean().get(i).getMRN();
                            list.add(listBedNo[i] + "," + listPatient[i] + "," + listMrn[i]);
                        }

                        Collections.sort(list);
                        for (int i = 0; i < list.size(); i++) {
                            String sum = list.get(i);
                            String arr[] = sum.split(",");
                            int j = 0;
                            listBedNo[i] = arr[j];
                            listPatient[i] = arr[j + 1];
                            listMrn[i] = arr[j + 2];
                        }
                        tricker = 2;
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
                                        intent.putExtra("strdf", strdf2);
                                        intent.putExtra("tricker", tricker);
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
            } else if (listmrnNext.size() == 1) {
                Log.d("check","listmrnNext=1 ");
                patientPerson = listmrnNext.get(0);
                Call<PatientInfoForPersonCollectionDao> callInfoPersonOne = HttpManager.getInstance().getService().loadListPatientInfo(patientPerson);
                callInfoPersonOne.enqueue(new Callback<PatientInfoForPersonCollectionDao>() {
                    @Override
                    public void onResponse(Call<PatientInfoForPersonCollectionDao> call, Response<PatientInfoForPersonCollectionDao> response) {
                        patientInfoForPersonCollectionDao = response.body();
                        listPatient = new String[numPatient];
                        listBedNo = new String[numPatient];
                        listMrn = new String[numPatient];
                        listBedNo[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getBedID();
                        listPatient[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getInitialName() + patientInfoForPersonCollectionDao.getListPatientInfoBean().getFirstName() + " " + patientInfoForPersonCollectionDao.getListPatientInfoBean().getLastName();
                        listMrn[0] = patientInfoForPersonCollectionDao.getListPatientInfoBean().getMRN();

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
                                        intent.putExtra("strdf", strdf2);
                                        intent.putExtra("tricker", tricker);
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
        }


        tvTimer = (TextView) rootView.findViewById(R.id.tvTime);
        tvPreparation = (TextView) rootView.findViewById(R.id.tvPreparation);
        tvDoublecheck = (TextView) rootView.findViewById(R.id.tvDoublecheck);
        tvAdministration = (TextView) rootView.findViewById(R.id.tvAdministration);
        tvUserName = (TextView) rootView.findViewById(R.id.tvUserName);
        tvTimer.setText(getArguments().getString("timer"));
        listView = (ListView) rootView.findViewById(R.id.lvPatientAdapter);

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


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }


}
