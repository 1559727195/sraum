package com.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.data.AllDevice;
import com.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by masskywcy on 2017-01-10.
 */
//用于设备信息alldevice表中查询
public class DbDevice {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DbDevice(Context context) {
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
    public void add(List<User.device> users) {
        db.beginTransaction();  //开始事务
        try {
            for (User.device allbox : users) {
                db.execSQL("INSERT INTO alldevice VALUES(null, ? , ?,  ?, ?, ?, ?, ?, ?)", new Object[]{allbox.type, allbox.number,
                        allbox.name, allbox.status, allbox.mode, allbox.dimmer, allbox.temperature, allbox.speed});
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
    public void updateAge(AllDevice allDevice) {
        ContentValues cv = new ContentValues();
        cv.put("deviceMac", allDevice.number);
        db.update("user", cv, "deviceName = ?", new String[]{allDevice.number});
    }

    /**
     * delete old person
     *
     * @param
     */
    //删除数据库中user中所有数据
    public void deleteTable() {
        db.execSQL("DELETE FROM alldevice");
    }


    /**
     * query all persons, return list
     *
     * @return
     */
    public List<User.device> query() {
        List<User.device> users = new ArrayList<User.device>();
        Cursor c = queryTheCursor();
        for (int i = 0; i < c.getCount(); i++)
            while (c.moveToNext()) {
                User.device user = new User.device();
                user.type = c.getString(c.getColumnIndex("type"));
                user.number = c.getString(c.getColumnIndex("number"));
                user.name = c.getString(c.getColumnIndex("name"));
                user.status = c.getString(c.getColumnIndex("status"));
                user.mode = c.getString(c.getColumnIndex("mode"));
                user.dimmer = c.getString(c.getColumnIndex("dimmer"));
                user.temperature = c.getString(c.getColumnIndex("temperature"));
                user.speed = c.getString(c.getColumnIndex("speed"));
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
        Cursor c = db.rawQuery("SELECT * FROM alldevice", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}

