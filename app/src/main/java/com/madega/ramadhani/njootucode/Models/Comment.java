package com.madega.ramadhani.njootucode.Models;

/**
 * Created by root on 9/18/18.
 */

public class Comment {

    private boolean hasComment;
    private PostModel postModel;
    private int id;
    private String body;
    private int likes;
    private String commenter;
    private String commenterPhoto;


    public PostModel getPostModel() {
        return postModel;
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public boolean isHasComment() {
        return hasComment;
    }

    public void setHasComment(boolean hasComment) {
        this.hasComment = hasComment;
    }

    public String getCommenterPhoto() {
        return commenterPhoto;
    }

    public void setCommenterPhoto(String commenterPhoto) {
        this.commenterPhoto = commenterPhoto;
    }
}
