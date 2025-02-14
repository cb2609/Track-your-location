package com.example.project.Model;

import java.util.HashMap;

public class User {
    private String uid, email;
    private HashMap<String,User> acceptList; //list user friends

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
        acceptList = new HashMap<>();
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, User> getAcceptList() {
        return acceptList;
    }

    public void setAcceptList(HashMap<String, User> acceptList) {
        this.acceptList = acceptList;
    }
}
