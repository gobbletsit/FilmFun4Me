package com.example.android.filmfun4me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            if (ListActivity.isListActive){
                Intent reloadListActivity = new Intent();
                reloadListActivity.setClassName("com.example.android.filmfun4me", ".activity.activity.list.view.ListActivity");
                reloadListActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(reloadListActivity);
            }
        }
    }
}
