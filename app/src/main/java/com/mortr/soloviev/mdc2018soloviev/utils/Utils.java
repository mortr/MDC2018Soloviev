package com.mortr.soloviev.mdc2018soloviev.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    private static final String PREFS_FILE = "Shared_Pref";
    private static final String PREFS_LAYOUT_IS_STANDARD = "PREFS_LAYOUT_IS_STANDARD";

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
}
