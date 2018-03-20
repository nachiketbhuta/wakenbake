package nshmadhani.com.wakenbake;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by Nisha on 3/15/2018.
 */

public class LoginFragment extends Fragment {


    private static final int RC_SIGN_IN = 2;
    private TextInputLayout mUserNameTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private Button mLoginButton;
    public GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private LoginActivity mActivity;
    public static final String TAG = LoginFragment.class.getSimpleName();
    public GoogleSignInAccount acct;
    public GoogleApiClient mGoogleApiClient;
    public SignInButton mGoogleButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.login_fragment, container, false);

        mUserNameTextInputLayout = rootView.findViewById(R.id.username_til);
        mPasswordTextInputLayout = rootView.findViewById(R.id.password_til);
        mLoginButton = rootView.findViewById(R.id.login_bt);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(mActivity, "OnConnectFailedListener", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleButton = rootView.findViewById(R.id.googleButton);

        mGoogleButton.setSize(SignInButton.SIZE_WIDE);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click");
                attemptLogin();
            }
        });


        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: google button" );
                final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (LoginActivity) getActivity();

        if (mActivity != null) {
            mAuth = mActivity.getmAuth();
        }

    }

    public void toggleInputs(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    mUserNameTextInputLayout.getEditText().setEnabled(!mUserNameTextInputLayout.getEditText().isEnabled());
                    mPasswordTextInputLayout.getEditText().setEnabled(!mPasswordTextInputLayout.getEditText().isEnabled());
                    mLoginButton.setEnabled(!mLoginButton.isEnabled());
            }
        });
    }

    public void attemptLogin(){
        toggleInputs();
        String username = mUserNameTextInputLayout.getEditText().getText().toString();
        String password = mPasswordTextInputLayout.getEditText().getText().toString();

        if (!username.equals("") && !password.equals("")) {
            try {
                Toast.makeText(mActivity, "All is Cool", Toast.LENGTH_SHORT).show();
                signIn(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Checking if username is empty and password is not empty
        else if (username.equals("") && !password.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter correct username", Toast.LENGTH_LONG).show();
        }
        //Checking if the username is not empty and password is empty
        else if (!username.equals("") && password.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter correct password", Toast.LENGTH_LONG).show();
        }
        //Checking if the both the fields are filled
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Enter both username and password", Toast.LENGTH_LONG).show();
        }
        toggleInputs();
    }

    private void signInWithGoogle() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(mActivity, "Google sign in failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        if (account != null) {

            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                Intent intent = new Intent(mActivity, LocationActivity.class);
                                startActivity(intent);
                                mActivity.finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                Toast.makeText(mActivity, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(mActivity, "No Google Sign IN", Toast.LENGTH_SHORT).show();
        }
    }


    public void signIn (final String email, String password) throws Exception {
        if(mActivity.isNetworkAvailable()) {
            Toast.makeText(mActivity, "Signing In", Toast.LENGTH_SHORT).show();
            final ProgressDialog progressDialog = new ProgressDialog(mActivity);
            progressDialog.setMessage("Verifying your credentials..");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                Log.i(TAG, "Successfully logged in!");
                                Intent intent = new Intent(mActivity, LocationActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                mActivity.finish();

                            } else {
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Log.i(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(mActivity, "Please enter correct username and password",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(mActivity, "NO Internet", Toast.LENGTH_SHORT).show();
        }

    }
}

