package com.sweng888.androiduiandlogin_ericbratter.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sweng888.androiduiandlogin_ericbratter.repository.tables.UserTable;

public class DatabaseAccess extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "app";

    public DatabaseAccess(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseAccess(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // In case the database is a cache for online data, you can discard the existing data
        // once the app is gathering information from an external data source.
        db.execSQL(UserTable.drop());
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
