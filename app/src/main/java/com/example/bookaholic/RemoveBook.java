package com.example.bookaholic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookaholic.details.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RemoveBook extends AppCompatActivity {
    DatabaseReference database;
    private Book selectedBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance().getReference();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_remove_book);

        selectedBook = (Book) getIntent().getSerializableExtra("selectedBook");

        Button btnNo = dialog.findViewById(R.id.btn_no);
        Button btnYes = dialog.findViewById(R.id.btn_yes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa sách ra khỏi Firebase
                database.child("Books").child(selectedBook.getId()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(RemoveBook.this, ManageBook.class));
                            }
                        });
            }
        });
        dialog.show();
    }
}