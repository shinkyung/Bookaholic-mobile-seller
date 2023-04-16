package com.example.bookaholic.orderHistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookaholic.Comment;
import com.example.bookaholic.Order;
import com.example.bookaholic.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private ArrayList<Order> mDataList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTxt, statusTxt, addressTxt, totalTxt, quantityTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            totalTxt = itemView.findViewById(R.id.totalTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
        }
    }

    public OrderHistoryAdapter(Context context, ArrayList<Order> dataList) {
        this.mDataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_item, parent, false);
        return new OrderHistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHistoryAdapter.ViewHolder holder, int position) {
        Order order = mDataList.get(position);
        System.out.println(order.getCreatedAt());
        holder.dateTxt.setText(order.getCreatedAt());
        holder.statusTxt.setText(order.getOrderStatus());
        holder.addressTxt.setText(order.getAddress());
        holder.totalTxt.setText(order.getTotalPrice().toString());
        holder.quantityTxt.setText(order.quantity().toString());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, OrderDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("position", String.valueOf(position));
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyData {
        private String createdAt, address, orderStatus;
        private float totalPrice;
        private Integer quantity;

        public MyData(String createdAt, String address, String orderStatus, float totalPrice, Integer quantity) {
            this.createdAt = createdAt;
            this.address = address;
            this.orderStatus = orderStatus;
            this.totalPrice = totalPrice;
            this.quantity = quantity;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getAddress() {
            return address;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}