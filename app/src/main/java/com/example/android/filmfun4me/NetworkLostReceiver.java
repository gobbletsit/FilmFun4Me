package com.example.android.filmfun4me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;

public class NetworkLostReceiver extends BroadcastReceiver {

    ListActivity listActivity;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            if (networkInfo == null) {
                Toast.makeText(context, context.getResources().getString(R.string.no_network_connection), Toast.LENGTH_LONG).show();
                listActivity.registerNetworkRegainedReceiver();
            }
        }
    }

    public void setListActivityHandler(ListActivity listActivity){
        this.listActivity = listActivity;
    }
}
