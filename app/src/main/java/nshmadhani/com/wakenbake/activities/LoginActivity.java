package nshmadhani.com.wakenbake.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.fragments.LoginFragment;
import nshmadhani.com.wakenbake.fragments.NoInternetConnectionDialog;
import nshmadhani.com.wakenbake.fragments.SignUpFragment;
import nshmadhani.com.wakenbake.interfaces.ConnectivityReceiver;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public EditText mLoginEmailEditText;
    public EditText mLoginPasswordEditText;
    public Button mLoginButton;
    public TextView mForgotPassword;
    public FirebaseAuth mAuth;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); // Creating a instance of Firebase object

        if(isNetworkAvailable())
            Toast.makeText(this, "Internet Available", Toast.LENGTH_SHORT).show();

        mViewPager = findViewById(R.id.view_pager);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Login", LoginFragment.class)
                .add("Signup", SignUpFragment.class)
                .create());
        mViewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(mViewPager);
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
