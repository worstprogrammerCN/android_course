package com.example.andrew.final_term.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.andrew.final_term.Model.Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

public class DBService extends SQLiteOpenHelper {
    private static final String DB = "app.db";
    private static final String TABLE = "Notes";
    private static final int DB_VERSION = 1;

    private static final String LAST_MODIFIED_TIME = "lastModifiedTime";
    private static final String CONTEXT = "context";
    private static final String IMAGES = "images";
    private DBService(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DBService getService(Context context) {
        return new DBService(context, DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("create table if not exists %s (" +
                "%s long primary key," +
                "%s varchar," +
                "%s varchar)", TABLE, LAST_MODIFIED_TIME, CONTEXT, IMAGES));
    }

    public ArrayList<Info> getAllInfo() {
        ArrayList <Info> infoArrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Long lastModifiedTime =  cursor.getLong(cursor.getColumnIndex(LAST_MODIFIED_TIME));
                String content = cursor.getString(cursor.getColumnIndex(CONTEXT));

                // get ArrayList images
                String imagesStr = cursor.getString(cursor.getColumnIndex(IMAGES));
                ArrayList<String> images = new ArrayList<>();
                try {
                    JSONArray imagesJSONArray = new JSONArray(imagesStr);
                    for (int i = 0; i < imagesJSONArray.length(); i++)
                        images.add(imagesJSONArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                infoArrayList.add(new Info("", lastModifiedTime, content, images));
            } while (cursor.moveToNext());
        }
        return infoArrayList;
    }

    public Info getInfo(long lastModifiedTime) {
        SQLiteDatabase db = getReadableDatabase();
        Long lt = lastModifiedTime;
        Cursor cursor = db.query(TABLE, null, "lastModifiedTime = ?", new String[]{ lt.toString() }, null, null, null);
        if (cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndex(CONTEXT));

            // get ArrayList images
            String imagesStr = cursor.getString(cursor.getColumnIndex(IMAGES));
            ArrayList<String> images = new ArrayList<>();
            try {
                JSONArray imagesJSONArray = new JSONArray(imagesStr);
                for (int i = 0; i < imagesJSONArray.length(); i++)
                    images.add(imagesJSONArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new Info("", lastModifiedTime, content, images);
        }
        return null;
    }
    public void updateInfo(long lastModifiedTime,  String context, ArrayList<String> images) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONTEXT, context);
        cv.put(IMAGES, getStringFromImagesArray(images));
        db.update(TABLE, cv, String.format("%s = %s", LAST_MODIFIED_TIME, String.valueOf(lastModifiedTime)), null);
    }

    public void addInfo(long lastModifiedTime, String context, ArrayList<String> images) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LAST_MODIFIED_TIME, lastModifiedTime);
        cv.put(CONTEXT, context);
        cv.put(IMAGES, getStringFromImagesArray(images));
        db.insert(TABLE, null, cv);
    }

    public void deleteInfo(long lastModifiedTime) {
        SQLiteDatabase db= getWritableDatabase();
        db.delete(TABLE, String.format("%s = %s", LAST_MODIFIED_TIME, String.valueOf(lastModifiedTime)), null);
    }

    public String getStringFromImagesArray(ArrayList<String> images) {
        JSONArray arr = new JSONArray(images);
        return arr.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
