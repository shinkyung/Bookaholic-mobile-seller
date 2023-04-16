package com.example.bookaholic.details;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.example.bookaholic.Comment;
import com.example.bookaholic.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetFragment";
    private String mAuthorContent, mCategoryContent, mDateContent, mCoverTypeContent, mSizeContent, mPublisher;
    private int mNumberPageContent;

    private CardView mCardView;
    FragmentTransaction fragmentManager;
    Integer type;
    ArrayList<Comment> comments;
    Context context;
    public BottomSheetFragment(ArrayList<Comment> comments, Integer type, Context context) {
        this.comments = comments;
        this.type = type;
        this.context = context;
    }
     public BottomSheetFragment(String authorContent,String categoryContent,String dateContent,String coverTypeContent,String sizeContent,int numberPageContent, String mPublisher, Integer type, Context context) {
         this.mAuthorContent = authorContent;
         this.mCategoryContent = categoryContent;
         this.mDateContent = dateContent;
         this.mCoverTypeContent = coverTypeContent;
         this.mSizeContent = sizeContent;
         this.mNumberPageContent = numberPageContent;
         this.type = type;
         this.mPublisher = mPublisher;
         this.context = context;
     }


    public static BottomSheetFragment newInstance(ArrayList<Comment> comments, Integer type, Context context) {
        return new BottomSheetFragment(comments, type, context);
    }
    public static BottomSheetFragment newInstance(String authorContent,String categoryContent,String dateContent,String coverTypeContent,String sizeContent,int numberPageContent, String mPublisher, Integer type, Context context) {
        return new BottomSheetFragment(authorContent, categoryContent, dateContent, coverTypeContent, sizeContent, numberPageContent, mPublisher, type, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        mCardView = view.findViewById(R.id.cardView);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        fragmentManager = getChildFragmentManager().beginTransaction();
        if (type == 1) {
            ReviewFragment reviewFragment = ReviewFragment.newInstance(comments, getActivity());

            fragmentManager.replace(R.id.fragmentContainer, reviewFragment);
            fragmentManager.commit();
        }
        else if (type == 2) {
            DetailFragment detailFragment = DetailFragment.newInstance(mAuthorContent, mCategoryContent, mDateContent, mCoverTypeContent, mSizeContent, mNumberPageContent, mPublisher, 2);
            fragmentManager.replace(R.id.fragmentContainer, detailFragment);
            fragmentManager.commit();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
