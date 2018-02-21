package com.mortr.soloviev.mdc2018soloviev;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.mortr.soloviev.mdc2018soloviev.utils.StorageUtils.CACHE_LATEST_BG_FILE_NAME;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {


    private final Context context = InstrumentationRegistry.getTargetContext();


    @Test
    public void getTheme() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals(sharedPreferences.getBoolean(Utils.PREFS_APP_THEME, true), Utils.isWhiteTheme(context));
    }

    @Test
    public void getIsWelcomeShow() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals(sharedPreferences.getBoolean(Utils.PREFS_WELCOME_PAGE_WAS_SHOWED, false), Utils.isWelcomePageShowed(context));
    }

    @Test
    public void getBgPeriodChange() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals(sharedPreferences.getString(Utils.PREFS_PREFS_TIME_PRIOD, "15_min"), Utils.getTimePeriodSettings(context));
    }

    @Test
    public void getOrientation() {
        final Configuration configuration = context.getResources().getConfiguration();
        int orientation = configuration.orientation;
        String orientationString = orientation == Configuration.ORIENTATION_LANDSCAPE ? "LANDSCAPE" : orientation == Configuration.ORIENTATION_PORTRAIT ? "PORTRAIT" : "UNDEFINED or Square";
        assertEquals(orientationString, Utils.getOrientation(context));
    }


    @Test
    public void getSavedBitmapSizeTest() {
        Bitmap bitmapFromUtilMethod = StorageUtils.getBitmap(context);

        Bitmap testBitmap = getBitmapFromCash();
        int heightTest = 0;
        int heightMethod = 0;
        int widthTest = 0;
        int widthMethod = 0;

        if (testBitmap != null) {
            heightTest = testBitmap.getHeight();
            widthTest = testBitmap.getWidth();
        }

        if (bitmapFromUtilMethod != null) {
            heightMethod = bitmapFromUtilMethod.getHeight();
            widthMethod = bitmapFromUtilMethod.getWidth();
        }
        assertEquals(heightTest, heightMethod);
        assertEquals(widthTest, widthMethod);

    }

    private Bitmap getBitmapFromCash() {
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


}