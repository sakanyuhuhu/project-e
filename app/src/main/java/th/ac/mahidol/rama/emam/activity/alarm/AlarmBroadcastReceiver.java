package th.ac.mahidol.rama.emam.activity.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver{
    private String sdlocID, wardName;

    @Override
    public void onReceive(Context context, Intent intent) {
        sdlocID = intent.getStringExtra("sdlocId");
        wardName = intent.getStringExtra("wardname");

        Intent i = new Intent(context.getApplicationContext(), PatientOnTimeActivity.class);
        i.putExtra("sdlocId", sdlocID);
        i.putExtra("wardname", wardName);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
