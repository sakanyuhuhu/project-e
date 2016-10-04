package th.ac.mahidol.rama.emam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import th.ac.mahidol.rama.emam.R;


public class CameraScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_scan_camera);
        mScannerView = new ZXingScannerView(CameraScanActivity.this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(CameraScanActivity.this);
        mScannerView.startCamera();
//        mScannerView.setFlash(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
//        mScannerView.setFlash(false);
    }

    @Override
    public void handleResult(Result result) {

        Intent intentMessage = new Intent();
        intentMessage.putExtra("MESSAGE", result.getText());
        setResult(1,intentMessage);
        finish();
    }
}
