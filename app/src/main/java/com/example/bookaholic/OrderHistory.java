package com.example.bookaholic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.bookaholic.orderHistory.OrderHistoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends Fragment implements UserDataChangedListener, BooksDataChangedListener {
    RecyclerView orderRV;
    ArrayList<Order> orderI;
    Context context;
    String status;
    Spinner spinner;
    public OrderHistory(Context context) {
        this.context = context;
    }
    public static OrderHistory newInstance(Context context) {
        OrderHistory fragment = new OrderHistory(context);

        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_order_history, null);
        spinner = view.findViewById(R.id.statusOrder);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.statusOrder_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        orderRV = view.findViewById(R.id.incomplete);

        initOrderHistory(view);


        return view;
    }
    public void initOrderHistory(LinearLayout view){
        orderI = new ArrayList<>();
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
                        for(Order order1: user.getOrderHistory()){
                                orderI.add(order1);
                        }
                    }
                    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(context, orderI);
                    orderRV.setAdapter(orderHistoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = parent.getItemAtPosition(position).toString();
                loadOrdersByStatus(status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                status = parent.getItemAtPosition(0).toString();
                loadOrdersByStatus(status);
            }
        });
    }

    private void loadOrdersByStatus(String selectedStatus) {
        ArrayList<Order> filteredOrders = new ArrayList<>();
        for (Order order : orderI) {
            if(selectedStatus.equals("All")){
                filteredOrders.add(order);
            }
            else {
                if (order.getOrderStatus().equals(selectedStatus)) {
                    filteredOrders.add(order);
                }
            }
        }
        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(context, filteredOrders);
        orderRV.setAdapter(orderHistoryAdapter);
    }

    @Override
    public void updateUserRelatedViews() {

    }

    @Override
    public void updateBooksRelatedViews() {

    }
}
