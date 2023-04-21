package com.example.bookaholic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<User> userList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public TextView fullnameTextView;
        public TextView emailTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            fullnameTextView = itemView.findViewById(R.id.fullnameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(context).load(user.getAvatar()).into(holder.userImage);
        holder.fullnameTextView.setText(user.getFullName());
        holder.emailTextView.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
