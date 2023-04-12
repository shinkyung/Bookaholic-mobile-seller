package com.example.bookaholic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookaholic.details.Book;

public class WishlistFragment extends Fragment implements UserDataChangedListener, BooksDataChangedListener {

    private final String TAG = "WishlistFragment";

    private RecyclerView recyclerView;
    private BookAdapter adapter;

    public WishlistFragment() {

    }

    public static WishlistFragment newInstance() {
        WishlistFragment fragment = new WishlistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        adapter = new BookAdapter(view.getContext(), Book.allBooks);
        adapter.filterByFavorite();
        recyclerView = view.findViewById(R.id.recyclerview_wishlist);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void notifyAdapter() {
        try {
            if (adapter != null) adapter.filterByFavorite();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
    @Override
    public void updateUserRelatedViews() {
        notifyAdapter();
    }

    @Override
    public void updateBooksRelatedViews() {
        notifyAdapter();
    }
}
