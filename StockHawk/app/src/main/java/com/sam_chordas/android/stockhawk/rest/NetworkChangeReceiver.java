package com.sam_chordas.android.stockhawk.rest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.sam_chordas.android.stockhawk.service.StockIntentService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.squareup.okhttp.internal.Util;

/**
 * Created by Zang on 2016-09-19.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{
    private static final String LOG_TAG = NetworkChangeReceiver.class.getSimpleName();
    Context mContext;
    MyStocksActivity mActivity;

    public NetworkChangeReceiver() {}

    public NetworkChangeReceiver (MyStocksActivity activity) {
        mActivity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                // Call the service to request the stocks

                Intent mServiceIntent = new Intent(context, StockIntentService.class);
                mServiceIntent.putExtra("tag", "update");
                context.startService(mServiceIntent);

            } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                Log.d(LOG_TAG, "No internet");
            }
        }
    }
}
