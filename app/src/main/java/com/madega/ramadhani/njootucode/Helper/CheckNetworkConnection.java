package com.madega.ramadhani.njootucode.Helper;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.madega.ramadhani.njootucode.Dialog.IntentConnectionDialog;

import es.dmoral.toasty.Toasty;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by root on 10/16/18.
 */

public class CheckNetworkConnection extends AppCompatActivity{





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("TUMEFIKA","WE ARE HERE");
        super.onCreate(savedInstanceState);


        if (!hasConnection()) {
            Toasty.info(getBaseContext(),"NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
           // Toasty.success(getBaseContext(),"Connection available", Toast.LENGTH_SHORT).show();
        }

            //mNetworkCallback.isConnected(hasConnection());


    }

    public boolean hasConnection() {
        boolean has_wifi = false;
        boolean has_mobileData = false;


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    has_wifi = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    has_mobileData = true;

        }
        return has_wifi || has_mobileData;
    }
}
