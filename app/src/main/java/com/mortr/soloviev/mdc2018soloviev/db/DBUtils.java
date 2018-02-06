package com.mortr.soloviev.mdc2018soloviev.db;


import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.DATE_INSTALLED;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.IS_SYSTEM;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.PACKAGE_NAME;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.Columns.STARTS_COUNT;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DB_SQL_SELECT_APPS;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DB_SQL_SELECT_APP_WITH_NAME;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.TABLE_NAME;

public class DBUtils {

    public interface MyDatabase {
        static final String DB_NAME = "launcher.app";
        static final String TABLE_NAME = "apps";

        static final String DB_SQL_SELECT_APP_WITH_NAME = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + PACKAGE_NAME + " = ?";
        static final String DB_SQL_SELECT_APPS = "SELECT * FROM " + TABLE_NAME;

        static interface Columns extends BaseColumns {
            String PACKAGE_NAME = "PACKAGE_NAME";
            String DATE_INSTALLED = "DATE_INSTALLED";
            String IS_SYSTEM = "IS_SYSTEM";
            String STARTS_COUNT = "STARTS_COUNT";
        }

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        Columns._ID + " INTEGER PRIMARY KEY," +
                        Columns.STARTS_COUNT + " INTEGER, " +
                        Columns.DATE_INSTALLED + " INTEGER, " +
                        Columns.IS_SYSTEM + " INTEGER, " +
                        Columns.PACKAGE_NAME + " TEXT )";

        static final String DROP_TABLE_SCRIPT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static ContentValues createContentValues(ResolveInfo info, Context context, int startCount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_SYSTEM, isSystemApp(context, info.activityInfo.packageName) ? 1 : 0);
        contentValues.put(PACKAGE_NAME, info.activityInfo.packageName);
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

    private static DBApplicationItem createDBApplicationItem(Cursor cursor) {
        return new DBApplicationItem(
                cursor.getString(cursor.getColumnIndex(MyDatabase.Columns.PACKAGE_NAME)),
                cursor.getLong(cursor.getColumnIndex(MyDatabase.Columns.DATE_INSTALLED)),
                cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.IS_SYSTEM)) == 1,
                cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.STARTS_COUNT)));
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

    public static DBApplicationItem getDBApplicationItemFromDB(ResolveInfo info, SQLiteDatabase db) {
        String[] args = {info.activityInfo.packageName};
        DBApplicationItem dbApplicationItem = null;
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APP_WITH_NAME, args);
        if (cursor.moveToFirst()) {
            dbApplicationItem = createDBApplicationItem(cursor);
        }
        cursor.close();
        return dbApplicationItem;
    }

    public static void onStartApp(ResolveInfo info, Context context, SQLiteDatabase db) {
        int startCount = 1;
        String[] args = {info.activityInfo.packageName};
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APP_WITH_NAME, args);
        if (cursor.moveToFirst()) {
            startCount += cursor.getInt(cursor.getColumnIndex(MyDatabase.Columns.STARTS_COUNT));

        }
        cursor.close();

        ContentValues contentValues = createContentValues(info, context, startCount);
        if (startCount == 1) {
            db.insert(TABLE_NAME, MyDatabase.Columns.IS_SYSTEM, contentValues);
        } else {
            db.updateWithOnConflict(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", args, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public static void onRemoveApp(ResolveInfo info, SQLiteDatabase db) {
        db.delete(TABLE_NAME, PACKAGE_NAME + " = ?", new String[]{info.activityInfo.packageName});
    }

    public static List<DBApplicationItem> getDBApplications(Context context, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(DB_SQL_SELECT_APPS, null);
        List<DBApplicationItem> applicationItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            applicationItems.add(createDBApplicationItem(cursor));
        }
        cursor.close();
        return applicationItems;
    }


    public static boolean isSystemApp(Context context, String packageName) {
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
