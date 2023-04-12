package com.example.bookaholic;

import static com.example.bookaholic.MainActivity.firebaseAuth;
import static com.example.bookaholic.MainActivity.firebaseUser;
import static com.example.bookaholic.Tools.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private final String TAG = "SignUpActivity";
    private Button btnRegister;
    private TextView btnSignIn;
    private TextInputEditText emailRegister, passRegister, confirmRegister;
    private String email, password, confirm, fullName, phoneNumber;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        emailRegister = findViewById(R.id.editEmailRegister);
        passRegister = findViewById(R.id.editPasswordRegister);
        confirmRegister = findViewById(R.id.editConfirmRegister);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> startSignInActivity());
        btnRegister.setOnClickListener(v -> handleSignUpAccount());
    }

    private void handleSignUpAccount() {
        email = Objects.requireNonNull(emailRegister.getText()).toString();
        password = Objects.requireNonNull(passRegister.getText()).toString();
        String[] parts = email.split("@");
        fullName = parts[0];

        if (checkInputRequirements()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, onCreateAccountCompleteListener);
        }
    }

    private final OnCompleteListener<AuthResult> onCreateAccountCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null)
                    firebaseUser.sendEmailVerification()
                            .addOnCompleteListener(onSendVerificationEmailComplete);
            } else {
                showToast(SignUpActivity.this, R.string.failed_to_register);
            }
        }
    };

    private final OnCompleteListener<Void> onSendVerificationEmailComplete = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                createNewUserInDatabase(fullName, email, phoneNumber);
                showToast(SignUpActivity.this, R.string.register_successful);
                startSignInActivity();
            } else {
                showToast(SignUpActivity.this, R.string.send_verification_email_failed);
            }
        }
    };

    private boolean checkInputRequirements() {

        if (!Tools.emailPatternValidate(email)) {
            showToast(this, R.string.invalid_email);
            return false;
        }

        if (!Tools.checkValidPassword(password)) {
            showToast(this, R.string.invalid_password);
            return false;
        }

        return true;
    }

    private void createNewUserInDatabase(String name, String email, String phoneNumber) {
        User newUser = new User(name, phoneNumber, email);
        newUser.saveToDatabase().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.d(TAG, "Successfully saved new user to database.");
            else
                Log.d(TAG, "Failed to save new user to database");
        });
    }

    private void startSignInActivity() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
