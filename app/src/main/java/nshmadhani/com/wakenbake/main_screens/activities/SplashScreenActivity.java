package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import nshmadhani.com.wakenbake.R;


public class SplashScreenActivity extends AppCompatActivity {

    long Delay = 3000; // duration of the Splash Screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

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
            }
        };
        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
}
