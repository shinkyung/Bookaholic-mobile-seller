package com.example.bookaholic.details;

import static android.content.ContentValues.TAG;
import static com.example.bookaholic.FirebaseHelper.syncCurrentUserFavoriteBooks;
import static com.example.bookaholic.MainActivity.currentSyncedUser;
import static com.example.bookaholic.Tools.showToast;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookaholic.AddBook;
import com.example.bookaholic.CartActivity;
import com.example.bookaholic.Comment;
import com.example.bookaholic.ManageBook;
import com.example.bookaholic.Order;
import com.example.bookaholic.OrderBook;
import com.example.bookaholic.R;
import com.example.bookaholic.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

public class Detail extends AppCompatActivity {
    private ViewGroup imageListView;
    private ImageView imageSelected, returnBtn;
    private TextView descriptionTxt, titleTxt, priceTxt, quantityTxt;
    private RatingBar ratingBar;
    private ImageView addToCartButton;
    private Button addBtn, removeBtn;
    Context context;
    private NotificationBadge shopping_badge;
    private Book currentBook;
    FragmentTransaction fragmentTransaction;
    DetailFragment detailFragment;

    RecyclerView gridView, commentListView;
    int countQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loadData();
        initAddToCartButton();
        initRecommend();
        initBasicInfo();
        initShowButton();
        initComment();
        initFavorite();
        initBookDetail();
        initCurrentUser();
        initShoppingCart();
        initAmountBtn();
        context = this;
        returnBtn = findViewById(R.id.returnBtn);
        int test = getResources().getIdentifier("avatar1", "drawable", getPackageName());
        System.out.println(test);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageListView = findViewById(R.id.imageListView);
        imageSelected = findViewById(R.id.imageSelected);

