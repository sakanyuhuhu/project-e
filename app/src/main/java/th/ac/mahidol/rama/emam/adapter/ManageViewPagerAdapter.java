package th.ac.mahidol.rama.emam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import th.ac.mahidol.rama.emam.fragment.BuildAdministrationFragment;
import th.ac.mahidol.rama.emam.fragment.BuildDoubleCheckFragment;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationFragment;


public class ManageViewPagerAdapter extends FragmentStatePagerAdapter {
    private String wardID, sdlocID, nfcUID, wardName, time, prn, tricker;
    public static int position;
    private int timeposition;
    public ManageViewPagerAdapter(FragmentManager manager, String wardID, String nfcUID, String sdlocID, String wardName, int position, String time, String prn, String tricker) {
        super(manager);
        this.wardID = wardID;
        this.sdlocID = sdlocID;
        this.nfcUID = nfcUID;
        this.wardName = wardName;
        this.time = time;
        this.prn = prn;
        this.tricker = tricker;
        this.timeposition = position;
    }


    @Override
    public Fragment getItem(int position) {
        this.position = position;
        Log.d("check", "nfcUID = "+nfcUID + " position = "+position);
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

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return time + "                 PREPARATION";
            case 1:
                return "DOUBLE CHECK";
            case 2:
                return "ADMINISTRATION";
            default:
                return "";
        }
    }

}
