package com.liunian.jqzx;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLittle extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "jq.db";
    private static SQLittle sqLittle = null;
    private final String TABLE_AE101B = "ae";
    private SQLiteDatabase db = null;

    private SQLittle(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static SQLittle getInstance(Context context) {
        if (sqLittle == null) {
            sqLittle = new SQLittle(context);
        }
        return sqLittle;
    }

    public SQLiteDatabase openReadLink() {
        if (db == null || !db.isOpen()) {
            db = sqLittle.getReadableDatabase();
        }
        return db;
    }

    public SQLiteDatabase openWriteLink() {
        if (db == null || !db.isOpen()) {
            db = sqLittle.getWritableDatabase();
        }
        return db;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_ae101b_sql = "create table if not exists " + TABLE_AE101B + " (" +
                "_id integer primary key autoincrement not null," +
                "time date," +
                "a_no varchar(20),bz varchar(10),ce vachar(10),ca varchar(10),q varchar(10),mpca varchar(10))";
        db.execSQL(create_ae101b_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
