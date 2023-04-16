package com.example.bookaholic.details;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;


import com.example.bookaholic.Comment;
import com.example.bookaholic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Review extends Dialog {

    private float userRate = 0;
    private Book book;

    public Review(@NonNull Context context) {
        super(context);
    }

    public Review(@NonNull Context context, Book book) {
        super(context);
        this.book = book;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_dialog);

        final AppCompatButton rateNowBtn = findViewById(R.id.rateNowBtn);
        final AppCompatButton laterBtn = findViewById(R.id.laterBtn);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final ImageView ratingImage = findViewById(R.id.ratingImage);
        final EditText contentReview = findViewById(R.id.content);

        rateNowBtn.setOnClickListener(v -> {
            String content = contentReview.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Books")
                    .child(book.getId()).child("comments");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                        Comment newComment = new Comment("Hi", "hao", "https://firebasestorage.googleapis.com/v0/b/bookaholic-82677.appspot.com/o/avatar.png?alt=media&token=4254ac57-c606-46d2-a076-2a8770260f3d", 4);
                        ArrayList<Comment> comments = new ArrayList<>();
                        for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                            Comment comment = commentSnapshot.getValue(Comment.class);
                            comments.add(comment);
                        }
                        comments.add(newComment);

                        myRef.setValue(comments);
                    } else {
                        Comment comment = new Comment("Hello", "hao", "https://firebasestorage.googleapis.com/v0/b/bookaholic-82677.appspot.com/o/avatar.png?alt=media&token=4254ac57-c606-46d2-a076-2a8770260f3d", 4);
                        ArrayList<Comment> comments = new ArrayList<>();
                        comments.add(comment);
                        myRef.setValue(comments);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });

            System.out.println(myRef);
            // Push new data to the database
//                String data = "Hello, Firebase!";
//            DatabaseReference newRef = myRef.push();
//
//            Comment comment = new Comment(newRef.getKey(), content, "1",
//                    book.getId(), "", userRate, false, "NgocHai");

//            newRef.setValue(comment);
            dismiss();
        });

        laterBtn.setOnClickListener(v -> dismiss());

//        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
//            if (rating <= 1) {
//                ratingImage.setImageResource(R.drawable.ic_1_star);
//            } else if (rating <= 2) {
//                ratingImage.setImageResource(R.drawable.ic_2_star);
//            } else if (rating <= 3) {
//                ratingImage.setImageResource(R.drawable.ic_3_star);
//            } else if (rating <= 4) {
//                ratingImage.setImageResource(R.drawable.ic_4_star);
//            } else {
//                ratingImage.setImageResource(R.drawable.ic_5_star);
//            }
//
//            animateImage(ratingImage);
//            userRate = rating;
//        });
    }

    private void animateImage(ImageView ratingImage) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }
}