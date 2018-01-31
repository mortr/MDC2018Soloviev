package com.mortr.soloviev.mdc2018soloviev;


import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import io.fabric.sdk.android.Fabric;

public class ApplicationMDC18 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Utils.refreshTheme(this);
    }
}
