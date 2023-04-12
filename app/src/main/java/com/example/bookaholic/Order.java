package com.example.bookaholic;

import com.example.bookaholic.details.Book;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    public static Order currentOrder = new Order();
    private ArrayList<OrderBook> orderBooks;
    private User user;
    private String address;
    private double totalPrice;
    private String orderStatus;
    private Date createdAt;

    public Order() {
        this.orderBooks = new ArrayList<>();
        this.user = MainActivity.currentSyncedUser;
        this.address = "";
        this.totalPrice = 0;
        this.orderStatus = "Incomplete";
        this.createdAt = new Date();
    }

    public Order(ArrayList<OrderBook> orderBooks, User user, String address, double totalPrice, String orderStatus, Date createdAt) {
        this.orderBooks = orderBooks;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void addOrderBook(OrderBook currentBook) { this.orderBooks.add(currentBook); }

    public Integer getOrderSize() { return orderBooks.size(); }

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
}
