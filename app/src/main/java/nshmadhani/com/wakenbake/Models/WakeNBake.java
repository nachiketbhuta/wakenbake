package nshmadhani.com.wakenbake.Models;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orm.SugarContext;

/**
 * Created by Nachiket on 17-Mar-18.
 */

public class WakeNBake extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SugarContext.init(this);
    }
}
