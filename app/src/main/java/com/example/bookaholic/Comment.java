package com.example.bookaholic;

public class Comment {
    private String content,name, avatar;
    private int rate;

    public Comment(){
    }
    public Comment(String content, String name, String avatar,int rate) {
        this.content = content;
        this.rate = rate;
        this.avatar = avatar;
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public void setRate(int rate){
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRate() {
        return rate;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

}
