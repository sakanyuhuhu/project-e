package th.ac.mahidol.rama.emam.activity;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import th.ac.mahidol.rama.emam.R;
import th.ac.mahidol.rama.emam.adapter.ViewPagerAdapter;
import th.ac.mahidol.rama.emam.fragment.BuildAdministrationFragment;
import th.ac.mahidol.rama.emam.fragment.BuildDoubleCheckFragment;
import th.ac.mahidol.rama.emam.fragment.BuildPreparationFragment;

public class PreparationActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private String wardID, sdlocID, nfcUID, wardName, nfcTagID, time, prn = "prepare", tricker;
    private int position, positionPage;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);

        initInstance(savedInstanceState);

    }

    private void initInstance(Bundle savedInstanceState) {
        nfcUID = getIntent().getExtras().getString("nfcUId");
        wardID = getIntent().getExtras().getString("wardId");
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");
        position = getIntent().getExtras().getInt("position");
        time = getIntent().getExtras().getString("time");
        tricker = getIntent().getExtras().getString("save");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationFragment.newInstance(wardID, nfcUID, sdlocID, wardName, position, time, prn, tricker)).commit();
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (!mNfcAdapter.isEnabled()) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        }

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag nfcTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcTagID = ByteArrayToHexString(nfcTag.getId());
            nfcUID = nfcTagID;
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationFragment.newInstance(wardID, nfcUID, sdlocID, wardName, position, time, prn, tricker)).commit();

//            veiwpager
//            Log.d("check", String.valueOf(ManageViewPagerAdapter.position));
//            Log.d("check", "positionPage = "+positionPage);
//            if(positionPage == 0){
//                getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildPreparationFragment.newInstance(wardID, nfcUID, sdlocID, wardName, position, time, prn, tricker)).commit();
//            }
//            else if(positionPage == 1){
//                getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildDoubleCheckFragment.newInstance(wardID, nfcUID, sdlocID, wardName, position, time, tricker)).commit();
//            }
//            else if(positionPage == 2){
//                getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, BuildAdministrationFragment.newInstance(wardID, nfcUID, sdlocID, wardName, position, time, tricker)).commit();
//            }

        }
        super.onNewIntent(intent);
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;

    }

    @Override
    protected void onResume() {
        if (mNfcAdapter != null)
            enableForegroundDispatchSystem();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mNfcAdapter != null)
            disableForegroundDispatchSystem();
        super.onPause();
    }

    private void enableForegroundDispatchSystem() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

    }

//    private void setupViewPager(ViewPager viewPager) {
//        ManageViewPagerAdapter adapter = new ManageViewPagerAdapter(getSupportFragmentManager(), wardID, null, sdlocID, wardName, position, time, prn, tricker);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                positionPage = position;
//            }
//
//            @Override cvghcv
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        viewPager.setAdapter(adapter);
//    }

    private void disableForegroundDispatchSystem() {
        mNfcAdapter.disableForegroundDispatch(this);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuildPreparationFragment().newInstance(wardID, nfcUID, sdlocID, wardName, position, time, prn, tricker), time + "              PREPARATION");
        adapter.addFragment(new BuildDoubleCheckFragment().newInstance(wardID, nfcUID, sdlocID, wardName, position, time, tricker), "DOUBLE CHECK");
        adapter.addFragment(new BuildAdministrationFragment().newInstance(wardID, nfcUID, sdlocID, wardName, position, time, tricker), "ADMINISTRATION");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                positionPage = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
    }

}
