package th.ac.mahidol.rama.emam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.activity.TimelineActivity;


public class MainMenuFragment extends Fragment implements View.OnClickListener{
    private ImageButton imgBtnMedicationManage;

    public MainMenuFragment() {
        super();
    }

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();

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
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        imgBtnMedicationManage = (ImageButton) rootView.findViewById(R.id.imgBMedication);
        imgBtnMedicationManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check", "onClick to TimelineActivity");
                Intent intent = new Intent(getContext(), TimelineActivity.class);
                getActivity().startActivity(intent);
            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == imgBtnMedicationManage.getId())
            Toast.makeText(getActivity(), "You Clicked the button!", Toast.LENGTH_LONG).show();

    }

}
