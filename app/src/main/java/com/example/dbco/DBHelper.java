package com.example.dbco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME="productDB";
    public static final String TABLE_PRODUCTS="products";
    public static final String KEY_ID="_id";
    public static final String KEY_PRODUCT="name";
    public static final String KEY_PRICE="mail";
    public static final String TABLE_USERS="users";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PRODUCTS + "(" + KEY_ID + " integer primary key," + KEY_PRODUCT + " text," +  KEY_PRICE+ " text" + ")");
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID + " integer primary key," + KEY_LOGIN + " text," +  KEY_PASSWORD+ " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PRODUCTS);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
    }
}
