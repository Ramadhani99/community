package com.madega.ramadhani.njootucode.Storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.madega.ramadhani.njootucode.Dao.PostDao;
import com.madega.ramadhani.njootucode.Dao.UserDao;
import com.madega.ramadhani.njootucode.Models.PostModel;
import com.madega.ramadhani.njootucode.Models.User;

/**
 * Created by root on 9/14/18.
 */

 @Database(entities = {User.class,PostModel.class},version = 3)
public abstract class ApplicationDatabase extends RoomDatabase {
     public abstract UserDao userDao();

     public abstract PostDao postdao();

     private static  ApplicationDatabase INSTANCE=null;

     //craete an Instance

    public static ApplicationDatabase getApplicationDatabase(Context context){
        if (INSTANCE==null){
         INSTANCE=  Room.databaseBuilder
                    (context.getApplicationContext(),ApplicationDatabase.class,"community")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
