package nshmadhani.com.wakenbake.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nshmadhani.com.wakenbake.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    public static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    public EditText newPassword;
    public EditText confirmPassword;
    public Button updatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        updatePassword = findViewById(R.id.updateButton);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPasswordOfUser = newPassword.getText().toString();
                String confirmPasswordOfUser = confirmPassword.getText().toString();

                if (newPasswordOfUser.equals(confirmPasswordOfUser)) {
                    resetPassword(user, newPasswordOfUser);
                }
            }
        });
    }

    private void resetPassword(FirebaseUser user, String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
