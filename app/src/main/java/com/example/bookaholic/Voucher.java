package com.example.bookaholic;

import com.example.bookaholic.details.Book;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Voucher implements Serializable {
    private String id, nameVoucher, idVoucher, typeVoucher, startVoucher, endVoucher;
    private int discountVoucher, minimumVoucher, quantityVoucher, limitVoucher;

    @Exclude
    public static ArrayList<Voucher> allVouchers = new ArrayList<>();

    public Voucher(){}
    public Voucher(String nameVoucher, String idVoucher, String typeVoucher, String startVoucher, String endVoucher,
            int discountVoucher, int minimumVoucher, int quantityVoucher, int limitVoucher){
        this.nameVoucher = nameVoucher;
        this.idVoucher = idVoucher;
        this.typeVoucher = typeVoucher;
        this.startVoucher = startVoucher;
        this.endVoucher = endVoucher;
        this.discountVoucher = discountVoucher;
        this.minimumVoucher = minimumVoucher;
        this.quantityVoucher = quantityVoucher;
        this.limitVoucher = limitVoucher;
    }

    public static Voucher findVoucherByName(String title) {
        for (int i = 0; i < allVouchers.size(); i++){
            if (allVouchers.get(i).getNameVoucher().contains(title)){
                return allVouchers.get(i);
            }
        }
        return null;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setNameVoucher(String nameVoucher){
        this.nameVoucher = nameVoucher;
    }

    public String getNameVoucher(){
        return nameVoucher;
    }

    public void setIdVoucher(String idVoucher){
        this.idVoucher = idVoucher;
    }

    public String getIdVoucher(){
        return idVoucher;
    }

    public void setTypeVoucher(String typeVoucher){
        this.typeVoucher = typeVoucher;
    }

    public String getTypeVoucher(){
        return typeVoucher;
    }

    public void setStartVoucher(String startVoucher){
        this.startVoucher = startVoucher;
    }

    public String getStartVoucher(){
        return startVoucher;
    }

    public void setEndVoucher(String endVoucher){
        this.endVoucher = endVoucher;
    }

    public String getEndVoucher(){
        return endVoucher;
    }

    public void setDiscountVoucher(int discountVoucher){
        this.discountVoucher = discountVoucher;
    }

    public int getDiscountVoucher(){
        return discountVoucher;
    }

    public void setMinimumVoucher(int minimumVoucher){
        this.minimumVoucher = minimumVoucher;
    }

    public int getMinimumVoucher(){
        return minimumVoucher;
    }

    public void setQuantityVoucher(int quantityVoucher){
        this.quantityVoucher = quantityVoucher;
    }

    public int getQuantityVoucher(){
        return quantityVoucher;
    }

    public void setLimitVoucher(int limitVoucher){
        this.limitVoucher = limitVoucher;
    }

    public int getLimitVoucher(){
        return limitVoucher;
    }

}
