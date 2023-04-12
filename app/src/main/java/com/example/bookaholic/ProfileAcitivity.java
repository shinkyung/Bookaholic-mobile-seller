package com.example.bookaholic;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookaholic.SignInActivity;
import com.example.bookaholic.AddBook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileAcitivity extends AppCompatActivity {
    private ImageView profilePictureImageView;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView userBioTextView;
    private Button addBook, logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acitivity);

        profilePictureImageView = findViewById(R.id.profile_picture);
        userNameTextView = findViewById(R.id.user_name);
        userEmailTextView = findViewById(R.id.user_email);
//        userBioTextView = findViewById(R.id.user_bio);

        addBook = findViewById(R.id.add_book_button);
        logOut = findViewById(R.id.logout_button);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            Log.d(TAG, "User's email: " + email);
            userEmailTextView.setText(email);
        } else {
            Log.d(TAG, "No user logged in");
        }
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileAcitivity.this, AddBook.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileAcitivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}