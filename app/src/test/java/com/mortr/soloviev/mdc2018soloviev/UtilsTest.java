package com.mortr.soloviev.mdc2018soloviev;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.mortr.soloviev.mdc2018soloviev.utils.Utils;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_LAYOUT_IS_STANDARD;
import static com.mortr.soloviev.mdc2018soloviev.utils.Utils.PREFS_SORT_TYPE;
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


}
