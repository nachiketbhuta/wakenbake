package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;
import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.interfaces.ConnectivityReceiver;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public EditText mLoginEmailEditText;
    public EditText mLoginPasswordEditText;
    public FancyButton mLoginButton;
    public TextView mSignupLinkTextView;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {

            initialLayout(); // Setting the layout of the login screen

            //Checking if the user is connected to internet.
            if (isNetworkAvailable()) {
                mAuth = FirebaseAuth.getInstance(); // Creating a instance of Firebase object

                //Clicking on Login Button
                mLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = mLoginEmailEditText.getText().toString(); // Getting the value of the email
                        String password = mLoginPasswordEditText.getText().toString(); // Getting the value of the password

                        mLoginEmailEditText.setEnabled(false);
                        mLoginPasswordEditText.setEnabled(true);

                        //Checking if both the fields are empty
                        if (!email.equals("") && !password.equals("")) {
                            try {
                                signIn(email, password);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //Checking if email is empty and password is not empty
                        else if (email.equals("") && !password.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter correct email", Toast.LENGTH_LONG).show();
                        }
                        //Checking if the email is not empty and password is empty
                        else if (!email.equals("") && password.equals("")) {
                            Toast.makeText(getApplicationContext(), "Enter correct password", Toast.LENGTH_LONG).show();
                        }
                        //Checking if the both the fields are filled
                        else {
                            Toast.makeText(getApplicationContext(), "Enter both username and password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Clicking on the Signup Link
                mSignupLinkTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Redirecting to the Signup Activity
                        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                mLoginEmailEditText.setEnabled(true);
                mLoginPasswordEditText.setEnabled(true);
                com.nispok.snackbar.Snackbar.with(getApplicationContext()).text("Please check your internet connection").show(LoginActivity.this);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void initialLayout () throws Exception {
        mLoginEmailEditText = findViewById(R.id.mLoginEmailEditText);
        mLoginPasswordEditText = findViewById(R.id.mLoginPasswordEditText);
        mLoginButton = findViewById(R.id.mLoginButton);
        mSignupLinkTextView = findViewById(R.id.mLoginSignupLinkTextView);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signIn (String email, String password) throws Exception {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "Successfully logged in!");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Please enter correct username and password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable = true;

        if (networkInfo != null && networkInfo.isConnected())
            networkAvailable = true;

        return networkAvailable;
    }
}
