package th.ac.mahidol.rama.emam.activity.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import th.ac.mahidol.rama.emam.manager.service;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("check", "AlarmBroadcastReceiver started");

        Intent intents = new Intent(context, service.class);
        context.startService(intents);

    }

}

