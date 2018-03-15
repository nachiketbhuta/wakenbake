package nshmadhani.com.wakenbake.fragments;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nshmadhani.com.wakenbake.R;
import nshmadhani.com.wakenbake.activities.LocationActivity;
import nshmadhani.com.wakenbake.activities.LoginActivity;

/**
 * Created by Nisha on 3/15/2018.
 */

public class LoginFragment extends Fragment {


    private TextInputLayout mUserNameTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private Button mLoginButton;

    private FirebaseAuth mAuth;

    private LoginActivity mActivity;


    public static final String TAG = LoginFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.login_fragment, container, false);

        mUserNameTextInputLayout = rootView.findViewById(R.id.username_til);
        mPasswordTextInputLayout = rootView.findViewById(R.id.password_til);
        mLoginButton = rootView.findViewById(R.id.login_bt);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click");
                attemptLogin();
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (LoginActivity) getActivity();

        mAuth = mActivity.getmAuth();
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

