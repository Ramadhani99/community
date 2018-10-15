package com.madega.ramadhani.njootucode.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by root on 9/14/18.
 */

@Entity
public class PostModel {

    @Ignore
    public int ATTACHMENT_TYPE=0;



    @PrimaryKey(autoGenerate = false)
    private int id;


    private boolean isLike;

    private String Share,Postname,Date,post;

    private int Likes,Comments;

    private String PosterImage;

    private String PostImage;
    private String publisherName;
    private int publisherId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String user) {
        this.publisherName = user;
    }



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

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
