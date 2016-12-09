package th.ac.mahidol.rama.emam.activity.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;

import th.ac.mahidol.rama.emam.R;

public class AlarmActivity extends AppCompatActivity{
    private String sdlocID, wardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        startAlert();
    }

    public void startAlert() {
        sdlocID = getIntent().getExtras().getString("sdlocId");
        wardName = getIntent().getExtras().getString("wardname");

         /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 00);

        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra("sdlocId", sdlocID);
        intent.putExtra("wardname", wardName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);//234324243

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        /* Repeating on every 30 second interval or 30 minutes(1000 * 60 * 30) */
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60 * 30, pendingIntent);

    }
}
