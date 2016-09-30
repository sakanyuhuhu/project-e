package th.ac.mahidol.rama.emam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.fragment.BuildHistoryPrepareFragment;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationForPatientFragment;

/**
 * Created by mac-mini-1 on 9/15/2016 AD.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private String nfcUID,sdlocID, wardName, time, firstName, lastName, RFID, userName;
    private int position, timeposition;
    private ListDrugCardDao listDrugCardDao;

    public TabsPagerAdapter(FragmentManager fm, String nfcUID, String sdlocID, String wardName, String RFID, String firstName, String lastName, int timeposition, int position, String time, ListDrugCardDao listDrugCardDao) {
        super(fm);
        this.nfcUID = nfcUID;
        this.sdlocID = sdlocID;
        this.wardName = wardName;
        this.time = time;
        this.firstName = firstName;
        this.lastName = lastName;
        this.RFID = RFID;
        this.timeposition = timeposition;
        this.position = position;
        this.listDrugCardDao = listDrugCardDao;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new BuildPreparationForPatientFragment().newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time, userName, listDrugCardDao);
            case 1:
                return new BuildHistoryPrepareFragment().newInstance(nfcUID, sdlocID, wardName, RFID, firstName, lastName, timeposition, position, time);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
