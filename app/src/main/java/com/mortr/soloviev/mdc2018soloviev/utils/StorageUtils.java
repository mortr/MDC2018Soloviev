package com.mortr.soloviev.mdc2018soloviev.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageUtils {

    public static final String CACHE_LATEST_BG_FILE_NAME = "latestBg";
    public static final String PREFS_PROFILE_INTERESTING_INFO = "PREFS_PROFILE_INTERESTING_INFO";

    public static void saveBitmap(final Context context, final Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(context.getCacheDir(), CACHE_LATEST_BG_FILE_NAME));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 88, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap getBitmap(final Context context) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(context.getCacheDir(), CACHE_LATEST_BG_FILE_NAME));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveProfileInterestingInfo(Context context, String info) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_PROFILE_INTERESTING_INFO, info);
        editor.apply();
    }

    @Nullable
    public static String getProfileInterestingInfo(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREFS_PROFILE_INTERESTING_INFO,null);
    }

//    public static void saveBitmap(final Context context, final Bitmap bitmap, final String imageName) {
//
//    }
//
//    public static Bitmap getBitmap(final Context context, final String imageName) {
//        return null;
//    }


}
