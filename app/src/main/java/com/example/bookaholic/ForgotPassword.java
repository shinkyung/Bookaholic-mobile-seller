package com.example.bookaholic;

import static android.content.ContentValues.TAG;
import static com.example.bookaholic.MainActivity.firebaseAuth;
import static com.example.bookaholic.Tools.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {
    private Button resetPassword;
    private TextInputEditText emailForgot;
    private String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPassword = findViewById(R.id.btnForgot);
        emailForgot = findViewById(R.id.editEmailForgot);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Objects.requireNonNull(emailForgot.getText()).toString();

                boolean validInputs = !email.equals("");

                if (!validInputs) {
                    showToast(ForgotPassword.this, R.string.empty_text);
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Password reset email sent successfully");
                                Toast.makeText(getApplicationContext(), "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotPassword.this, SignInActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                Log.w(TAG, "Failed to send password reset email", task.getException());
                                Toast.makeText(getApplicationContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
