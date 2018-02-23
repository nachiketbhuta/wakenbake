package nshmadhani.com.wakenbake.main_screens.interfaces;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.app.PendingIntent.getActivity;

public interface ConnectivityReceiver  {
        boolean isNetworkAvailable ();
}
