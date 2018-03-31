package nshmadhani.com.wakenbake.Fragments;

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

import nshmadhani.com.wakenbake.Activities.LoginActivity;
import nshmadhani.com.wakenbake.Activities.OtpActivity;
import nshmadhani.com.wakenbake.R;


public class SignUpFragment extends Fragment {

    private TextInputLayout mUserNameTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;
    private LoginActivity mActivity;
    public static final String TAG = SignUpFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.signup_fragment, container, false);

       // mUserNameTextInputLayout = rootView.findViewById(R.id.signupUsername_til);
        mPasswordTextInputLayout =  rootView.findViewById(R.id.signupPassword_til);
        mEmailTextInputLayout = rootView.findViewById(R.id.signupEmail_til);
        mSignUpButton = rootView.findViewById(R.id.signupButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
        return rootView;
    }

    public void toggleInputs(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mUserNameTextInputLayout.getEditText().setEnabled(!mUserNameTextInputLayout.getEditText().isEnabled());
                mPasswordTextInputLayout.getEditText().setEnabled(!mPasswordTextInputLayout.getEditText().isEnabled());
                mEmailTextInputLayout.getEditText().setEnabled(!mEmailTextInputLayout.getEditText().isEnabled());
                mSignUpButton.setEnabled(!mSignUpButton.isEnabled());
            }
        });
    }
    private void attemptSignup() {
        toggleInputs();

        //String username = mUserNameTextInputLayout.getEditText().getText().toString();
        String password = mPasswordTextInputLayout.getEditText().getText().toString();
        String email = mEmailTextInputLayout.getEditText().getText().toString();
        if ( !password.equals("")) {
            try {
                Toast.makeText(mActivity, "All is Cool", Toast.LENGTH_SHORT).show();
                signUp(email, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Checking if username is empty and password is not empty
        else if (!password.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter correct username", Toast.LENGTH_LONG).show();
        }
        //Checking if the username is not empty and password is empty
        else if ( password.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Enter correct password", Toast.LENGTH_LONG).show();
        }
        //Checking if the both the fields are filled
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Enter both username and password", Toast.LENGTH_LONG).show();
        }
        toggleInputs();
    }
    public void signUp(String email,String password) {
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("Creating Account..\nThis might take a while...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(mActivity, OtpActivity.class);
                            startActivity(intent);
                            mActivity.finish();

                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (LoginActivity) getActivity();
        mAuth = mActivity.getmAuth();
    }
}
