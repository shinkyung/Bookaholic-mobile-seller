package com.example.bookaholic.voucher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookaholic.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {
    private ArrayList<Voucher> voucherList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView voucherImage;
        public TextView voucherNameTextView;
        public TextView voucherTypeTextView;
        public TextView voucherQuantityTextView;
        public TextView voucherDiscountTextView;
        public TextView dateTxt;
        public TextView endTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            voucherImage = itemView.findViewById(R.id.voucherImage);
            voucherNameTextView = itemView.findViewById(R.id.voucherNameTxt);
            voucherTypeTextView = itemView.findViewById(R.id.voucherTypeTextView);
            voucherQuantityTextView = itemView.findViewById(R.id.quantityTxt);
            voucherDiscountTextView = itemView.findViewById(R.id.voucherDiscountTextView);
            dateTxt = itemView.findViewById(R.id.dateTxt);
        }
    }

    public VoucherAdapter(Context context, ArrayList<Voucher> voucherList) {
        this.voucherList = voucherList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voucher_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        if (voucher.getTypeVoucher().contains("Price")){
            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/bookaholic-82677.appspot.com/o/price.png?alt=media&token=d4666cba-d55b-4f69-a81f-f1a3ddc47c0d").into(holder.voucherImage);
        } else {
            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/bookaholic-82677.appspot.com/o/percent.png?alt=media&token=0f23176c-74e9-4dd2-881e-ab0e01c62550").into(holder.voucherImage);
        }
        holder.voucherNameTextView.setText(voucher.getNameVoucher());
        holder.voucherTypeTextView.setText(voucher.getTypeVoucher());
        holder.voucherQuantityTextView.setText(String.valueOf(voucher.getQuantityVoucher()));
        if(voucher.getTypeVoucher().contains("Price"))
            holder.voucherDiscountTextView.setText(displayPrice(voucher.getDiscountVoucher()));
        else
            holder.voucherDiscountTextView.setText(displayPercent(voucher.getDiscountVoucher()));
        holder.dateTxt.setText(displayDate(voucher.getStartVoucher(), voucher.getEndVoucher()));
//        Intent intent = new Intent();
//        context.startActivity();
    }

    public String displayPrice(int price) {
        String str = NumberFormat.getNumberInstance(Locale.US).format(price);
        str += " đ";
        return str;
    }

    public String displayPercent(int price) {
        String str = NumberFormat.getNumberInstance(Locale.US).format(price);
        str += " %";
        return str;
    }

    public String displayDate(String start, String end) {
        String str = start + " - " + end;
        return str;
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }
}