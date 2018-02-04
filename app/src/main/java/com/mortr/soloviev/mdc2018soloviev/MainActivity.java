package com.mortr.soloviev.mdc2018soloviev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher.LauncherActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.WelcomeActivity;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
//        Utils.refreshTheme(this.getApplication());
        if (Utils.isWelcomePageShowed(this)) {
            startActivity(new Intent(this, com.mortr.soloviev.mdc2018soloviev.ui.launcher.LauncherActivity.class));
        } else {
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

}
