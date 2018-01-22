package nshmadhani.com.wakenbake.java_classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.app.PendingIntent.getActivity;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static final String TAG = ConnectivityReceiver.class.getSimpleName();
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {}

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.d(TAG, "onReceive: ");
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected());
        }
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) WakeNBakeApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
