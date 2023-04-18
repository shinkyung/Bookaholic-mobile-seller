package com.example.bookaholic;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookaholic.details.Book;

import java.util.ArrayList;

public class ManageFragment extends Fragment implements UserDataChangedListener, BooksDataChangedListener{
    private final String TAG = "ManageFragment";

    private RecyclerView recyclerView;
    private ImageButton buttonFilter;

    private ManageBookAdapter adapter;
    private SearchView searchView;

    private Button buttonTypeScience, buttonTypeRomantic, buttonTypeMystery,
            buttonTypeHorror, buttonTypeSelfHelp, buttonTypeShortStories,
            buttonTypeCook, buttonTypeEssay, buttonTypeHistory;

    private Button filterConfirmButton, filterResetButton;
    private EditText inputMinPrice, inputMaxPrice;
    ArrayList<String> selectedType = new ArrayList<>();
    Integer minPrice = null, maxPrice = null;
    private ScrollView filterContainer;


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
        recyclerView = view.findViewById(R.id.recyclerview_manage);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = view.findViewById(R.id.searchview_manage);
        searchView.setOnQueryTextListener(searchQueryTextListener);


        filterContainer = view.findViewById(R.id.filters_container_manage);

        filterConfirmButton = view.findViewById(R.id.button_filter_confirm_manage);
        filterResetButton = view.findViewById(R.id.button_filter_reset_manage);
        filterConfirmButton.setOnClickListener(v -> onConfirm());
        filterResetButton.setOnClickListener(v -> resetFilters());

        buttonTypeScience = view.findViewById(R.id.button_select_filter_type_science_manage);
        buttonTypeHorror = view.findViewById(R.id.button_select_filter_type_horror_manage);
        buttonTypeCook = view.findViewById(R.id.button_select_filter_type_cookbooks_manage);
        buttonTypeEssay = view.findViewById(R.id.button_select_filter_type_essay_manage);
        buttonTypeHistory = view.findViewById(R.id.button_select_filter_type_history_manage);
        buttonTypeShortStories = view.findViewById(R.id.button_select_filter_type_shortstories_manage);
        buttonTypeMystery = view.findViewById(R.id.button_select_filter_type_mystery_manage);
        buttonTypeRomantic = view.findViewById(R.id.button_select_filter_type_romance_manage);

        buttonTypeScience.setOnClickListener(filterSelectListener);
        buttonTypeHorror.setOnClickListener(filterSelectListener);
        buttonTypeCook.setOnClickListener(filterSelectListener);
        buttonTypeEssay.setOnClickListener(filterSelectListener);
        buttonTypeHistory.setOnClickListener(filterSelectListener);
        buttonTypeShortStories.setOnClickListener(filterSelectListener);
        buttonTypeMystery.setOnClickListener(filterSelectListener);
        buttonTypeRomantic.setOnClickListener(filterSelectListener);

        inputMinPrice = view.findViewById(R.id.edittextMinimumPrice_manage);
        inputMaxPrice = view.findViewById(R.id.edittextMaximumPrice_manage);


        buttonFilter = view.findViewById(R.id.button_filter_manage);
        buttonFilter.setOnClickListener(v -> showFilterMenu());

