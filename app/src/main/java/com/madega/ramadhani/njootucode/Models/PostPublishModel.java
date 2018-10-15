package com.madega.ramadhani.njootucode.Models;

/**
 * Created by root on 10/15/18.
 */

public class PostPublishModel {
    private String user_token;
    private String textPost;
    private String imgPost;

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getTextPost() {
        return textPost;
    }

    public void setTextPost(String textPost) {
        this.textPost = textPost;
    }

    public String getImgPost() {
        return imgPost;
    }

    public void setImgPost(String imgPost) {
        this.imgPost = imgPost;
    }
}
