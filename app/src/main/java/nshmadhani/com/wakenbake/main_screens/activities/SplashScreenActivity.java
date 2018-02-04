package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.Timer;
import java.util.TimerTask;

import nshmadhani.com.wakenbake.R;

public class SplashScreenActivity extends AppCompatActivity {

    long Delay = 8000; // duration of the Splash Screen
    AVLoadingIndicatorView indicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        indicatorView = findViewById(R.id.indicatorView);

        displaySplash(); // Displays the Splash Screen

    }
    void startAnim(){
        indicatorView.show();
    }

    void stopAnim(){
        indicatorView.hide();
    }
    private void displaySplash () {
        // Create a Timer
        Timer RunSplash = new Timer();

        startAnim();
        // Task to do when the timer ends
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {

                stopAnim();
                // Close SplashScreenActivity.class
                finish();
                // Start HomeScreenActivity.class
                Intent intent = new Intent(SplashScreenActivity.this,
                        HomeScreenActivity.class);
                startActivity(intent);

            }
        };
        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
}
