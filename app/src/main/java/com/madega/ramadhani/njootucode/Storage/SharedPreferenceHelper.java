package com.madega.ramadhani.njootucode.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.madega.ramadhani.njootucode.Models.User;

/**
 * Created by root on 9/24/18.
 */

public class SharedPreferenceHelper {
    public static String USER_INFO="USER_INFORMATION";

    public static void StoreUser(Context context,String userJson){
        SharedPreferences sharedPreferences=context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        SharedPreferences.Editor Usereditor=sharedPreferences.edit();
        Usereditor.clear();
        Usereditor.putString(USER_INFO,userJson);
        Usereditor.apply();
        Usereditor.commit();

    }
    public static User getUSer(Context context){
        User user=null;
        SharedPreferences sharedPreferences=context.getSharedPreferences(USER_INFO,0);
        String userjson=sharedPreferences.getString(USER_INFO,"");
        user=new Gson().fromJson(userjson,User.class);
        return user;

    }

}