        return view;
    }

    View.OnClickListener filterSelectListener = v -> {
        if (v.getId() == R.id.button_select_filter_type_science_manage) {
            if (selectedType.contains("science")) selectedType.remove("science");
            else selectedType.add("science");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_romance_manage) {
            if (selectedType.contains("romance")) selectedType.remove("romance");
            else selectedType.add("romance");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_mystery_manage) {
            if (selectedType.contains("mystery")) selectedType.remove("mystery");
            else selectedType.add("mystery");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_horror_manage) {
            if (selectedType.contains("horror")) selectedType.remove("horror");
            else selectedType.add("horror");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_shortstories_manage) {
            if (selectedType.contains("shortstories")) selectedType.remove("shortstories");
            else selectedType.add("shortstories");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_history_manage) {
            if (selectedType.contains("history")) selectedType.remove("history");
            else selectedType.add("history");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_essay_manage) {
            if (selectedType.contains("essay")) selectedType.remove("essay");
            else selectedType.add("essay");
            updateTypeFilterButtons();
        } else if (v.getId() == R.id.button_select_filter_type_cookbooks_manage) {
            if (selectedType.contains("cookbooks")) selectedType.remove("cookbooks");
            else selectedType.add("cookbooks");
            updateTypeFilterButtons();
        }
    };

    private void resetFilters() {
        selectedType.clear();
        minPrice = null;
        maxPrice = null;
        updateTypeFilterButtons();
        inputMinPrice.setText("");
        inputMaxPrice.setText("");
    }

    void updateTypeFilterButtons() {
        if (selectedType.contains("romance")) {
            buttonTypeRomantic.setBackgroundColor(Color.BLACK);
            buttonTypeRomantic.setTextColor(Color.WHITE);
        } else {
            buttonTypeRomantic.setBackgroundColor(Color.WHITE);
            buttonTypeRomantic.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("mystery")) {
            buttonTypeMystery.setBackgroundColor(Color.BLACK);
            buttonTypeMystery.setTextColor(Color.WHITE);
        } else {
            buttonTypeMystery.setBackgroundColor(Color.WHITE);
            buttonTypeMystery.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("science")) {
            buttonTypeScience.setBackgroundColor(Color.BLACK);
            buttonTypeScience.setTextColor(Color.WHITE);
        } else {
            buttonTypeScience.setBackgroundColor(Color.WHITE);
            buttonTypeScience.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("horror")) {
            buttonTypeHorror.setBackgroundColor(Color.BLACK);
            buttonTypeHorror.setTextColor(Color.WHITE);
        } else {
            buttonTypeHorror.setBackgroundColor(Color.WHITE);
            buttonTypeHorror.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("shortstories")) {
            buttonTypeShortStories.setBackgroundColor(Color.BLACK);
            buttonTypeShortStories.setTextColor(Color.WHITE);
        } else {
            buttonTypeShortStories.setBackgroundColor(Color.WHITE);
            buttonTypeShortStories.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("history")) {
            buttonTypeHistory.setBackgroundColor(Color.BLACK);
            buttonTypeHistory.setTextColor(Color.WHITE);
        } else {
            buttonTypeHistory.setBackgroundColor(Color.WHITE);
            buttonTypeHistory.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("essay")) {
            buttonTypeEssay.setBackgroundColor(Color.BLACK);
            buttonTypeEssay.setTextColor(Color.WHITE);
        } else {
            buttonTypeEssay.setBackgroundColor(Color.WHITE);
            buttonTypeEssay.setTextColor(Color.BLACK);
        }
        if (selectedType.contains("cookbooks")) {
            buttonTypeCook.setBackgroundColor(Color.BLACK);
            buttonTypeCook.setTextColor(Color.WHITE);
        } else {
            buttonTypeCook.setBackgroundColor(Color.WHITE);
            buttonTypeCook.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void updateBooksRelatedViews() {
        notifyAdapter();
    }

    private void hideFilterMenu() {
        Log.d(TAG, "hide filter now");
        ObjectAnimator animator = ObjectAnimator.ofFloat(filterContainer, "translationX", 0);
        animator.setDuration(500);
        animator.start();
    }

    private void showFilterMenu() {
        Log.d(TAG, "filter button clicked");
        ObjectAnimator animator = ObjectAnimator.ofFloat(filterContainer, "translationX", -filterContainer.getWidth());
        animator.setDuration(500);
        animator.start();
    }

    private void onConfirm() {
        hideFilterMenu();
        String minPriceString = inputMinPrice.getText().toString();
        String maxPriceString = inputMaxPrice.getText().toString();

        if (!minPriceString.isEmpty())
            minPrice = Integer.parseInt(inputMinPrice.getText().toString());

        if (!maxPriceString.isEmpty())
            maxPrice = Integer.parseInt(inputMaxPrice.getText().toString());

        adapter.filterByOptions(selectedType, minPrice, maxPrice);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyAdapter() {
        adapter.setBooks(Book.allBooks);
        adapter.notifyDataSetChanged();
    }

    private final SearchView.OnQueryTextListener searchQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.filterByName(newText);
            return false;
        }
    };

    @Override
    public void updateUserRelatedViews() {
    }

}
