package com.madega.ramadhani.njootucode.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by root on 9/14/18.
 */

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private Integer Id ;

    private String fullname;
    private String email;
    private String phone;
    private String password;
    private String profileImgaePath;
    private String token;
    private  String status;


    @Ignore
    public String confirm_password;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getProfileImgaePath() {
        return profileImgaePath;
    }

    public void setProfileImgaePath(String profileImgaePath) {
        this.profileImgaePath = profileImgaePath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
