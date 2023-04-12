package com.example.bookaholic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BranchListViewAdapter extends BaseAdapter {

    private final ArrayList<Branch> branches;

    private Date currentTime = Calendar.getInstance().getTime();
    private SimpleDateFormat h_sdf = new SimpleDateFormat("HH");
    private SimpleDateFormat m_sdf = new SimpleDateFormat("mm");
    private int h = Integer.parseInt(h_sdf.format(currentTime));
    private int m = Integer.parseInt(m_sdf.format(currentTime));

    public BranchListViewAdapter(ArrayList<Branch> branches) {
        this.branches = branches;
    }

    @Override
    public int getCount() {
        return branches.size();
    }

    @Override
    public Object getItem(int i) {
        return branches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.fragment_map_branch_detail, null);
        } else viewProduct = convertView;

        Branch branch = (Branch) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.branch_name)).setText(String.format("%s", branch.getName()));
        ((TextView) viewProduct.findViewById(R.id.branch_phoneNumber)).setText(String.format("Số điện thoại: %s", branch.getPhoneNumber()));
        TextView state = viewProduct.findViewById(R.id.branch_state);
        TextView else_state = viewProduct.findViewById(R.id.branch_else_state);

        ArrayList<String> res = branch.getFormat(this.h, this.m);

        state.setText(res.get(0));
        else_state.setText(res.get(1));

        if (res.get(0).equals("Open")) {
            state.setTextColor(Color.parseColor("#22B14C"));
        } else {
            state.setTextColor(Color.parseColor("#FF0000"));
        }

        return viewProduct;
    }
}
