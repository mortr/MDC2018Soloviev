package com.mortr.soloviev.mdc2018soloviev.db;


import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.ACTIVITY_NAME;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.DATE_INSTALLED;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.DESKTOP_COORDINATES_X;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.DESKTOP_COORDINATES_Y;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.IS_DESKTOP;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.IS_SYSTEM;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.PACKAGE_NAME;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.STARTS_COUNT;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DB_SQL_SELECT_APPS;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DB_SQL_SELECT_APP_WITH_FROM_DESKTOP;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DB_SQL_SELECT_APP_WITH_NAME;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.TABLE_NAME;
import static com.mortr.soloviev.mdc2018soloviev.ui.launcher.LauncherActivity.DESKTOP_X;
import static com.mortr.soloviev.mdc2018soloviev.ui.launcher.LauncherActivity.DESKTOP_Y;

public class DBUtils {

    public interface MyDatabase {
        String DB_NAME = "launcher.app";
        String TABLE_NAME = "apps";
        String DB_SQL_SELECT_APP_WITH_FROM_DESKTOP = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + IS_DESKTOP + " = ?";
        String DB_SQL_SELECT_APP_WITH_NAME = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + PACKAGE_NAME + " = ?";
        String DB_SQL_SELECT_APPS = "SELECT * FROM " + TABLE_NAME;

        interface Columns extends BaseColumns {
            String PACKAGE_NAME = "PACKAGE_NAME";
            String ACTIVITY_NAME = "ACTIVITY_NAME";
            String DATE_INSTALLED = "DATE_INSTALLED";
            String IS_SYSTEM = "IS_SYSTEM";
            String STARTS_COUNT = "STARTS_COUNT";
            String IS_DESKTOP = "IS_DESKTOP";
            String DESKTOP_COORDINATES_X = "DESKTOP_COORDINATES_X";
            String DESKTOP_COORDINATES_Y = "DESKTOP_COORDINATES_Y";
        }

        String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        Columns._ID + " INTEGER PRIMARY KEY," +
                        Columns.STARTS_COUNT + " INTEGER, " +
                        Columns.DATE_INSTALLED + " INTEGER, " +
                        Columns.IS_SYSTEM + " INTEGER, " +
                        Columns.IS_DESKTOP + " INTEGER, " +
                        Columns.DESKTOP_COORDINATES_X + " DOUBLE, " +
                        DESKTOP_COORDINATES_Y + " DOUBLE, " +
                        Columns.ACTIVITY_NAME + " TEXT," +
                        Columns.PACKAGE_NAME + " TEXT )";

        @SuppressWarnings("unused")
        String DROP_TABLE_SCRIPT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static ContentValues createContentValues(ResolveInfo info, Context context, int startCount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_SYSTEM, isSystemApp(context, info.activityInfo.packageName) ? 1 : 0);
        contentValues.put(PACKAGE_NAME, info.activityInfo.packageName);
        contentValues.put(ACTIVITY_NAME, info.activityInfo.name);
        contentValues.put(STARTS_COUNT, startCount);
        try {
            contentValues.put(DATE_INSTALLED,
                    context.getPackageManager().getPackageInfo(info.activityInfo.packageName, 0).firstInstallTime);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            contentValues.put(DATE_INSTALLED, 0);
        }
        return contentValues;
    }

    private static ContentValues createContentValues(@NonNull ComponentName componentName, float x, float y, Context context, @Nullable DBApplicationItem dbApplicationItem) {
        ContentValues contentValues = new ContentValues();
        String packageName = componentName.getPackageName();
        contentValues.put(PACKAGE_NAME, packageName);
        contentValues.put(ACTIVITY_NAME, componentName.getClassName());
        contentValues.put(IS_DESKTOP, 1);
        contentValues.put(DESKTOP_COORDINATES_X, x);
        contentValues.put(DESKTOP_COORDINATES_Y, y);
        if (dbApplicationItem != null) {
            contentValues.put(IS_SYSTEM, dbApplicationItem.isSystem() ? 1 : 0);
            contentValues.put(STARTS_COUNT, dbApplicationItem.getStartsCount());
            contentValues.put(DATE_INSTALLED, dbApplicationItem.getDateInstalled());
        } else {
            contentValues.put(IS_SYSTEM, isSystemApp(context, packageName) ? 1 : 0);
            contentValues.put(STARTS_COUNT, 0);
            try {
                contentValues.put(DATE_INSTALLED,
                        context.getPackageManager().getPackageInfo(packageName, 0).firstInstallTime);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                contentValues.put(DATE_INSTALLED, 0);
            }

        }
        return contentValues;
    }

