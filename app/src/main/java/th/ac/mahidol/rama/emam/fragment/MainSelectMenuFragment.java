package th.ac.mahidol.rama.emam.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.MainActivity;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;
import th.ac.mahidol.rama.emam.activity.addmedication.AddMedicationPatientAllActivity;
import th.ac.mahidol.rama.emam.activity.history.PatientAllActivity;


public class MainSelectMenuFragment extends Fragment implements View.OnClickListener{
    private ImageButton imgBMedication, imgBHistory, imgBAddMedication, imgBLogout;
    private String sdlocID, nfcUID, wardName;
    public MainSelectMenuFragment() {
        super();
    }

    public static MainSelectMenuFragment newInstance(String nfcUID, String sdlocID, String wardname) {
        MainSelectMenuFragment fragment = new MainSelectMenuFragment();
        Bundle args = new Bundle();
        args.putString("nfcUId", nfcUID);
        args.putString("sdlocId",sdlocID);
        args.putString("wardname", wardname);
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
        View rootView = inflater.inflate(R.layout.fragment_main_select_menu, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        imgBMedication = (ImageButton) rootView.findViewById(R.id.imgBMedication);
        imgBHistory = (ImageButton) rootView.findViewById(R.id.imgBHistory);
        imgBAddMedication = (ImageButton) rootView.findViewById(R.id.imgBAddMedication);
        imgBLogout = (ImageButton) rootView.findViewById(R.id.imgBLogout);

        imgBMedication.setOnClickListener(this);
        imgBHistory.setOnClickListener(this);
        imgBAddMedication.setOnClickListener(this);
        imgBLogout.setOnClickListener(this);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imgBMedication) {
            Intent intent = new Intent(getContext(), TimelineActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
        }
        else if (view.getId() == R.id.imgBHistory){
            Intent intent = new Intent(getContext(), PatientAllActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
        }
        else if(view.getId() == R.id.imgBAddMedication){
            Intent intent = new Intent(getContext(), AddMedicationPatientAllActivity.class);
            intent.putExtra("nfcUId", nfcUID);
            intent.putExtra("sdlocId", sdlocID);
            intent.putExtra("wardname", wardName);
            getActivity().startActivity(intent);
        }
        else if(view.getId() == R.id.imgBLogout){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, final int keyCode, final KeyEvent event) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("EMAM");
                builder.setMessage("คุณต้องการออกจากระบบใช่หรือไม่?");
                builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(event.getAction() == KeyEvent.ACTION_UP & keyCode == KeyEvent.KEYCODE_BACK){
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
                builder.setNegativeButton("ไม่ใช่",null);
                builder.create();
                builder.show();
                return false;
            }
        });
    }
}
