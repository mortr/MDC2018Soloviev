package com.mortr.soloviev.mdc2018soloviev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mortr.soloviev.mdc2018soloviev.network.ImageLoaderService;
import com.mortr.soloviev.mdc2018soloviev.ui.MainPagerActivity;
import com.mortr.soloviev.mdc2018soloviev.ui.welcomePages.WelcomePagesActivity;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            startService(new Intent(this, ImageLoaderService.class));
        }
//        Utils.refreshTheme(this.getApplication());
        if (Utils.isWelcomePageShowed(this)) {
            startActivity(new Intent(this, MainPagerActivity.class));
        } else {
            startActivity(new Intent(this, WelcomePagesActivity.class));
        }
    }

}
