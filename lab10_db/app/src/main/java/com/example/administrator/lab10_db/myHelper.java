package com.example.administrator.lab10_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/6.
 */

public class myHelper extends SQLiteOpenHelper {
    public final String TABLE = "person";
    public final int DB_VERSION = 1;
    public myHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TABLE + "("
                + "name varchar primary key,"
                + "birthday varchar,"
                + "gift varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // nothing..
    }

    public void insert(Person person) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", person.getName());
        values.put("birthday", person.getBirthday());
        values.put("gift", person.getGift());
        db.insert(TABLE, null, values);
    }

    public void delete(String name) {
        SQLiteDatabase db =  getWritableDatabase();
        db.delete(TABLE, "name = ? ", new String[]{ name });
    }

    public void update(String name, Person person) {
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("birthday", person.getBirthday());
        values.put("gift", person.getGift());
        db.update(TABLE, values, "name = ?", new String[]{ name });
    }

    public boolean findIfDuplicate(String name) {
        SQLiteDatabase db =  getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, "name = ?", new String[]{ name }, null, null, null);
        if (cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public ArrayList<HashMap<String, String>> getPersons() {
        ArrayList<HashMap<String, String>> persons = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> m = new HashMap<String, String>();
                m.put("name", cursor.getString(cursor.getColumnIndex("name")));
                m.put("birthday", cursor.getString(cursor.getColumnIndex("birthday")));
                m.put("gift", cursor.getString(cursor.getColumnIndex("gift")));
                persons.add(m);
            } while (cursor.moveToNext());
        }

        return persons;
    }
}
