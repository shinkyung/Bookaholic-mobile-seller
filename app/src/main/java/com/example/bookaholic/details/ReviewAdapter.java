package com.example.bookaholic.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookaholic.Comment;
import com.example.bookaholic.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Comment> mDataList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarItem;
        public TextView nameItem;
        public TextView contentItem;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarItem = itemView.findViewById(R.id.avatarItem);
            nameItem = itemView.findViewById(R.id.nameItem);
            contentItem = itemView.findViewById(R.id.contentItem);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    public ReviewAdapter(Context context, ArrayList<Comment> dataList) {
        this.mDataList = dataList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mDataList.get(position);
        Glide.with(context)
                .load(comment.getAvatar())
                .into(holder.avatarItem);
        holder.nameItem.setText(comment.getName());
        holder.contentItem.setText(comment.getContent());
        holder.ratingBar.setRating(comment.getRate());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyData {
        private int mAvatar;
        private String mName;
        private String mContent;
        private float mRating;

        public MyData(int avatar, String name, String content, float rating) {
            mAvatar = avatar;
            mName = name;
            mContent = content;
            mRating = rating;
        }

        public int getAvatar() {
            return mAvatar;
        }

        public String getName() {
            return mName;
        }

        public String getContent() {
            return mContent;
        }

        public float getRating() {
            return mRating;
        }
    }
}

