package com.example.bookaholic;

import static android.content.ContentValues.TAG;
import static com.example.bookaholic.FirebaseHelper.initBooksDatabaseReference;
import static com.example.bookaholic.FirebaseHelper.initCurrentUserDatabaseReference;

import com.example.bookaholic.details.Book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

interface UserDataChangedListener {
    void updateUserRelatedViews();
}

interface BooksDataChangedListener {
    void updateBooksRelatedViews();
}

public class MainActivity extends AppCompatActivity {

    public static FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    public static User currentSyncedUser;

    private TextView home, manage, settings;

    private ImageButton buttonHome;
    private ImageButton buttonFavorite;
    private ImageButton buttonProfile;
    private Fragment  fragmentProfile;
    private OrderHistory fragmentHome;
    private ManageFragment fragmentManage;

    private UserDataChangedListener userDataChangedListener;
    private BooksDataChangedListener booksDataChangedListener;
    public static UserDataChangedListener listenerForBookDetailsActivity, listenerForBookClassifyActivity;
    private NotificationBadge shopping_badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        fragmentHome = new OrderHistory(MainActivity.this);
        fragmentProfile  = new ProfileFragment();
        fragmentManage = new ManageFragment();
        buttonHome = findViewById(R.id.bottomNavBarButtonHome);
        buttonFavorite = findViewById(R.id.bottomNavBarButtonFavorite);
        buttonProfile = findViewById(R.id.bottomNavBarButtonProfile);

        home = findViewById(R.id.home);
        manage = findViewById(R.id.wishlist);
        settings = findViewById(R.id.profile);

        buttonHome.setOnClickListener(onBottomNavBarButtonClicked);
        buttonFavorite.setOnClickListener(onBottomNavBarButtonClicked);
        buttonProfile.setOnClickListener(onBottomNavBarButtonClicked);
        InitShoppingCartListener();
        setUpDefaultFragment();
    }

    private void setUpDefaultFragment() {
        resetSelectedBottomNavbarButton();
        buttonHome.setImageResource(R.drawable.home_selected);
        userDataChangedListener = fragmentHome;
        booksDataChangedListener = fragmentHome;
        switchFragment(R.id.fragmentcontainerMainActivity, fragmentHome);
    }

    private final View.OnClickListener onBottomNavBarButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetSelectedBottomNavbarButton();
            int viewId = view.getId();
            if (viewId == R.id.bottomNavBarButtonHome) {
                Log.d(TAG, "Home button clicked!");
                buttonHome.setImageResource(R.drawable.home_selected);
                home.setTextColor(Color.parseColor("#7041EE"));
                manage.setTextColor(Color.GRAY);
                settings.setTextColor(Color.GRAY);
                home.setTypeface(null, Typeface.BOLD);
                manage.setTypeface(null, Typeface.NORMAL);
                settings.setTypeface(null, Typeface.NORMAL);
                buttonProfile.setColorFilter(Color.parseColor("#B2B2B2"));
                userDataChangedListener = fragmentHome;
                booksDataChangedListener = fragmentHome;
                switchFragment(R.id.fragmentcontainerMainActivity, fragmentHome);
            } else if (viewId == R.id.bottomNavBarButtonFavorite) {
                Log.d(TAG, "Favorite button clicked!");
                buttonFavorite.setImageResource(R.drawable.favorite_selected);
                manage.setTextColor(Color.parseColor("#7041EE"));
                home.setTextColor(Color.GRAY);
                settings.setTextColor(Color.GRAY);
                manage.setTypeface(null, Typeface.BOLD);
                home.setTypeface(null, Typeface.NORMAL);
                settings.setTypeface(null, Typeface.NORMAL);
                buttonProfile.setColorFilter(Color.parseColor("#B2B2B2"));
                userDataChangedListener = fragmentManage;
                booksDataChangedListener = fragmentManage;
                switchFragment(R.id.fragmentcontainerMainActivity, fragmentManage);
            } else if (viewId == R.id.bottomNavBarButtonProfile) {
                Log.d(TAG, "Profile button clicked!");
                settings.setTextColor(Color.parseColor("#7041EE"));
                manage.setTextColor(Color.GRAY);
                home.setTextColor(Color.GRAY);
                settings.setTypeface(null, Typeface.BOLD);
                manage.setTypeface(null, Typeface.NORMAL);
                home.setTypeface(null, Typeface.NORMAL);
                buttonProfile.setImageResource(R.drawable.profile_selected);
                buttonProfile.setColorFilter(Color.parseColor("#7041EE"));
                switchFragment(R.id.fragmentcontainerMainActivity, fragmentProfile);
            } else {
                Log.d(TAG, "Don't know which button clicked!");
            }
        }
    };

    private void resetSelectedBottomNavbarButton() {
        buttonHome.setImageResource(R.drawable.home_unselected);
        buttonFavorite.setImageResource(R.drawable.favorite_unselected);
        buttonProfile.setImageResource(R.drawable.profile_unselected);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!Tools.isOnline(this)) {
            startActivity(new Intent(this, NoInternetActivity.class));
        } else {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            } else {
                initCurrentUserDatabaseReference(currentUserDatabaseListener);
                initBooksDatabaseReference(booksDatabaseListener);
            }
        }
    }

    private final ValueEventListener currentUserDatabaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(TAG, "Detected a data change of current user on the database.");
            currentSyncedUser = snapshot.getValue(User.class);
//            Log.d("test",currentSyncedUser.getEmail());
            userDataChangedListener.updateUserRelatedViews();
            if (listenerForBookDetailsActivity != null)
                listenerForBookDetailsActivity.updateUserRelatedViews();
            if (listenerForBookClassifyActivity != null)
                listenerForBookClassifyActivity.updateUserRelatedViews();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, error.toString());
        }
    };

    private final ValueEventListener booksDatabaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(TAG, "All books database changed.");
            Book.allBooks = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Book.allBooks.add(dataSnapshot.getValue(Book.class));
            }
            booksDataChangedListener.updateBooksRelatedViews();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, error.toString());
        }
    };

    private void switchFragment(int fragmentContainerResourceId, Fragment fragmentObject) {
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainerResourceId, fragmentObject)
                .commit();
    }
    private void InitShoppingCartListener() {
        Order order = new Order();
    }
}