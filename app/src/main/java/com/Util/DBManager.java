package com.Util;

/**
 * Created by masskywcy on 2016-07-29.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.data.Allbox;

import java.util.ArrayList;
import java.util.List;

/*用于数据库表boxdevice查询操作*/
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     *
     * @param
     */
    public void add(List<Allbox> users) {
        db.beginTransaction();  //开始事务
        try {
            for (Allbox allbox : users) {
                db.execSQL("INSERT INTO boxdevice VALUES(null, ? , ?,  ?, ?, ?)", new Object[]{allbox.type, allbox.number,
                        allbox.name, allbox.status, allbox.sign});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update person's age
     *
     * @param
     */
    public void updateAge(Allbox allbox) {
        ContentValues cv = new ContentValues();
        cv.put("deviceMac", allbox.number);
        db.update("user", cv, "deviceName = ?", new String[]{allbox.number});
    }

    /**
     * delete old person
     *
     * @param
     */
    //删除数据库中user中所有数据
    public void deleteTable() {
        db.execSQL("DELETE FROM boxdevice");
    }


    /**
     * query all persons, return list
     *
     * @return
     */
    public List<Allbox> query() {
        List<Allbox> users = new ArrayList<Allbox>();
        Cursor c = queryTheCursor();
        for (int i = 0; i < c.getCount(); i++)
            while (c.moveToNext()) {
                Allbox user = new Allbox();
                user.type = c.getString(c.getColumnIndex("type"));
                user.number = c.getString(c.getColumnIndex("number"));
                user.name = c.getString(c.getColumnIndex("name"));
                user.status = c.getString(c.getColumnIndex("status"));
                user.sign = c.getString(c.getColumnIndex("sign"));
                users.add(user);
            }
        c.close();
        return users;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM boxdevice", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
