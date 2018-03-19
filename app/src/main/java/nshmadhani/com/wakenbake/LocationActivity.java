package nshmadhani.com.wakenbake;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skyfishjy.library.RippleBackground;

public class LocationActivity extends AppCompatActivity implements ConnectivityReceiver {

    public RippleBackground rippleBackground;
    public ImageView locationImageView;
    public TextView gettingLocationTextView;

    public static final String TAG = LocationActivity.class.getSimpleName();
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        Log.d(TAG, "onCreate: ");
        rippleBackground = findViewById(R.id.content);
        locationImageView = findViewById(R.id.locationIcon);
        gettingLocationTextView = findViewById(R.id.gettingLocation);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (isNetworkAvailable()) { //Start Ripple Effect
            rippleBackground.startRippleAnimation();
            makeUseOfNewLocation();
            Log.d(TAG, "onCreate:  in if ");
        } else {
            NoInternetConnectionDialog connectionDialog = new NoInternetConnectionDialog();
            connectionDialog.show(getFragmentManager(), "no_internet_dialog");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) { //If permission is granted
            makeUseOfNewLocation();
        } else {
            Log.d(TAG, "onRequestPermissionsResult: ");
        }
    }

    private void makeUseOfNewLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {// If ACCESS_FINE_LOCATION is granted
                //makeUseOfNewLocation();
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);         
                Log.d(TAG, "onRequestPermissionsResult: in inner if ");
            } else {
                Log.d(TAG, "onRequestPermissionsResult: in inner else");
            }
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //Got last known location. in Some rare situations this can be null.
                        if (location != null) {
                            Log.d(TAG, "onSuccess: " + location.toString());

                            Intent intent = new Intent(LocationActivity.this, NavigationActivity.class);
                            intent.putExtra("latitude", location.getLatitude());
                            intent.putExtra("longitude", location.getLongitude());
                            startActivity(intent);
                            finish();
                        } else {

                            Log.d(TAG, "onSuccess: " + "in else");
                        }
                    }
                });
            
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        boolean networkAvailable = false;

        if (networkInfo != null && networkInfo.isConnected())
            networkAvailable = true;

        return networkAvailable;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
}

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
