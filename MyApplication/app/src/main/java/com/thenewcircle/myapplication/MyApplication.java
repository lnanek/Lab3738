package com.thenewcircle.myapplication;

import android.app.Application;

/**
 * Created by lnanek on 9/10/14.
 */
public class MyApplication extends Application {

    public static MyApplication instance;

    @Override
    public void onCreate() {

        instance = this;

        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
