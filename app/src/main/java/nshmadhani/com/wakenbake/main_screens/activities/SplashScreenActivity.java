package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import io.supercharge.shimmerlayout.ShimmerLayout;
import nshmadhani.com.wakenbake.R;


public class SplashScreenActivity extends AppCompatActivity {

    long Delay = 1000; // duration of the Splash Screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        displaySplash(); // Displays the Splash Screen

        //Shimmer effect (Facebook)
        ShimmerLayout shimmerText = findViewById(R.id.shimmer_text);
        shimmerText.startShimmerAnimation();
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
                        SignupActivity.class);
                startActivity(intent);
                finish();
            }
        };
        // Start the timer
        RunSplash.schedule(ShowSplash, Delay);
    }
}
