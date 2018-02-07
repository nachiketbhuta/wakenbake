package nshmadhani.com.wakenbake.main_screens.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.main_screens.fragments.ErrorDialogFragment;
import nshmadhani.com.wakenbake.main_screens.fragments.NoInternetConnectionDialog;
import nshmadhani.com.wakenbake.main_screens.interfaces.ConnectivityReceiver;

import static java.util.concurrent.TimeUnit.SECONDS;

public class OtpActivity extends AppCompatActivity implements ConnectivityReceiver {

    private static final String TAG = OtpActivity.class.getSimpleName();
    private Pinview pinView;
    private FirebaseAuth mAuth;
    private EditText phoneNumberEditText;
    private TextView otpTextView;
    private Button confirmButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

       if (isNetworkAvailable()) {
           //Phone Number
           phoneNumberEditText = findViewById(R.id.otpPhoneNumber);

           //OTP
           otpTextView = findViewById(R.id.otpTextView);

           mAuth = FirebaseAuth.getInstance();

           pinView = findViewById(R.id.otpPinView);
           pinView.setInputType(Pinview.InputType.NUMBER);

           //Initializing the Confirm Button
           confirmButton = findViewById(R.id.confirmButton);

           confirmButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   phoneNumberEditText.setEnabled(false);
                   confirmButton.setEnabled(false);
                   phoneNumber = phoneNumberEditText.getText().toString();
                   PhoneAuthProvider.getInstance().verifyPhoneNumber(
                           phoneNumber,        // Phone number to verify
                           60,                 // Timeout duration
                           SECONDS,   // Unit of timeout
                           OtpActivity.this,               // Activity (for callback binding)
                           mCallbacks);

               }
           });

           mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

               @Override
               public void onVerificationCompleted(PhoneAuthCredential credential) {

                   Log.i(TAG, "onVerificationCompleted:" + credential);
                   signInWithPhoneAuthCredential(credential);
               }

               @Override
               public void onVerificationFailed(FirebaseException e) {
                   Log.i(TAG, "onVerificationFailed", e);

                   if (e instanceof FirebaseAuthInvalidCredentialsException) {
                       Log.i(TAG, "Invalid Request");
                   } else if (e instanceof FirebaseTooManyRequestsException) {
                       Log.i(TAG, "The SMS quota for the project has been exceeded");
                   }
               }

               @Override
               public void onCodeSent(String verificationId,
                                      PhoneAuthProvider.ForceResendingToken token) {
                   Log.i(TAG, "onCodeSent:" + verificationId);

                   phoneNumberEditText.setVisibility(View.INVISIBLE);

                   otpTextView.setVisibility(View.VISIBLE);
                   pinView.setVisibility(View.VISIBLE);

                   confirmButton.setText("Verify OTP");


                   mVerificationId = verificationId;
                   mResendToken = token;

               }
           };
       }
       else {
           NoInternetConnectionDialog connectionDialog = new NoInternetConnectionDialog();
           connectionDialog.show(getFragmentManager(), "no_internet_dialog");
       }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");

                            // ...
                           // Toast.makeText(OtpActivity.this,"Redirecting to Main Activity",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OtpActivity.this, LocationActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.i(TAG, "Exception in signInWithPhoneAuthCredential");
                            }
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
