package nshmadhani.com.wakenbake.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.UserProfileChangeRequest;
;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.fragments.NoInternetConnectionDialog;
import nshmadhani.com.wakenbake.interfaces.ConnectivityReceiver;


public class SignupActivity extends AppCompatActivity implements ConnectivityReceiver {

    public static final String TAG = SignupActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 2;
    public EditText mSignupNameEditText;
    public EditText mSignupEmailEditText;
    public EditText mSignupPasswordEditText;
    public Button mSignupSignupButton;
    public TextView mSignupLoginLinkTextView;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Checking if the user is connected to internet.
        try {

            initialLayout(); // Setting the layout of the signup screen

            if (isNetworkAvailable()) {
                mAuth = FirebaseAuth.getInstance(); // Creating a instance of Firebase object

                //Clicking on Signup Button
                mSignupSignupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSignupEmailEditText.setEnabled(true);
                        mSignupPasswordEditText.setEnabled(true);
                        //Getting values of the email and password fields
                        String email = mSignupEmailEditText.getText().toString();
                        String password = mSignupPasswordEditText.getText().toString();
                        //String username = mSignupNameEditText.getText().toString();

                        //Checking the fields
                        if (!email.equals("") && !password.equals("")) {
                            try {
                                createAccount(email, password); // Creating an account on the Firebase
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (email.equals("") && !password.equals("")) {
                            Toast.makeText(SignupActivity.this, "Enter email", Toast.LENGTH_LONG).show();
                        } else if (!email.equals("") && password.equals("")) {
                            Toast.makeText(SignupActivity.this, "Enter password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Enter both email and password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Clicking on the Signup Link
                mSignupLoginLinkTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Redirecting to the Signup Activity
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                mSignupEmailEditText.setEnabled(true);
                mSignupPasswordEditText.setEnabled(true);

                NoInternetConnectionDialog connectionDialog = new NoInternetConnectionDialog();
                connectionDialog.show(getFragmentManager(), "no_internet_dialog");

                //System.exit(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void createAccount( String email, String password) throws Exception{

    }

    public void initialLayout () {
        mSignupNameEditText = findViewById(R.id.signupUsername);
        mSignupEmailEditText = findViewById(R.id.signupEmail);
        mSignupPasswordEditText = findViewById(R.id.signupPassword);
        mSignupSignupButton = findViewById(R.id.signupButton);
        mSignupLoginLinkTextView = findViewById(R.id.signupLink);
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
