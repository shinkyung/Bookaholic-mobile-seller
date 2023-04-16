package com.example.bookaholic.orderHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import static com.example.bookaholic.MainActivity.currentSyncedUser;

import com.example.bookaholic.OrderBook;
import com.example.bookaholic.R;

import java.util.ArrayList;

public class OrderDetail extends AppCompatActivity {
    String position;
    RecyclerView orderDetailRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        loadData();
        ArrayList<OrderBook> orderBooks= currentSyncedUser.getOrderHistory().get(Integer.valueOf(position)).getOrderBooks();
        orderDetailRV = findViewById(R.id.orderDetailRV);

        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this, orderBooks);
        orderDetailRV.setAdapter(orderDetailAdapter);
    }

    private void loadData() {
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        position = bundle.getString("position");
    }
}