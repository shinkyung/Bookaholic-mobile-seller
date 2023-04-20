package com.example.bookaholic;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Order {
    public static Order currentOrder = new Order();
    private ArrayList<OrderBook> orderBooks;
    private String address;
    private double totalPrice;
    private String orderStatus;
    private String createdAt;

    private int id;

    private String orderOwner;


    public Order() {
        this.orderBooks = new ArrayList<>();
        this.address = "";
        this.totalPrice = 0;
        this.orderStatus = "Incomplete";
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        createdAt = dateFormat.format(now);
        orderOwner = "";
        id = 0;

    }

    public Order(ArrayList<OrderBook> orderBooks, int id ,String address, double totalPrice, String orderStatus, String createdAt, String orderOwner) {
        this.orderBooks = orderBooks;
        this.address = address;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.orderOwner = orderOwner;
        this.id = id;
    }

    public ArrayList<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    public void setOrderBooks(ArrayList<OrderBook> orderBooks) {
        this.orderBooks = orderBooks;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String displayTotal() {
        String str = NumberFormat.getNumberInstance(Locale.US).format(totalPrice);
        str += " đ";
        return str;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void addOrderBook(OrderBook currentBook) { this.orderBooks.add(currentBook); }

    public Integer orderSize() { return orderBooks.size(); }

    public boolean checkOrderBook(OrderBook orderBook) {
        for (OrderBook book : orderBooks){
            if (orderBook.getBook().getTitle() == book.getBook().getTitle())
                return true;
        }
        return false;
    }

    public void addExistedOrderBook(OrderBook orderBook) {
        for (OrderBook book : orderBooks) {
            if (book.book.getTitle() == orderBook.book.getTitle()){
                book.quantity += orderBook.quantity;
                return;
            }
        }
    }

    public Integer quantity() {
        return orderBooks.size();
    }

    public String getOrderOwner() {
        return orderOwner;
    }

    public void setOrderOwner(String orderOwner) {
        this.orderOwner = orderOwner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
