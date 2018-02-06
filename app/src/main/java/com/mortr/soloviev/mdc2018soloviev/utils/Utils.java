package com.mortr.soloviev.mdc2018soloviev.utils;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.db.DBHelper;
import com.mortr.soloviev.mdc2018soloviev.db.DBUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    private static final String PREFS_FILE = "Shared_Pref";
    private static final String PREFS_LAYOUT_IS_STANDARD = "PREFS_LAYOUT_IS_STANDARD";
    private static final String PREFS_APP_THEME = "PREFS_APP_THEME";
    private static final String PREFS_WELCOME_PAGE_WAS_SHOWED = "PREFS_WELCOME_PAGE_WAS_SHOWED";
    private static final String PREFS_SORT_TYPE = "PREFS_SORT_TYPE";
    private static final String PREFS_PREFS_APP_SHOWED = "PREFS_PREFS_APP_SHOWED";

    public static void savePreferenceAppShowingState(Activity context, boolean isShow) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_PREFS_APP_SHOWED, isShow);
        editor.apply();
    }

    public static boolean isPreferenceAppShowed(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_PREFS_APP_SHOWED, false);
    }

    public enum SortType {
        SORT_AZ(), SORT_ZA(), SORT_DATA(), DEFAULT(), SORT_START_COUNT();
    }


    public static void saveWelcomePageShowingState(Context context, boolean isShow) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_WELCOME_PAGE_WAS_SHOWED, isShow);
        editor.apply();
    }

    public static boolean isWelcomePageShowed(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_WELCOME_PAGE_WAS_SHOWED, false);
    }

    public static void saveLayoutSettings(Context context, boolean isStandard) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_LAYOUT_IS_STANDARD, isStandard);
        editor.apply();
    }

    public static boolean isStandardLayoutsWasSaved(Context context) {
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

    public static void restartCurrentActivity(Activity currentActivity) {
        currentActivity.finish();
        final Intent intent = currentActivity.getIntent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity.startActivity(intent);
    }

    public static void applyTheme(Activity currentActivity, boolean isWhiteTheme) {
        currentActivity.finish();
        Utils.saveAppTheme(currentActivity.getApplicationContext(), isWhiteTheme);
        refreshTheme(currentActivity.getApplication(), isWhiteTheme);
        restartCurrentActivity(currentActivity);
    }


    private static void refreshTheme(Application application, boolean isWhiteTheme) {
        if (isWhiteTheme) {
            application.setTheme(R.style.AppTheme_WhiteTheme);
        } else {
            application.setTheme(R.style.AppTheme_BlackTheme);
        }
    }

    public static void refreshTheme(Application application) {
        refreshTheme(application, Utils.isWhiteTheme(application.getApplicationContext()));
    }

    @Nullable
    public static List<ResolveInfo> getListApplications(Context context) {
        if (context == null) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return packageManager.queryIntentActivities(intent, 0);
    }

    public static SortType getTypeSort(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        return SortType.valueOf(sharedPreferences.getString(PREFS_SORT_TYPE, SortType.DEFAULT.name()));
    }

    public static void saveSortSettings(Context context, SortType sort) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_SORT_TYPE, sort.name());
        editor.apply();
    }


    public static List<ResolveInfo> getSortedApps(List<ResolveInfo> resolveInfoList, SortType sort, final Context context) {
        List<ResolveInfo> sortedResolveList = new ArrayList<>(resolveInfoList);
        switch (sort) {
            case SORT_AZ:
                Collections.sort(sortedResolveList,
                        new Comparator<ResolveInfo>() {
                            @Override
                            public int compare(ResolveInfo o1, ResolveInfo o2) {
                                PackageManager packageManager = context.getPackageManager();
                                return o1.loadLabel(packageManager).toString()
                                        .compareTo(o2.loadLabel(packageManager).toString());
                            }
                        });
                break;
            case SORT_ZA:
                Collections.sort(sortedResolveList,
                        new Comparator<ResolveInfo>() {
                            @Override
                            public int compare(ResolveInfo o1, ResolveInfo o2) {
                                PackageManager packageManager = context.getPackageManager();
                                return o1.loadLabel(packageManager).toString()
                                        .compareTo(o2.loadLabel(packageManager).toString());
                            }
                        });
                Collections.reverse(sortedResolveList);
                break;
            case SORT_START_COUNT:
                final DBHelper dbHelper = new DBHelper(context);
                final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                Collections.sort(sortedResolveList, new Comparator<ResolveInfo>() {
                    @Override
                    public int compare(ResolveInfo o1, ResolveInfo o2) {
                        return DBUtils.getStartCount(o1, sqLiteDatabase) - DBUtils.getStartCount(o2, sqLiteDatabase);
                    }
                });
                Collections.reverse(sortedResolveList);
                dbHelper.close();
                break;
            case SORT_DATA:
                Collections.sort(sortedResolveList, new Comparator<ResolveInfo>() {
                    @Override
                    public int compare(ResolveInfo o1, ResolveInfo o2) {
                        long o1date = 0;
                        long o2date = 0;

                        try {
                            o1date = context.getPackageManager().getPackageInfo(o1.activityInfo.packageName, 0).firstInstallTime;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        try {
                            o2date = context.getPackageManager().getPackageInfo(o2.activityInfo.packageName, 0).firstInstallTime;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        return (int) (o1date - o2date);
                    }
                });
                Collections.reverse(sortedResolveList);
                break;
        }
        return sortedResolveList;
    }

}
