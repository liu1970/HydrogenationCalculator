package com.liunian.jqzx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AeDao {

    private SQLittle sq;
    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public AeDao(Context context) {
        sq = SQLittle.getInstance(context);
        write = sq.openWriteLink();
        read = sq.getReadableDatabase();
    }

    public long insert(AE ae) {
        ContentValues cvr = new ContentValues();
        cvr.put("a_no", ae.getA_NO());
        cvr.put("bz", ae.getBZ());
        cvr.put("ce", ae.getCE());
        cvr.put("ca", ae.getCA());
        cvr.put("q", ae.getQ());
        cvr.put("mpca", ae.getMPCA());
        cvr.put("time",ae.getTIME());
        long ae1 = write.insert("ae", null, cvr);
        cvr.clear();
        return ae1;
    }

    public int delete(String condition) {
        return write.delete("ae", condition, null);
    }

    public int update(AE ae, String condition) {
        ContentValues cvr = new ContentValues();
        cvr.put("a_no", ae.getA_NO());
        cvr.put("bz", ae.getBZ());
        cvr.put("ce", ae.getCE());
        cvr.put("ca", ae.getCA());
        cvr.put("q", ae.getQ());
        cvr.put("mpca", ae.getMPCA());
        int ae1 = write.update("ae", cvr, condition, null);
        cvr.clear();
        return ae1;
    }

    public ArrayList<AE> query(String condition) {
        String sql = String.format("select * from ae where a_no='%s' order by time desc limit 0,1", condition);
        ArrayList<AE> list = new ArrayList<>();
        Cursor cursor = read.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            AE ae = new AE();
            ae.setTIME(cursor.getString(1));
            ae.setA_NO(cursor.getString(2));
            ae.setBZ(cursor.getString(3));
            ae.setCE(cursor.getString(4));
            ae.setCA(cursor.getString(5));
            ae.setQ(cursor.getString(6));
            ae.setMPCA(cursor.getString(7));
            list.add(ae);
        }
        cursor.close();
        return list;
    }
    public ArrayList<AE> query() {
        String sql = "select * from ae";
        ArrayList<AE> list = new ArrayList<>();
        Cursor cursor = read.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            AE ae = new AE();
            ae.setTIME(cursor.getString(1));
            ae.setA_NO(cursor.getString(2));
            ae.setBZ(cursor.getString(3));
            ae.setCE(cursor.getString(4));
            ae.setCA(cursor.getString(5));
            ae.setQ(cursor.getString(6));
            ae.setMPCA(cursor.getString(7));
            list.add(ae);
        }
        cursor.close();
        return list;
    }

    public void closeLink() {
        if (write != null && write.isOpen()) {
            write.close();
            write = null;
        }
        if (read != null && read.isOpen()) {
            read.close();
            read = null;
        }
    }

}
