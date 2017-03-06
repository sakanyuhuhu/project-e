package th.ac.mahidol.rama.emam.activity.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import th.ac.mahidol.rama.emam.manager.service;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private String setSound;
    private MediaPlayer media;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("check", "AlarmBroadcastReceiver started");
        SharedPreferences prefs = context.getSharedPreferences("SetSound", Context.MODE_PRIVATE);
        setSound = prefs.getString("sound", null);

        if(setSound.equals("OPEN")){
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            media = MediaPlayer.create(context,alarmUri);
            media.setLooping(true);
            media.start();
        }
        else{
            if(media!=null)
                media.stop();
        }

        Intent intents = new Intent(context, service.class);
        context.startService(intents);

    }

    public AlarmBroadcastReceiver stopSound(){
        if(media!=null)
            media.stop();
        return null;
    }
}

