package nachiketbhuta.com.wakenbake.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import nachiketbhuta.com.wakenbake.R;
import nachiketbhuta.com.wakenbake.Interfaces.IConnectivityReceiver;
import nachiketbhuta.com.wakenbake.Fragments.LoginFragment;
import nachiketbhuta.com.wakenbake.Fragments.SignUpFragment;

public class LoginActivity extends AppCompatActivity implements IConnectivityReceiver {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public FirebaseAuth mAuth;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); // Creating a instance of Firebase object

        if (isNetworkAvailable()) { // Checking Internet Connectivity
            Toast.makeText(this, "Internet Available", Toast.LENGTH_SHORT).show();

            mViewPager = findViewById(R.id.view_pager);
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("Login", LoginFragment.class)
                    .add("Signup", SignUpFragment.class)
                    .create()); // Adding the fragments to the viewpager object
            mViewPager.setAdapter(adapter);
            SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(mViewPager); //setting the viewpager to the SmartTabLayout

        } else {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection")
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   isNetworkAvailable();
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

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, LocationActivity.class);
            intent.putExtra("email", currentUser.getEmail());
            startActivity(intent);
            finish();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
