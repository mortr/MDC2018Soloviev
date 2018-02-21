package com.mortr.soloviev.mdc2018soloviev.utils;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mortr.soloviev.mdc2018soloviev.R;
import com.mortr.soloviev.mdc2018soloviev.db.DBHelper;
import com.mortr.soloviev.mdc2018soloviev.db.DBUtils;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final String PREFS_FILE = "Shared_Pref";
    public static final String PREFS_LAYOUT_IS_STANDARD = "PREFS_LAYOUT_IS_STANDARD";
    public static final String PREFS_APP_THEME = "PREFS_APP_THEME";
    public static final String PREFS_WELCOME_PAGE_WAS_SHOWED = "PREFS_WELCOME_PAGE_WAS_SHOWED";
    public static final String PREFS_SORT_TYPE = "PREFS_SORT_TYPE";
    private static final String PREFS_PREFS_APP_SHOWED = "PREFS_PREFS_APP_SHOWED";
    public static final String PREFS_PREFS_TIME_PRIOD = "PREFS_PREFS_TIME_PRIOD";
    public static final String PREFS_IMG_SOURCE="PREFS_IMG_SOURCE";

    public static void launchApp(ResolveInfo appInfo, Context context) {
        ActivityInfo activity = appInfo.activityInfo;

        ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                activity.name);
        launchApp(name, context);
    }

    public static void launchApp(ComponentName componentName, Context context) {
        sendYAPPMEvent(YAPPEventName.LAUNCH_APP_START, componentName.getClassName());
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
        intent.setComponent(componentName);
        context.startActivity(intent);
    }

    public static void launchWebReference(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    public static void launchCustom(Context context, String action, String[] categories, ComponentName componentName) {
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        for (String category : categories) {
            intent.addCategory(category);
        }
        if (componentName != null) {
            intent.setComponent(componentName);
        }
        context.startActivity(intent);

    }

    public enum YAPPEventName {
        WELC_PAGE_ON_FOREGROUND, WELC_SCREANS_CREATE, LAUNCH_PAGE_ON_FOREGROUND, LAUNCH_SCREANS_CREATE, LAUNCH_DRAWER_OPEN, LAUNCH_DRAWER_ITEM_CHOOSE,
        LAUNCH_APP_START, LAUNCH_APP_DELETE, LAUNCH_APP_INFO, LAUNCH_APP_STARTS_INFO, LAUNCH_SORT_APP_CHANGE,
        LAUNCH_BACK_PRESS, MAIN_SCREEN_OPEN, PROFILE_OPEN, PROFILE_GIT_HUB, PROFILE_BACK, PROFILE_HOME, PROFILE_MOB_PHONE,
        PROFILE_ADDITIONAL_MAIL, PROFILE_MAIN_MAIL, PROFILE_HOME_ADDRESS, THEME_CHANGE, LAYOUT_DENSITY_CHANGE
    }

    public enum SortType {
        SORT_AZ(), SORT_ZA(), SORT_DATA(), DEFAULT(), SORT_START_COUNT()
    }

    public static void sendYAPPMEvent(final YAPPEventName eventName, final String message) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("time", String.valueOf(System.currentTimeMillis()));
        if (message != null && !message.isEmpty()) {
            arguments.put("message", message);
        }
        final Thread sendEventToYAPM = new Thread(new Runnable() {

            @Override
            public void run() {

                YandexMetrica.reportEvent(eventName.name(), arguments);
            }
        });
        sendEventToYAPM.start();
    }

    public static String getOrientation(Context context) {
        final Configuration configuration = context.getResources().getConfiguration();
        int orientation = configuration.orientation;

        return
                orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? "LANDSCAPE"
                        : orientation == Configuration.ORIENTATION_PORTRAIT ? "PORTRAIT" : "UNDEFINED or Square";
    }

    public static void savePreferenceAppShowingState(Context context, boolean isShow) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_PREFS_APP_SHOWED, isShow);
        editor.apply();
    }

    public static boolean isPreferenceAppShowed(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_PREFS_APP_SHOWED, false);
    }

    public static String getTimePeriodSettings(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREFS_PREFS_TIME_PRIOD, "15_min");
    }

    public static void saveTimePeriodSettings(Context context, String string) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_PREFS_TIME_PRIOD, string);
        editor.apply();
    }

    public static String getImgSourceSettings(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREFS_IMG_SOURCE, "yandex");
    }

    public static void saveImgSourceSettings(Context context, String keySource) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(Utils.PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_IMG_SOURCE, keySource);
        editor.apply();
    }

    public static int getPeriod(String key) {
        switch (key) {
            case "15_min": {
                return 15 * 60 * 1000;
            }
            case "1_hour": {
                return 60 * 60 * 1000;
            }
            case "24_hour": {
                return 24 * 60 * 60 * 1000;
            }
            case "8_hour": {
                return 8 * 60 * 60 * 1000;
            }
            case "5_s": {
                return 5 * 1000;
            }
            default:
                return 15 * 60 * 1000;
        }

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
        sendYAPPMEvent(YAPPEventName.LAYOUT_DENSITY_CHANGE, isStandard ? "Standard layout" : "Compact layout");
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
        sendYAPPMEvent(YAPPEventName.THEME_CHANGE, isWhiteTheme ? "White theme" : "Black theme");
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

    public static void saveThemeAndActivityRestart(Activity currentActivity, boolean isWhiteTheme) {
        currentActivity.finish();
        Utils.saveAppTheme(currentActivity.getApplicationContext(), isWhiteTheme);
//        refreshTheme(currentActivity.getApplication(), isWhiteTheme);
        restartCurrentActivity(currentActivity);
    }


//    public static void applyTheme(Activity currentActivity, boolean isWhiteTheme) {
//        currentActivity.finish();
//        Utils.saveAppTheme(currentActivity.getApplicationContext(), isWhiteTheme);
//        refreshTheme(currentActivity.getApplication(), isWhiteTheme);
//        restartCurrentActivity(currentActivity);
//    }
//    private static void refreshTheme(Application application, boolean isWhiteTheme) {
//        if (isWhiteTheme) {
//            application.setTheme(R.style.AppTheme_WhiteTheme);
//        } else {
//            application.getApplicationContext().setTheme(R.style.AppTheme_BlackTheme);
//        }
//    }

//    public static void refreshTheme(Application application) {
//        refreshTheme(application, Utils.isWhiteTheme(application.getApplicationContext()));
//    }

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
        sendYAPPMEvent(YAPPEventName.LAUNCH_SORT_APP_CHANGE, sort.name());
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


    @NonNull
    public static PopupMenu createApplicationPopupMenu(final View v, final ResolveInfo appInfo) {
        final PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.context_menu);
        final DBHelper dbHelper = new DBHelper(v.getContext());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_info: {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        ActivityInfo activityInfo = appInfo.activityInfo;
                        intent.setData(Uri.parse("package:" + activityInfo.packageName));
                        sendYAPPMEvent(YAPPEventName.LAUNCH_APP_INFO, activityInfo.name);
                        v.getContext().startActivity(intent);
                    }
                    return true;
                    case R.id.menu_delete: {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        ActivityInfo activityInfo = appInfo.activityInfo;
                        intent.setData(Uri.parse("package:" + activityInfo.packageName));
                        sendYAPPMEvent(YAPPEventName.LAUNCH_APP_DELETE, activityInfo.name);
                        v.getContext().startActivity(intent);
                        return true;
                    }
                    case R.id.menu_count: {
                        sendYAPPMEvent(YAPPEventName.LAUNCH_APP_STARTS_INFO, appInfo.activityInfo.name);
                        int startCount = DBUtils.getStartCount(appInfo, dbHelper.getWritableDatabase());
                        dbHelper.close();
                        Toast.makeText(v.getContext(), "start count = " + startCount, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    default:
                        return true;
                }
            }
        });
        return popup;
    }

    @NonNull
    public static Bitmap getProcessedBitmap(Bitmap bitmap, float containerWidth, float containerHeight) {
        final Bitmap processedBitmap = Bitmap.createBitmap((int) containerWidth, (int) containerHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(processedBitmap);


        Paint paint = new Paint();
        paint.setFilterBitmap(true);


        final float bWidth = bitmap.getWidth();
        final float bHeight = bitmap.getHeight();

        float dx = 0;
        float dy = 0;
        float scaleValue;
        if (containerHeight > containerWidth) {
            scaleValue = containerHeight / bHeight;
            dx = (containerWidth - bWidth * scaleValue) / 2;
        } else {
            scaleValue = containerWidth / bWidth;
            dy = (containerHeight - bHeight * scaleValue) / 2;
        }

        Matrix matrix = new Matrix();
        matrix.setScale(scaleValue, scaleValue);
        matrix.postTranslate(dx, dy);
        canvas.drawBitmap(bitmap, matrix, paint);
        return processedBitmap;
    }

}
