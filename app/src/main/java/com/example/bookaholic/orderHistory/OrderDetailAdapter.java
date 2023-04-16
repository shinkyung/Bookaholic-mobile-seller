package com.example.bookaholic.orderHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookaholic.OrderBook;
import com.example.bookaholic.R;
import com.example.bookaholic.details.Book;

import java.util.ArrayList;

        public class OrderDetailAdapter extends RecyclerView.Adapter<com.example.bookaholic.orderHistory.OrderDetailAdapter.ViewHolder> {
            private ArrayList<OrderBook> mDataList;
            private Context context;

            public static class ViewHolder extends RecyclerView.ViewHolder {
                public ImageView bookImageView;
        public TextView titleTxt, quantityTxt, priceTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
        }
    }

    public OrderDetailAdapter(Context context, ArrayList<OrderBook> dataList) {
        this.mDataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderBook orderBook = mDataList.get(position);
        Book book = orderBook.getBook();
        // set image using Glide library
        Glide.with(holder.bookImageView.getContext())
                .load(book.getImages().get(0))
                .into(holder.bookImageView);

        holder.titleTxt.setText(book.getTitle());
        holder.quantityTxt.setText(String.valueOf(orderBook.getQuantity()));
        holder.priceTxt.setText(String.valueOf(book.getPrice() * orderBook.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class OrderDetail {
        private String bookImageUrl, bookTitle;
        private int quantity;
        private double price;

        public OrderDetail(String bookImageUrl, String bookTitle, int quantity, double price) {
            this.bookImageUrl = bookImageUrl;
            this.bookTitle = bookTitle;
            this.quantity = quantity;
            this.price = price;
        }

        public String getBookImageUrl() {
            return bookImageUrl;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}
