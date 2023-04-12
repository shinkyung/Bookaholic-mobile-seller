package com.example.bookaholic;

import static com.example.bookaholic.MainActivity.firebaseAuth;
import static com.example.bookaholic.MainActivity.firebaseUser;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String fullName, phoneNumber, address, email;
    private ArrayList<Integer> favoriteBookIds = new ArrayList<>();
    private Map<String, Integer> quantityByBookId = new HashMap<>();

    public User() {
    }
    public User(String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.phoneNumber = "";
        this.email = email;
    }

    public User(String fullName, String phoneNumber, String address, String email, ArrayList<Integer> favoriteBookIds, Map<String, Integer> quantityByBookId) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.favoriteBookIds = favoriteBookIds;
        this.quantityByBookId = quantityByBookId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Integer> getFavoriteBookIds() {
        return favoriteBookIds;
    }

    public void setFavoriteBookIds(ArrayList<Integer> favoriteBookIds) {
        this.favoriteBookIds = favoriteBookIds;
    }

    public Map<String, Integer> getQuantityByBookId() {
        return quantityByBookId;
    }

    public void setQuantityByBookId(Map<String, Integer> quantityByBookId) {
        this.quantityByBookId = quantityByBookId;
    }

    @Exclude
    public boolean unlike(Integer id) {
        return this.favoriteBookIds.remove(id);
    }

    @Exclude
    public void like(Integer catId) {
        if (!this.favoriteBookIds.contains(catId))
            this.favoriteBookIds.add(catId);
    }

    @Exclude
    public Task<Void> saveToDatabase() {
        firebaseUser = firebaseAuth.getCurrentUser();
        return FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(firebaseUser.getUid())
                .setValue(this);
    }

    @Exclude
    public boolean likeBookWithId(Integer id) {
        return this.favoriteBookIds.contains(id);
    }
}