    private static DBApplicationItem createDBApplicationItem(Cursor cursor) {
        return new DBApplicationItem(cursor.getString(cursor.getColumnIndex(MyDatabase.Columns.PACKAGE_NAME)),
                cursor.getString(cursor.getColumnIndex(MyDatabase.Columns.ACTIVITY_NAME)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.Columns.DATE_INSTALLED)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.IS_SYSTEM)) == 1,
                cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.STARTS_COUNT)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.IS_DESKTOP)) == 1,
                cursor.getFloat(cursor.getColumnIndex(MyDatabase.Columns.DESKTOP_COORDINATES_X)),
                cursor.getFloat(cursor.getColumnIndex(MyDatabase.Columns.DESKTOP_COORDINATES_Y)));
    }

    public static int getStartCount(ResolveInfo info, SQLiteDatabase db) {
        String[] args = {info.activityInfo.packageName};
        int startCount = 0;
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APP_WITH_NAME, args);
        if (cursor.moveToFirst()) {
            startCount = cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.STARTS_COUNT));
        }
        cursor.close();
        return startCount;
    }

    public static DBApplicationItem getDBApplicationItemFromDB(ComponentName name, SQLiteDatabase db) {
        String[] args = {name.getPackageName()};
        DBApplicationItem dbApplicationItem = null;
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APP_WITH_NAME, args);
        if (cursor.moveToFirst()) {
            dbApplicationItem = createDBApplicationItem(cursor);
        }
        cursor.close();
        return dbApplicationItem;
    }

    public static void updateDesktopApp(SQLiteDatabase writableDatabase, ComponentName componentName, float x, float y) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DESKTOP_COORDINATES_X, x);
        contentValues.put(DESKTOP_COORDINATES_Y, y);
        String[] args = {componentName.getPackageName()};
        writableDatabase.updateWithOnConflict(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", args, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static List<DBApplicationItem> getDesktopDBApplicationItemFromDB(SQLiteDatabase db) {
        List<DBApplicationItem> list = new ArrayList<>();
        String[] args = {"1"};
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APP_WITH_FROM_DESKTOP, args);
        while (cursor.moveToNext()) {
            list.add(createDBApplicationItem(cursor));
        }
        cursor.close();
        return list;
    }

    public static void saveDesktopApp(DBHelper dbHelper, ComponentName componentName, Bundle placeCoordinatesForAppChoose, Context context) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DBApplicationItem dbApplicationItem = getDBApplicationItemFromDB(componentName, db);
        float x = 0;
        float y = 0;
        if (placeCoordinatesForAppChoose != null) {
            x = placeCoordinatesForAppChoose.getFloat(DESKTOP_X);
            y = placeCoordinatesForAppChoose.getFloat(DESKTOP_Y);
        }
        ContentValues contentValues = createContentValues(componentName, x, y, context, dbApplicationItem);
        if (dbApplicationItem == null) {
            db.insert(TABLE_NAME, MyDatabase.Columns.IS_SYSTEM, contentValues);
        } else {
            String[] args = {componentName.getPackageName()};
            db.updateWithOnConflict(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", args, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public static void deleteAppFromDesktop(DBHelper dbHelper, ComponentName componentName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        DBApplicationItem dbApplicationItem = getDBApplicationItemFromDB(componentName, db);
        if (dbApplicationItem != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(IS_DESKTOP, 0);
            contentValues.put(DESKTOP_COORDINATES_X, 0);
            contentValues.put(DESKTOP_COORDINATES_Y, 0);
            String[] args = {componentName.getPackageName()};
            db.updateWithOnConflict(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", args, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }


    public static void onStartApp(ResolveInfo info, Context context, SQLiteDatabase db) {
        int startCount = 1;
        String[] args = {info.activityInfo.packageName};
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APP_WITH_NAME, args);
        boolean isFirst = true;
        if (cursor.moveToFirst()) {
            startCount += cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.STARTS_COUNT));
            isFirst = false;
        }
        cursor.close();

        ContentValues contentValues = createContentValues(info, context, startCount);
        if (isFirst) {
            db.insert(TABLE_NAME, MyDatabase.Columns.IS_SYSTEM, contentValues);
        } else {
            db.updateWithOnConflict(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", args, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

//    public static void onRemoveApp(ResolveInfo info, SQLiteDatabase db) {
//        db.delete(TABLE_NAME, PACKAGE_NAME + " = ?", new String[]{info.activityInfo.packageName});
//    }

    @SuppressWarnings("unused")
    public static List<DBApplicationItem> getDBApplications(Context context, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APPS, null);
        List<DBApplicationItem> applicationItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            applicationItems.add(createDBApplicationItem(cursor));
        }
        cursor.close();
        return applicationItems;
    }


    private static boolean isSystemApp(Context context, String packageName) {
        final ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (info.flags & mask) != 0;
    }

}
