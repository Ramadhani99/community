package com.madega.ramadhani.njootucode.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.madega.ramadhani.njootucode.Models.PostModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 9/25/18.
 */

@Dao
public interface PostDao {

    @Query("select *from postmodel ORDER BY id DESC")
    List<PostModel> getAllpost();

    @Insert
    void InsertAll(PostModel... postmodel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void InsertPost(PostModel postModel);

    @Query("Delete from postmodel")
    void DeleteAllfromPost();



}
