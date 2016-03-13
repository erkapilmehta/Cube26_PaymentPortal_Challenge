package com.pankaj.cube26_paymentportal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pankaj.cube26_paymentportal.Entities.Likes;

import java.util.ArrayList;

/**
 * Created by Pankaj on 13/03/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "payment_portal";
    public static final String TABLE_NAME = "likes";
    public static final String LIKE_ID = "id";
    public static final String GATEWAY_NAME = "name";
    public static final String TOTAL_COUNT = "totalcount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + LIKE_ID
                + " integer primary key, " + GATEWAY_NAME + " text, " + TOTAL_COUNT + " text DEFAULT '0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addLike(Likes obj) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GATEWAY_NAME, obj.getName());
        contentValues.put(TOTAL_COUNT, obj.getTotalCount());
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateLike(Likes obj) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GATEWAY_NAME, obj.getName());
        contentValues.put(TOTAL_COUNT, obj.getTotalCount());
        db.update(TABLE_NAME, contentValues, GATEWAY_NAME+"= ?", new String[]{obj.getName()});
        return true;
    }

    public ArrayList<Likes> getAllLikesArrayList() {
        ArrayList<Likes> arraylist = new ArrayList<Likes>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Likes obj = new Likes();
            obj.setName(res.getString(res.getColumnIndex(GATEWAY_NAME)));
            obj.setTotalCount(res.getString(res.getColumnIndex(TOTAL_COUNT)));
            arraylist.add(obj);
            res.moveToNext();
        }
        return arraylist;
    }

    public void closeDB() {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
