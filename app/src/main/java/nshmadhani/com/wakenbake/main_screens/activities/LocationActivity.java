package nshmadhani.com.wakenbake.main_screens.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skyfishjy.library.RippleBackground;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.fragments.NoInternetConnectionDialog;
import nshmadhani.com.wakenbake.main_screens.interfaces.ConnectivityReceiver;

public class LocationActivity extends AppCompatActivity implements ConnectivityReceiver {

    public LocationManager locationManager;
    public LocationListener locationListener;

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
            //makeUseOfNewLocation();
            gettingLocation();
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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) // If ACCESS_FINE_LOCATION is granted
               //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); //Update the location
                //gettingLocation();
                makeUseOfNewLocation();
        }
    }

    private void gettingLocation() {

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                makeUseOfNewLocation();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d(TAG, "onStatusChanged: ");
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d(TAG, "onProviderEnabled: ");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d(TAG, "onProviderDisabled: ");
            }
        };


        //Checking for permission for phone less than MarshMallow
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                makeUseOfNewLocation();
            }
        }

        //Phone is above Marshmallow
        else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Ask for permission
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                //We have the permissions of location
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                makeUseOfNewLocation();
            }
        }
    }

    private void makeUseOfNewLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //Got last known location. in Some rare situations this can be null.
                            if (location != null) {
                                Log.d(TAG, "onSuccess: " + location.toString());

                                Intent intent =  new Intent(LocationActivity.this,MainActivity.class);
//                                intent.putExtra("latitude",location.getLatitude());
//                                intent.putExtra("longitude",location.getLongitude());
                                intent.putExtra("location", location);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d("OnFailure ", "onSuccess: In Else Block");
                            }
                        }
                    });
        }
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
}
