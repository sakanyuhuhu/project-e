package th.ac.mahidol.rama.emam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import th.ac.mahidol.rama.emam.R;

/**
 * Created by mac-mini-1 on 9/16/2016 AD.
 */
public class CameraScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_scan_camera);
        mScannerView = new ZXingScannerView(CameraScanActivity.this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(CameraScanActivity.this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        Intent intentMessage = new Intent();
        intentMessage.putExtra("MESSAGE", result.getText());
        setResult(1,intentMessage);
        finish();
    }
}
