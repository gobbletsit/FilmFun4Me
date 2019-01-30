package com.example.android.filmfun4me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.android.filmfun4me.activity.activity.detail.view.DetailActivity;
import com.example.android.filmfun4me.activity.activity.list.view.ListActivity;

public class NetworkRegainedReceiver extends BroadcastReceiver {

    ListActivity listActivity = null;

    DetailActivity detailActivity = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // if isListActive/isDetailActive

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            if (networkInfo!= null && networkInfo.isConnected()){
                Toast.makeText(context, context.getResources().getString(R.string.connection_re_established), Toast.LENGTH_SHORT).show();
                if (ListActivity.isListActive && listActivity != null){
                    listActivity.reload();
                } else if (DetailActivity.isDetailActive && detailActivity != null){
                    detailActivity.reload();
                }
            }
        }
    }

    public void setListActivityHandler(ListActivity listActivity){
        this.listActivity = listActivity;
    }

    public void setDetailActivityHandler(DetailActivity detailActivity){
        this.detailActivity = detailActivity;
    }
}
