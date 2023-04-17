package com.example.bookaholic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookaholic.details.Book;

public class ManageFragment extends Fragment implements UserDataChangedListener, BooksDataChangedListener{
    private final String TAG = "ManageFragment";

    private RecyclerView recyclerView;
    private ManageBookAdapter adapter;

    public ManageFragment() {

    }

    public static ManageFragment newInstance() {
        ManageFragment fragment = new ManageFragment();
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
        View view = inflater.inflate(R.layout.manage_view, container, false);
        adapter = new ManageBookAdapter(view.getContext(), Book.allBooks);
        recyclerView = view.findViewById(R.id.manageLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
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
