package com.example.bookaholic;

import static android.content.ContentValues.TAG;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookaholic.details.Book;
import com.example.bookaholic.details.Detail;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<OrderBook> mBookList;
    private Context context;
    private OnQuantityChangedListener onQuantityChangedListener;
    private onDeleteListener onDeleteListener;


    public CartAdapter(ArrayList<OrderBook> bookList, Context context) {
        this.context = context;
        mBookList = bookList;
    }

    public interface onDeleteListener {
        void onDelete();
    }
    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookImageView;
        public TextView bookNameTextView;
        public TextView bookQuantityTextView;
        public TextView bookPriceTextView;
        public TextView errorMessageTextView;
        public Button increaseQuantityButton, decreaseQuantityButton, deleteButton;
        private ConstraintLayout layout;

        public CartViewHolder(View itemView) {
            super(itemView);
            Log.e("CartViewHolder", "CartViewHolder: hello");
            bookImageView = itemView.findViewById(R.id.bookImageView);
            bookNameTextView = itemView.findViewById(R.id.bookNameTextView);
            bookQuantityTextView = itemView.findViewById(R.id.bookQuantityTextView);
            bookPriceTextView = itemView.findViewById(R.id.bookPriceTextView);
            increaseQuantityButton = itemView.findViewById(R.id.bookQuantityIncreaseButton);
            decreaseQuantityButton = itemView.findViewById(R.id.bookQuantityDecreaseButton);
            deleteButton = itemView.findViewById(R.id.bookDeleteButton);
            errorMessageTextView = itemView.findViewById(R.id.errorMessageTextView);
            layout = itemView.findViewById(R.id.cartItemLayout);
        }
    }
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        OrderBook orderBook = mBookList.get(position);

        holder.bookNameTextView.setText(orderBook.getBook().getTitle());
        holder.bookQuantityTextView.setText(String.valueOf(orderBook.getQuantity()));
        holder.bookPriceTextView.setText(orderBook.getBook().displayablePrice());
        Glide.with(context)
                .load(orderBook.book.getImages().get(0))
                .into(holder.bookImageView);
        holder.layout.setOnClickListener(v -> startBookDetailsActivity(orderBook.book));

        holder.increaseQuantityButton.setOnClickListener(v -> {
            if (orderBook.getQuantity() == orderBook.getBook().getQuantity().intValue()) {
                holder.errorMessageTextView.setText("Quantity limit exceeded");
                holder.errorMessageTextView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.errorMessageTextView.setVisibility(View.GONE);
                    }
                }, 2000); // 2 seconds delay
                return;
            }
            orderBook.increaseQuantity();
            holder.bookQuantityTextView.setText(String.valueOf(orderBook.getQuantity()));
            notifyDataSetChanged();

            if (onQuantityChangedListener != null)
                onQuantityChangedListener.onQuantityChanged();
        });
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (orderBook.getQuantity() == 1)
                return;
            orderBook.decreaseQuantity();
            holder.bookQuantityTextView.setText(String.valueOf(orderBook.getQuantity()));
            notifyDataSetChanged();

            if (onQuantityChangedListener != null)
                onQuantityChangedListener.onQuantityChanged();
        });

        holder.deleteButton.setOnClickListener(v -> {
            mBookList.remove(position);
            notifyDataSetChanged();
            if (onDeleteListener != null)
                onDeleteListener.onDelete();
        });
    }
    private void startBookDetailsActivity(Book book) {
        try {
            Intent intent = new Intent(context, Detail.class);
            Bundle bundle = new Bundle();
            bundle.putString("bookName", book.getTitle());
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public void setOnDeleteListener(onDeleteListener listener) {
        onDeleteListener = listener;
    }
    public void setOnQuantityChangeListener(OnQuantityChangedListener listener) {
        onQuantityChangedListener = listener;
    }


}
