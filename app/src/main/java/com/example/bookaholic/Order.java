package com.example.bookaholic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    public static Order currentOrder = new Order();
    private ArrayList<OrderBook> orderBooks;
    private String address;
    private double totalPrice;
    private String orderStatus;
    private String createdAt;

    public Order() {
        this.orderBooks = new ArrayList<>();
        this.address = "";
        this.totalPrice = 0;
        this.orderStatus = "Incomplete";
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        createdAt = dateFormat.format(now);
    }

    public Order(ArrayList<OrderBook> orderBooks, String address, double totalPrice, String orderStatus, String createdAt) {
        this.orderBooks = orderBooks;
        this.address = address;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
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
}
