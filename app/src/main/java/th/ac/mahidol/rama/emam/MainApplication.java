package th.ac.mahidol.rama.emam;

import android.app.Application;

import th.ac.mahidol.rama.emam.manager.Contextor;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
