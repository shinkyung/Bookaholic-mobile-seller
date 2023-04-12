package com.example.bookaholic;
import com.example.bookaholic.details.Book;
public class OrderBook {
    public Book book;
    public int quantity;

    public OrderBook(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }
    public int decreaseQuantity(){
        return --quantity;
    }
    public int increaseQuantity(){
        return ++quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int calcuteTotalPrice(){
        return book.getPrice() * quantity;
    }
}
