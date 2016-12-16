package th.ac.mahidol.rama.emam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.ac.mahidol.rama.emam.R;

public class BuildMedicationManagementFragment extends Fragment{


    private ViewPager viewPager;
    private String wardID, sdlocID, nfcUID, wardName, time, prn, tricker;
    private int timeposition;

    public BuildMedicationManagementFragment() {
        super();
    }

    public static BuildMedicationManagementFragment newInstance(String wardID, String nfcUId, String sdlocId, String wardName, int timeposition, String time, String prn, String tricker) {
        BuildMedicationManagementFragment fragment = new BuildMedicationManagementFragment();
        Bundle args = new Bundle();
        args.putString("wardId", wardID);
        args.putString("nfcUId", nfcUId);
        args.putString("sdlocId", sdlocId);
        args.putString("wardname", wardName);
        args.putInt("timeposition", timeposition);
        args.putString("time", time);
        args.putString("prn", prn);
        args.putString("save", tricker);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medication_management, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {

    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        wardID = getArguments().getString("wardId");
        nfcUID = getArguments().getString("nfcUId");
        sdlocID = getArguments().getString("sdlocId");
        wardName = getArguments().getString("wardname");
        timeposition = getArguments().getInt("timeposition");
        time = getArguments().getString("time");
        prn = getArguments().getString("prn");
        tricker = getArguments().getString("save");

        Log.d("check", "wardID = "+wardID+" /nfcUID = "+nfcUID+" /sdlocID = "+sdlocID+" /wardName = "+wardName+" /timeposition = "+timeposition+" /time = "+time+" /prn = "+prn+" /tricker = "+tricker);

        //////////////////////////////////////////
        //start viewpager in fragment
        /////////////////////////////////////////

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return BuildPreparationFragment.newInstance(wardID, nfcUID, sdlocID, wardName, timeposition, time, prn, tricker);
                    case 1:
                        return BuildDoubleCheckFragment.newInstance(wardID, nfcUID, sdlocID, wardName, timeposition, time, tricker);
                    case 2:
                        return BuildAdministrationFragment.newInstance(wardID, nfcUID, sdlocID, wardName, timeposition, time, tricker);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

//            @Override
//            public CharSequence getPageTitle(int position) {
//                Log.d("check", "getPageTitle = "+position);
//                switch (position){
//                    case 0:
//                        return "Preparetion";
//                    case 1:
//                        return "Double Check";
//                    case 2:
//                        return "Administration";
//                    default:
//                        return "";
//                }
//            }

        });

        /////////////////////////////////
        //stop viewpager in fragment
        ////////////////////////////////

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

    private void onRestoreInstanceState(Bundle savedInstanceState) {

    }
}
