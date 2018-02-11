package com.mortr.soloviev.mdc2018soloviev.ui.mainScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;



public class MainScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(Utils.isWhiteTheme(this)?R.style.AppTheme_WhiteTheme:R.style.AppTheme_BlackTheme);
        super.onCreate(savedInstanceState);
        Utils.sendYAPPMEvent(Utils.YAPPEventName.MAIN_SCREEN_OPEN,"");
        setContentView(R.layout.activity_main_screen);
    }
}
