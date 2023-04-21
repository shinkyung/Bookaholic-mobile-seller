package com.example.bookaholic;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookaholic.voucher.Voucher;
import com.example.bookaholic.voucher.VoucherActivity;
import com.example.bookaholic.voucher.VoucherAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    RecyclerView userRV;
    ArrayList<User> users = new ArrayList<>();
    Context context;

    ImageView returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        returnBtn = findViewById(R.id.returnUser);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initOrderHistory();

    }
    public void initOrderHistory(){
        users = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for (DataSnapshot voucherSnapshot : dataSnapshot.getChildren()) {
                        User user = voucherSnapshot.getValue(User.class);
                        users.add(user);
                    }
                    userRV = findViewById(R.id.userRV);
                    UserAdapter userAdapter = new UserAdapter(UserActivity.this, users);

                    userRV.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
