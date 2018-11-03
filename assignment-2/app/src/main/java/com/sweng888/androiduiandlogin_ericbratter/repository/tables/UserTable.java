package com.sweng888.androiduiandlogin_ericbratter.repository.tables;

import com.sweng888.androiduiandlogin_ericbratter.model.User;

public class UserTable {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_FIRST_NAME = "first_name";
    public static final String COLUMN_NAME_LAST_NAME = "last_name";
    public static final String COLUMN_NAME_BIRTHDAY = "birthday";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_MOBILE_PHONE = "mobile_phone";

    public static String create(){
        return new String ( "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " TEXT PRIMARY KEY," +
                COLUMN_NAME_FIRST_NAME + " TEXT," +
                COLUMN_NAME_LAST_NAME  + " TEXT," +
                COLUMN_NAME_BIRTHDAY + " TEXT," +
                COLUMN_NAME_MOBILE_PHONE + " TEXT," +
                COLUMN_NAME_PASSWORD + " TEXT)" );
    }

    public static final String drop(){
        return "DROP TABLE IF EXISTS " +TABLE_NAME;
    }
}
