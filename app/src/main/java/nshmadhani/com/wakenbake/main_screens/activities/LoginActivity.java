package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.fragments.NoInternetConnectionDialog;
import nshmadhani.com.wakenbake.main_screens.interfaces.ConnectivityReceiver;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public EditText mLoginEmailEditText;
    public EditText mLoginPasswordEditText;
    public Button mLoginButton;
    public TextView mForgotPassword;
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

                        mLoginEmailEditText.setEnabled(true);
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
                mForgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Redirecting to the Signup Activity
                        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                mLoginEmailEditText.setEnabled(true);
                mLoginPasswordEditText.setEnabled(true);

                NoInternetConnectionDialog connectionDialog = new NoInternetConnectionDialog();
                connectionDialog.show(getFragmentManager(), "no_internet_dialog");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void initialLayout () throws Exception {
        mLoginEmailEditText = findViewById(R.id.loginUsername);
        mLoginPasswordEditText = findViewById(R.id.loginPassword);
        mLoginButton = findViewById(R.id.loginButton);
        mForgotPassword = findViewById(R.id.forgotPassword);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, LocationActivity.class);
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

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
