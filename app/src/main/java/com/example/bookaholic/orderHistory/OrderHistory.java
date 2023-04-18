package com.example.bookaholic.orderHistory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bookaholic.MainActivity;
import com.example.bookaholic.Order;
import com.example.bookaholic.R;
import com.example.bookaholic.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {
    RecyclerView orderRV;
    ArrayList<Order> order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        if (MainActivity.currentSyncedUser.getOrderHistory() == null)
            Log.e("Error", "Sai r");
        else {
            initOrderHistory();
        }
    }

    public void initOrderHistory(){
        order = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user.getOrderHistory() == null){
                            continue;
                        }
                        order.addAll(user.getOrderHistory());
                    }
                    orderRV = findViewById(R.id.orderRV);
                    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(OrderHistory.this, order);
                    orderRV.setAdapter(orderHistoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

    }
}
