package com.example.bookaholic.orderHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookaholic.MainActivity;
import com.example.bookaholic.Order;
import com.example.bookaholic.OrderBook;
import com.example.bookaholic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

interface OnOrderDataLoadedListener {
    void onOrderDataLoaded(ArrayList<OrderBook> orders);
}

public class OrderDetail extends AppCompatActivity {
    ArrayList<OrderBook> orders;
    RecyclerView orderDetailRV;
    OnOrderDataLoadedListener listener;
    String orderOwner, orderId;
    Button confirmButton, denyButton;
    TextView statusText;
    private ImageView returnBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        returnBtn = findViewById(R.id.returnOrder);
        statusText = findViewById(R.id.statusTxt);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadData(listener);
        initConfirmButton();
        initDenyButton();
    }

    private void loadData(OnOrderDataLoadedListener listener) {
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        orderOwner = bundle.getString("orderOwner");
        orderId = bundle.getString("orderID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users").child(orderOwner).child("orderHistory").child(orderId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    System.out.println("Hello World");
                }
                Order order = snapshot.getValue(Order.class);
                orders = order.getOrderBooks();
                orderDetailRV = findViewById(R.id.orderDetailRV);

                OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(OrderDetail.this, orders);
                orderDetailRV.setAdapter(orderDetailAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initConfirmButton(){
        confirmButton = findViewById(R.id.confirmButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference statusRef = database.getReference("Users").child(orderOwner).child("orderHistory").child(orderId).child("orderStatus");


        confirmButton.setOnClickListener(view -> {
            statusRef.setValue("Completed");
            startActivity(new Intent(OrderDetail.this, MainActivity.class));
        });
    }

    public void initDenyButton(){
        denyButton = findViewById(R.id.denyButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference statusRef = database.getReference("Users").child(orderOwner).child("orderHistory").child(orderId).child("orderStatus");


        denyButton.setOnClickListener(view -> {
            statusRef.setValue("Denied");
            startActivity(new Intent(OrderDetail.this, MainActivity.class));
        });
    }
}