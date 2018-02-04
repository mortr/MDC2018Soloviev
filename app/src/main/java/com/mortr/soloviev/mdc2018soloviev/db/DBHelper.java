package com.mortr.soloviev.mdc2018soloviev.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.mortr.soloviev.mdc2018soloviev.db.DBHelper.MyDatabase.DB_NAME;


public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public interface MyDatabase {
        static final String DB_NAME = "launcher.app";
        static final String TABLE_NAME = "records";


        static interface Columns extends BaseColumns {
            String PACKAGE_NAME = "PACKAGE_NAME";
            String STARTS_COUNT = "STARTS_COUNT";
        }

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        Columns.STARTS_COUNT + " NUMBER, " +
                        Columns.PACKAGE_NAME + " TEXT )";

        static final String DROP_TABLE_SCRIPT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyDatabase.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
