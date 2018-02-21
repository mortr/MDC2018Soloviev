package com.mortr.soloviev.mdc2018soloviev;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.mortr.soloviev.mdc2018soloviev.ui.quasilauncher.SquareTextView;
import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_APP_THEME;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_IMG_SOURCE;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_LAYOUT_IS_STANDARD;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_PREFS_TIME_PRIOD;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_SORT_TYPE;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_WELCOME_PAGE_WAS_SHOWED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UtilsTest {

    private Context context = Robolectric.buildActivity(StartActivity.class).get();

    @Test
    public void getProcessedBitmapTest() {
        int beginSizeX = 1024;
        int beginSizeY = 880;
        int viewX = 700;
        int viewY = 1080;
        Bitmap testBitmap = Bitmap.createBitmap(beginSizeX, beginSizeY, Bitmap.Config.ARGB_8888);

        Bitmap changedBitmap = Utils.getProcessedBitmap(testBitmap, viewX, viewY);
        assertEquals(changedBitmap.getWidth(), viewX);
        assertEquals(changedBitmap.getHeight(), viewY);
    }

    @Test
    public void getListApplicationsNullContextTest() {
        assertThat(Utils.getListApplications(null), Matchers.nullValue());
    }

    @Test
    public void getSortTypeEmptySharedTest() {
        assertEquals(Utils.SortType.DEFAULT, Utils.getTypeSort(context));
    }

    @Test
    public void getSortTypeSharedTest() {
        Utils.SortType sortType = Utils.SortType.SORT_AZ;
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREFS_SORT_TYPE, sortType.name()).commit();
        assertEquals(sortType, Utils.getTypeSort(context));

    }

    @Test
    public void saveSortTypeTest() {
        Utils.SortType sortType = Utils.SortType.SORT_DATA;
        Utils.saveSortSettings(context, sortType);
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals(sortType, Utils.SortType.valueOf(sharedPreferences.getString(PREFS_SORT_TYPE, Utils.SortType.DEFAULT.name())));
    }

    @Test
    public void getListApplicationsTest() {
        Utils.getListApplications(context);
        assertThat(Utils.getListApplications(context).size(), is(1));
    }

    @Test
    public void getListApplicationsNameTest() {
        Utils.getListApplications(context);
        assertThat(Utils.getListApplications(context).get(0).activityInfo.name, is(StartActivity.class.getCanonicalName()));
    }

    @Test
    public void getLayoutSettingsEmptySharedTest() {
        assertEquals(true, Utils.isStandardLayoutsWasSaved(context));
    }

    @Test
    public void getLayoutSettingsSharedTest() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PREFS_LAYOUT_IS_STANDARD, false).commit();

         assertEquals(false, Utils.isStandardLayoutsWasSaved(context));


    }

    @Test
    public void saveLayoutSettingsTest() {
        Utils.saveLayoutSettings(context, false);
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals(false, sharedPreferences.getBoolean(PREFS_LAYOUT_IS_STANDARD, true));
    }

    @Test
    public void getThemeSharedTest() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Utils.PREFS_APP_THEME, false);
        editor.commit();

        assertEquals(false, Utils.isWhiteTheme(context));

        editor = sharedPreferences.edit();
        editor.putBoolean(Utils.PREFS_APP_THEME, true);
        editor.commit();
        assertEquals(true, Utils.isWhiteTheme(context));
    }

    @Test
    public void getWelcomeWasShowedEmptySharedTest() {
        assertEquals(false, Utils.isWelcomePageShowed(context));
    }

    @Test
    public void getWelcomeWasShowedSharedTest() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(PREFS_WELCOME_PAGE_WAS_SHOWED, true).commit();

        assertEquals(true, Utils.isWelcomePageShowed(context));


    }

    @Test
    public void savetWelcomeWasShowedTest() {
        Utils.saveWelcomePageShowingState(context, true);
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals(true, sharedPreferences.getBoolean(PREFS_WELCOME_PAGE_WAS_SHOWED, false));
    }

    @Test
    public void getPeriodTest() {

        assertEquals(15 * 60 * 1000, Utils.getPeriod("15_min"));
        assertEquals(60 * 60 * 1000, Utils.getPeriod("1_hour"));
        assertEquals(24 * 60 * 60 * 1000, Utils.getPeriod("24_hour"));
        assertEquals(8 * 60 * 60 * 1000, Utils.getPeriod("8_hour"));
        assertEquals(5 * 1000, Utils.getPeriod("5_s"));
        assertEquals(15 * 60 * 1000, Utils.getPeriod("chtotoneto"));
    }

    @Test
    public void getImgKeySourceEmptySharedTest() {
        assertEquals("yandex", Utils.getImgSourceSettings(context));
    }

    @Test
    public void getImgKeySourceSharedTest() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREFS_IMG_SOURCE, "flickr").commit();

        assertEquals("flickr", Utils.getImgSourceSettings(context));


    }

    @Test
    public void saveImgKeySourceTest() {
        Utils.saveImgSourceSettings(context, "flickr");
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals("flickr", sharedPreferences.getString(PREFS_IMG_SOURCE, "yandex"));
    }



    @Test
    public void getImgPeriodChangeEmptySharedTest() {
        assertEquals("15_min", Utils.getTimePeriodSettings(context));
    }

    @Test
    public void getImgPeriodChangeSharedTest() {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(PREFS_PREFS_TIME_PRIOD, "8_hour").commit();

        assertEquals("8_hour", Utils.getTimePeriodSettings(context));


    }

    @Test
    public void saveImgPeriodChangeTest() {
        Utils.saveTimePeriodSettings(context, "24_hour");
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        assertEquals("24_hour", sharedPreferences.getString(PREFS_PREFS_TIME_PRIOD, "15_min"));
    }



}
