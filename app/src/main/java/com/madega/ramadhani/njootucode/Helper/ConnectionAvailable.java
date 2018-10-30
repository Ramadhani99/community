package com.madega.ramadhani.njootucode.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by root on 10/17/18.
 */

public class ConnectionAvailable {
    public static boolean hasConnection(Context context) {
        boolean has_wifi = false;
        boolean has_mobileData = false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
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
