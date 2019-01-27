package com.example.android.filmfun4me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo networkInfo = connMgr
                .getActiveNetworkInfo();

        Toast.makeText(context, "Receiver Called!", Toast.LENGTH_SHORT).show();

        Log.d(NetworkChangeReceiver.class.getSimpleName(), "Receiver received");

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            Toast.makeText(context, "Changed!", Toast.LENGTH_SHORT).show();
            if (networkInfo != null && !networkInfo.isConnected()) {
                Toast.makeText(context, "Connection lost!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Connection re-established!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
