package com.example.bookaholic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.bookaholic.MainActivity.firebaseAuth;
import static com.example.bookaholic.MainActivity.firebaseUser;
import static com.example.bookaholic.Tools.showToast;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class SignInActivity extends AppCompatActivity{
    private ProgressBar progressBarSignIn;
    private static final String TAG = "SignInActivity";
    private Button btnLogin;
    private TextView btnSignUp, forgetPass;
    private TextInputEditText emailLogin, passLogin;
    private String email, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        emailLogin = findViewById(R.id.editEmailLogin);
        passLogin = findViewById(R.id.editPasswordLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        forgetPass = findViewById(R.id.forgotLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Objects.requireNonNull(emailLogin.getText()).toString();
                password = Objects.requireNonNull(passLogin.getText()).toString();

                boolean validInputs = !email.equals("") && !password.equals("");

                if (!validInputs) {
                    showToast(SignInActivity.this, R.string.empty_text);
                } else {
                    if (firebaseAuth != null)
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(onSignInComplete);
                    else
                        Log.d(TAG, "handleSignInSubmit: firebaseAuth is null");
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    private final OnCompleteListener<AuthResult> onSignInComplete = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    if (!firebaseUser.isEmailVerified())
                        showToast(SignInActivity.this, R.string.you_havent_verify_email);
                    else {

                        showToast(SignInActivity.this, R.string.sign_in_sucess);
                        startMainActivity();
                    }
                }
            } else showToast(SignInActivity.this, R.string.sign_in_failed);
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
