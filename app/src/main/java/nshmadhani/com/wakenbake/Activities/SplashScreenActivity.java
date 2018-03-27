package nshmadhani.com.wakenbake.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import nshmadhani.com.wakenbake.Interfaces.IConnectivityReceiver;
import nshmadhani.com.wakenbake.R;


public class SplashScreenActivity extends AppCompatActivity implements IConnectivityReceiver{

    private ImageView imageView;
    long Delay = 4000; // duration of the Splash Screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        if (isNetworkAvailable()) { //Checking internet connectivity
            initLayout(); // initiate the layout
        }
        else { //Displays a dialog box telling no internet connectivity
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection")
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isNetworkAvailable())
                                        initLayout();
                                }
                            })

                    .setNegativeButton("Cancel",
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
        }
    }

    private void initLayout() {
        //Initiating the layout
        imageView = findViewById(R.id.splashScreenImage);

        Picasso.with(this)
                .load(R.drawable.splash_screen)
                .into(imageView);

        displaySplash(); // Displays the Splash Screen
    }
    private void displaySplash () {
        // Create a Timer
        Timer RunSplash = new Timer();

        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {

                // Start HomeScreenActivity.class
                Intent intent = new Intent(SplashScreenActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

