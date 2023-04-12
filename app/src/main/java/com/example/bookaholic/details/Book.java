package com.example.bookaholic.details;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.example.bookaholic.Comment;
import com.example.bookaholic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Book implements Serializable {
    @Exclude
    public static ArrayList<Book> allBooks = new ArrayList<>();
    private String imageUrl, title, author, category, description, downloadURL, size,publicationDate,
            publisher, typeOfCover, recentlyDate, id;
    private ArrayList<Comment> comments = new ArrayList<>();
    private int price, quantity, numberOfPages, buyer;
    private ArrayList<String> images = new ArrayList<>();

    public Book(){

    }

    //add
    public Book(String imageUrl, String title, String author, String category, String description, int quantity, int price
            , String publicationDate, String publisher, String size, int numberOfPages, String typeOfCover, ArrayList<String> images, ArrayList<Comment> comments) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.size = size;
        this.numberOfPages = numberOfPages;
        this.typeOfCover = typeOfCover;
        this.comments = comments;
        this.images = images;
        this.buyer = 0;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.recentlyDate = currentDate.format(formatter);
    }

    //update
    public Book(String imageUrl, String title, String author, String category, String description, int quantity, int price
            , String publicationDate, String publisher, String size, int numberOfPages, String typeOfCover, ArrayList<String> images, ArrayList<Comment> comments, int buyer, String recentlyDate) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.size = size;
        this.numberOfPages = numberOfPages;
        this.typeOfCover = typeOfCover;
        this.comments = comments;
        this.images = images;
        this.buyer = buyer;
        this.recentlyDate = recentlyDate;
    }

    public Book(String title, String author, String category, String description, String downloadURL, ArrayList<Comment> comments, int price, ArrayList<String> images
        ,int quantity, String size, String publicationDate, String publisher, String typeOfCover, String recentlyDate, int numberOfPages, int buyer) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.downloadURL = downloadURL;
        this.comments = comments;
        this.price = price;
        this.images = images;
        this.quantity = quantity;
        this.size = size;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.typeOfCover = typeOfCover;
        this.recentlyDate = recentlyDate;
        this.numberOfPages = numberOfPages;
        this.buyer = buyer;
    }

    public static ArrayList<Book> getAllBooks() {
        return allBooks;
    }

    public static void setAllBooks(ArrayList<Book> allBooks) {
        Book.allBooks = allBooks;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getImageUrl(){return imageUrl;};

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRateAvg(){
        float sum = 0;
        if (comments == null)
            return 0;
        for (int i = 0; i < comments.size(); i++){
            sum += comments.get(i).getRate();
        }
        return sum/comments.size();
    }
    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @NonNull
    @Exclude
    public Book deepCopy() {
        return new Book(this.title, this.author, this.category, this.description, this.downloadURL, this.comments, this.price, this.images, this.quantity, this.size, this.publicationDate, this.publisher,this.typeOfCover, this.recentlyDate, this.numberOfPages, this.buyer);
    }

    @SuppressLint("DefaultLocale")
    public String getDisplayablePrice() {
        String str = NumberFormat.getNumberInstance(Locale.US).format(price);
        str += " đ";
        return str;
    }

    @Exclude
    public boolean hasNameSimilarTo(String text) {
        return this.title.toLowerCase().contains(text.toLowerCase());
    }

    @Exclude
    public boolean hasPriceInRange(Integer min, Integer max) {
        return (min == null || price >= min) && (max == null || price <= max);
    }

    public static Book findBookByTitle(String title) {
        for (int i = 0; i < allBooks.size(); i++){
            if (allBooks.get(i).getTitle().contains(title)){
                return allBooks.get(i);
            }
        }
        return null;
    }


    @Exclude
    public static Integer idOfBookWithName(String bookName) {
        for (int i = 0; i < allBooks.size(); i++)
            if (allBooks.get(i).title.equals(bookName))
                return i;
        return -1;
    }


    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity= quantity;
    }
    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getTypeOfCover() {
        return typeOfCover;
    }

    public void setTypeOfCover(String typeOfCover) {
        this.typeOfCover = typeOfCover;
    }

    public Integer getBuyer() {
        return buyer;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

    public String getRecentlyDate() {
        return recentlyDate;
    }

    public void setRecentlyDate(String recentlyDate) {
        this.recentlyDate = recentlyDate;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("author", author);
        result.put("category", category);
        result.put("description", description);
        result.put("quantity", quantity);
        result.put("price", price);
        result.put("publicationDate", publicationDate);
        result.put("publisher", publisher);
        result.put("size", size);
        result.put("numberOfPages", numberOfPages);
        result.put("typeOfCover", typeOfCover);
        result.put("images", images);
        result.put("buyer", buyer);
        result.put("displayablePrice", getDisplayablePrice());
        result.put("rateAvg", getRateAvg());
        result.put("recentlyDate", recentlyDate);
        result.put("id", id);

        return result;
    }
}
