package th.ac.mahidol.rama.emam;

import android.app.Application;

import th.ac.mahidol.rama.emam.manager.Contextor;

/**
 * Created by mi- on 22/8/2559.
 */
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
