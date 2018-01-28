package com.mortr.soloviev.mdc2018soloviev.ui.mainScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.mortr.soloviev.mdc2018soloviev.R;

import io.fabric.sdk.android.Fabric;


public class MainScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());//TODO move to application
        setContentView(R.layout.activity_main_screen);
    }
}
