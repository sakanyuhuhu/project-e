package th.ac.mahidol.rama.emam.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import th.ac.mahidol.rama.emam.activity.alarm.PatientOnTimeActivity;


public class service extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Intent i = new Intent(getBaseContext(), PatientOnTimeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
