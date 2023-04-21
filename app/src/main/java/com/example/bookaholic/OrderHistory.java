package com.example.bookaholic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.bookaholic.orderHistory.OrderHistoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistory extends Fragment implements UserDataChangedListener, BooksDataChangedListener {
    RecyclerView orderRV, orderRVC;
    ArrayList<Order> orderI, orderC;
    Context context;
    public OrderHistory(Context context) {
        this.context = context;
    }
    public static OrderHistory newInstance(Context context) {
        OrderHistory fragment = new OrderHistory(context);

        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_order_history, null);

        initOrderHistory(view);


        return view;
    }
    public void initOrderHistory(LinearLayout view){
        orderC = new ArrayList<>();
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
                            if(order1.getOrderStatus().contains("Incomplete")){
                                orderI.add(order1);
                            }
                            else{
                                orderC.add(order1);
                            }
                        }
                    }
                    orderRV = view.findViewById(R.id.incomplete);
                    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(context, orderI);
                    orderRV.setAdapter(orderHistoryAdapter);

                    orderRVC = view.findViewById(R.id.complete);
                    OrderHistoryAdapter orderHistoryCAdapter = new OrderHistoryAdapter(context, orderC);
                    orderRVC.setAdapter(orderHistoryCAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

    }
    @Override
    public void updateUserRelatedViews() {

    }

    @Override
    public void updateBooksRelatedViews() {

    }
}