        ArrayList<String> images = currentBook.getImages();
        Glide.with(context)
                .load(images.get(0))
                .into(imageSelected);
        for (int i = 0; i < images.size(); i++){
            final View singleFrame = getLayoutInflater().inflate(R.layout.image_detail,null);
            singleFrame.setId(i);
            ImageView single_image = (ImageView) singleFrame.findViewById(R.id.single_image);
            Glide.with(context)
                    .load(images.get(i))
                    .into(single_image);
            imageListView.addView(singleFrame);

            singleFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(context)
                            .load(images.get(singleFrame.getId()))
                            .into(imageSelected);
                }
            });
        }
        shopping_badge.setNumber(Order.currentOrder.getOrderSize());
    }
    private void loadData() {
        Intent data = getIntent();
        Bundle bundle = data.getExtras();
        currentBook = Book.findBookByTitle(bundle.getString("bookName"));
        Log.e("TAG", currentBook.getAuthor());
        //Set data to screen

    }
    public void initFavorite(){
        ImageView imageViewHeart = findViewById(R.id.image_view_heart);
        Integer bookID = Book.idOfBookWithName(currentBook.getTitle());
//        if (currentSyncedUser.likeBookWithId(bookID))
            imageViewHeart.setColorFilter(ContextCompat.getColor(Detail.this, R.color.md_theme_light_onSurfaceVariant), PorterDuff.Mode.SRC_IN);
//        else
//            imageViewHeart.setColorFilter(ContextCompat.getColor(Detail.this,R.color.md_theme_light_onSurfaceVariant), PorterDuff.Mode.SRC_IN);

        imageViewHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    int toastMessage;

                    if (currentSyncedUser.likeBookWithId(bookID)) {
                        toastMessage = R.string.removed_from_favorites;
                        currentSyncedUser.unlike(bookID);
                        imageViewHeart.setColorFilter(ContextCompat.getColor(Detail.this,R.color.md_theme_light_onSurfaceVariant), PorterDuff.Mode.SRC_IN);

                    } else {
                        toastMessage = R.string.added_to_favorites;
                        currentSyncedUser.like(bookID);
                        imageViewHeart.setColorFilter(ContextCompat.getColor(Detail.this,R.color.red), PorterDuff.Mode.SRC_IN);
                    }

                    OnCompleteListener<Void> onCompleteListener = task -> {
                        if (task.isSuccessful())
                            showToast(Detail.this, toastMessage);
                    };

                    syncCurrentUserFavoriteBooks(onCompleteListener);

//                    updateAddToFavoriteButton();

                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
//                if (true){
//                    imageViewHeart.setColorFilter(ContextCompat.getColor(Detail.this, R.color.black), PorterDuff.Mode.SRC_IN);
//                }
//                else {
//                    imageViewHeart.setColor(ContextCompat.getColor(Detail.this, R.color.red), PorterDuff.Mode.SRC_IN);
//                }
            }
        });
    }
    public void initBasicInfo(){
        titleTxt = findViewById(R.id.titleTxt);
        priceTxt = findViewById(R.id.priceTxt);
        ratingBar = findViewById(R.id.ratingBar);
        descriptionTxt = findViewById(R.id.descriptionTxt);

        titleTxt.setText(currentBook.getTitle());
        priceTxt.setText(currentBook.getDisplayablePrice());
        ratingBar.setRating(currentBook.getRateAvg());
        descriptionTxt.setText(currentBook.getDescription());
        descriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTextView(descriptionTxt);
            }
        });
    }
    ArrayList<Comment> comments = new ArrayList<>();

    public void initComment(){
        for (int i = 0; i < currentBook.getComments().size() && i < 2; i++){
            comments.add(currentBook.getComments().get(i));
        }
        ReviewAdapter adapter = new ReviewAdapter(this,comments);

        commentListView = findViewById(R.id.commentListView);
        commentListView.setAdapter(adapter);
    }

    public void initRecommend(){
        gridView = findViewById(R.id.gridview);
        GridAdapter adapter = new GridAdapter(this, Book.allBooks);
        gridView.setAdapter(adapter);
    }

    public void initAddToCartButton(){
        addToCartButton = findViewById(R.id.addToCartButton);
        shopping_badge = findViewById(R.id.shopping_badge);

        addToCartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                OrderBook orderBook = new OrderBook(currentBook, countQuantity);
                int toastMessage;
                try {
                    if (Order.currentOrder.checkOrderBook(orderBook)){
                    toastMessage = R.string.added_to_cart;
                    Order.currentOrder.addExistedOrderBook(orderBook);
                    showToast(Detail.this, toastMessage);
                    }
                else {
                    Order.currentOrder.addOrderBook(orderBook);
                    toastMessage = R.string.added_to_cart;
                    shopping_badge.setNumber(Order.currentOrder.getOrderSize());
                    showToast(Detail.this, toastMessage);

                    }

                } catch (Exception e) {
                    Log.e("AddToCart", e.toString());
                }
            }
        });
    }

    private void toggleTextView(TextView textView) {
        if (textView.getMaxLines() == 4) {
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);
        } else {
            textView.setMaxLines(4);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
    private void initBookDetail(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        detailFragment = DetailFragment.newInstance(currentBook.getAuthor(), currentBook.getCategory(), currentBook.getPublicationDate(), currentBook.getTypeOfCover(), currentBook.getSize(), currentBook.getNumberOfPages(), currentBook.getPublisher(), 1);
        fragmentTransaction.replace(R.id.fragmentBookDetail, detailFragment);
        fragmentTransaction.commit();
    }

    private void initShowButton(){
        Button showBottomSheetButton = findViewById(R.id.show_bottom_sheet_button);
        Button showBookDetail = findViewById(R.id.showBookDetail);
        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(currentBook.getComments(), 1, Detail.this);
                bottomSheetFragment.show(getSupportFragmentManager(), BottomSheetFragment.TAG);
            }
        });
        showBookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(currentBook.getAuthor(), currentBook.getCategory(), currentBook.getPublicationDate(), currentBook.getTypeOfCover(), currentBook.getSize(), currentBook.getNumberOfPages(), currentBook.getPublisher(), 2, Detail.this);
                bottomSheetFragment.show(getSupportFragmentManager(), BottomSheetFragment.TAG);
            }

        });

    }
    public void initCurrentUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(Detail.this, SignInActivity.class));
        }
//        Log.d("Test", currentUser.getDisplayName());
    }

    public void initShoppingCart(){
        ImageView shoppingBtn = findViewById(R.id.shoppingBtn);
        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Detail.this, ManageBook.class));
            }
        });
    }

    public void initAmountBtn(){
        addBtn = findViewById(R.id.addBtn);
        removeBtn = findViewById(R.id.removeBtn);
        quantityTxt = findViewById(R.id.quantityTxt);
        quantityTxt.setText(String.valueOf(countQuantity));
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countQuantity <= 1){
                    return;
                } else {
                    countQuantity--;
                    quantityTxt.setText(String.valueOf(countQuantity));
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countQuantity++;
                quantityTxt.setText(String.valueOf(countQuantity));
            }
        });
    }
}
