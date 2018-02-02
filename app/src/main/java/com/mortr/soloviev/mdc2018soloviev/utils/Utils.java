package com.mortr.soloviev.mdc2018soloviev.utils;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;

import com.mortr.soloviev.mdc2018soloviev.R;

public class Utils {

    private static final String PREFS_FILE = "Shared_Pref";
    private static final String PREFS_LAYOUT_IS_STANDARD = "PREFS_LAYOUT_IS_STANDARD";
    private static final String PREFS_APP_THEME = "PREFS_APP_THEME";
    private static final String PREFS_WELCOME_PAGE_WAS_SHOWED="PREFS_WELCOME_PAGE_WAS_SHOWED";


    public static void saveWelcomePageShowingState(Context context, boolean isShow) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_WELCOME_PAGE_WAS_SHOWED, isShow);
        editor.apply();
    }

    public static boolean isWelcomePageShowed(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_WELCOME_PAGE_WAS_SHOWED, false);
    }
    public static void saveLayoutSettings(Context context, boolean isStandard) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_LAYOUT_IS_STANDARD, isStandard);
        editor.apply();
    }
    public static boolean isStandardLayoutsWasSaved(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_LAYOUT_IS_STANDARD, true);
    }

    private static void saveAppTheme(Context context, boolean isWhiteTheme) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Utils.PREFS_APP_THEME, isWhiteTheme);
        editor.apply();
    }

    public static boolean isWhiteTheme(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Utils.PREFS_APP_THEME, true);
    }

    private static void restartCurrentActivity(Activity currentActivity) {
        currentActivity.finish();
        final Intent intent = currentActivity.getIntent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity.startActivity(intent);
    }

    public static void applyTheme(Activity currentActivity, boolean isWhiteTheme) {
        currentActivity.finish();
        Utils.saveAppTheme(currentActivity.getApplicationContext(), isWhiteTheme);
        refreshTheme(currentActivity.getApplication(),isWhiteTheme);
        restartCurrentActivity(currentActivity);
    }


    public static void refreshTheme(Application application,boolean isWhiteTheme) {
        if (isWhiteTheme) {
            application.setTheme(R.style.AppTheme_WhiteTheme);
        }else{
            application.setTheme(R.style.AppTheme_BlackTheme);
        }
    }

    public static void refreshTheme(Application application) {
        refreshTheme(application,Utils.isWhiteTheme(application.getApplicationContext()));
    }


}
