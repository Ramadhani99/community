package com.madega.ramadhani.njootucode.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by root on 9/14/18.
 */

@Entity
public class PostModel {

    @PrimaryKey
    private int id;

    private String Share,Postname,Date,post;

    private int Likes,Comments;

    private String PosterImage;

    private String PostImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getComments() {
        return Comments;
    }

    public void setComments(int comments) {
        Comments = comments;
    }

    public String getShare() {
        return Share;
    }

    public void setShare(String share) {
        Share = share;
    }

    public String getPostname() {
        return Postname;
    }

    public void setPostname(String postname) {
        Postname = postname;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPosterImage() {
        return PosterImage;
    }

    public void setPosterImage(String posterImage) {
        PosterImage = posterImage;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }
}
