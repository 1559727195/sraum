package com.Util;

/**
 * Created by masskywcy on 2016-07-29.
 */
/*创建数据库*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sraum.db";
    //数据库版本迭代
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS boxdevice" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR, number VARCHAR, " +
                "name VARCHAR,status VARCHAR,sign VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS alldevice" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, type VARCHAR, number VARCHAR, " +
                "name VARCHAR,status VARCHAR,mode VARCHAR,dimmer VARCHAR,temperature VARCHAR,speed VARCHAR)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2:
                break;
        }
    }
}
