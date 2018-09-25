package com.madega.ramadhani.njootucode.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.madega.ramadhani.njootucode.Models.User;

import java.util.List;

/**
 * Created by root on 9/14/18.
 */

@Dao
public interface UserDao {
    @Query("Select *from user")
    List<User> getAllUser();

    @Insert
    void InserUser(User user);

    @Delete
    void DeleteUser(User user);

    @Update
    void UpdateUser(User user);
}
