package com.thenewcircle.myquickblogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * @author Lance Nanek (c07793)
 */
public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final boolean disconnected = intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        Log.d("ConnectionReceiver", "onReceive received intent = " + intent
        + " disconnected = " + disconnected);

    }
}
