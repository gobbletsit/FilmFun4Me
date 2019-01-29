package com.example.android.filmfun4me.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.android.filmfun4me.NetworkChangeReceiver;
import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;

public class NetworkRegainedReceiver extends BroadcastReceiver {

    ListActivity listActivity = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            if (networkInfo!=null && networkInfo.isConnected()){
                /*Intent a = new Intent(context, ListActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(a);*/
                listActivity.reload();
                Toast.makeText(context, "Connection re-established!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setListActivityHandler(ListActivity listActivity){
        this.listActivity = listActivity;
    }
}
