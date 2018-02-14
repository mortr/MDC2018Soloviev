package com.mortr.soloviev.mdc2018soloviev.ui.welcomePages;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.utils.Utils;
import com.yandex.metrica.YandexMetrica;


public abstract class WelcomePagesFragment extends Fragment {

    public static final String TAG = "WelcomePagesFr";

    public abstract String getTitle();

    public void onFrontPagerScreen() {
        Log.d(TAG, this.getClass().getName()+"onFrontScreen()");
        Utils.sendYAPPMEvent(Utils.YAPPEventName.WELC_PAGE_ON_FOREGROUND,WelcomePagesFragment.this.getClass().getName());
    }


}
