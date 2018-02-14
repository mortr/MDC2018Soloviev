package com.mortr.soloviev.mdc2018soloviev.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.CREATE_TABLE;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DB_NAME;
import static com.mortr.soloviev.mdc2018soloviev.db.DBUtils.MyDatabase.DROP_TABLE_SCRIPT;


public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_SCRIPT);
        db.execSQL(CREATE_TABLE);
    }
}
