package com.example.bookaholic;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookaholic.details.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private RecyclerView mCartRecyclerView;
    private CartAdapter mCartAdapter;
    private TextView mTotalPriceTextView, mShippingFeeTextView, mCartTotalPriceTextView;
    private ArrayList<OrderBook> mBookList;

    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        initConfirmButton();
        ImageButton buttonBack = findViewById(R.id.button_cart_back);
        buttonBack.setOnClickListener(v -> CartActivity.this.finish());

        // Initialize the book list with some sample books
        mBookList = Order.currentOrder.getOrderBooks();

        // Get a reference to the RecyclerView
        mCartRecyclerView = findViewById(R.id.cartRecyclerView);
        mTotalPriceTextView = findViewById(R.id.totalPriceTextView);
        mShippingFeeTextView = findViewById(R.id.shippingFeeTextView);
        mCartTotalPriceTextView = findViewById(R.id.cartTotalPriceTextView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCartRecyclerView.setLayoutManager(layoutManager);

        // Create an instance of the CartAdapter class and pass the book list to its constructor
        mCartAdapter = new CartAdapter(mBookList, this);

        // Set the CartAdapter instance to the RecyclerView
        mCartRecyclerView.setAdapter(mCartAdapter);

        // Calculate the total price
        float totalPrice = 0;
        for (OrderBook orderBook : mBookList) {
            totalPrice += orderBook.getBook().getPrice() * orderBook.getQuantity();
        }
        float shippingFee = 0;
        if (mBookList.size() != 0) {
            shippingFee = 30000;
        }
        float cartTotalPrice = totalPrice + shippingFee;

        mShippingFeeTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(shippingFee) + " đ");
        mTotalPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(totalPrice) + " đ");
        mCartTotalPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(cartTotalPrice) + " đ");
        Order.currentOrder.setTotalPrice((totalPrice));

        mCartAdapter.setOnQuantityChangeListener(new CartAdapter.OnQuantityChangedListener() {
            @Override
            public void onQuantityChanged() {
                // Calculate the total price
                float totalPrice = 0;
                for (OrderBook orderBook : mBookList) {
                    totalPrice += orderBook.getBook().getPrice() * orderBook.getQuantity();
                }
                float shippingFee = 0;
                if (mBookList.size() != 0) {
                    shippingFee = 30000;
                }
                float cartTotalPrice = totalPrice + shippingFee;
                Order.currentOrder.setTotalPrice((totalPrice));
                mShippingFeeTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(shippingFee) + " đ");
                mTotalPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(totalPrice) + " đ");
                mCartTotalPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(cartTotalPrice) + " đ");
            }
        });

        mCartAdapter.setOnDeleteListener(new CartAdapter.onDeleteListener() {
            public void onDelete() {
                // Calculate the total price
                float totalPrice = 0;
                for (OrderBook orderBook : mBookList) {
                    totalPrice += orderBook.getBook().getPrice() * orderBook.getQuantity();
                }
                float shippingFee = 0;
                if (mBookList.size() != 0) {
                    shippingFee = 30000;
                }
                float cartTotalPrice = totalPrice + shippingFee;
                Order.currentOrder.setTotalPrice((totalPrice));
                mShippingFeeTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(shippingFee) + " đ");
                mTotalPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(totalPrice) + " đ");
                mCartTotalPriceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(cartTotalPrice) + " đ");
            }
        });
    }
    public void initConfirmButton() {
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users")
                    .child(MainActivity.currentSyncedUser.getId()).child("orderHistory");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                        ArrayList<Order> orderHistory = new ArrayList<>();
                        for (DataSnapshot orderHistorySnapshot : dataSnapshot.getChildren()) {
                            Order order = orderHistorySnapshot.getValue(Order.class);
                            orderHistory.add(order);
                        }
                        orderHistory.add(Order.currentOrder);

                        myRef.setValue(orderHistory);
                    } else {
                        ArrayList<Order> orderHistory = new ArrayList<>();
                        orderHistory.add(Order.currentOrder);

                        myRef.setValue(orderHistory);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        });
    }
}
