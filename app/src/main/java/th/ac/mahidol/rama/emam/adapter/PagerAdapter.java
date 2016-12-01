package th.ac.mahidol.rama.emam.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.ac.mahidol.rama.emam.fragment.BuildAdministrationFragment;
import th.ac.mahidol.rama.emam.fragment.BuildDoubleCheckFragment;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BuildPreparationFragment tab1 = new BuildPreparationFragment();
                return tab1;
            case 1:
                BuildDoubleCheckFragment tab2 = new BuildDoubleCheckFragment();
                return tab2;
            case 2:
                BuildAdministrationFragment tab3 = new BuildAdministrationFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
