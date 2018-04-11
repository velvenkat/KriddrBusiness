package com.purple.kriddr;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pf-05 on 1/31/2018.
 */

public class dbhelp {

    private static final String DATABASE_NAME = "kriddr";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE = "user_master";
    public static final String KEY_USID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_BUS_STATUS = "bus_status";
    public static final String KEY_STATUS = "status";
    public static final String KEY_USER = "name";
    public static final String KEY_BUSINESS_ID = "business_id";
    public static final String KEY_URL = "url";
    public static final String KEY_BUSINESS_NAME = "business_name";
    public static final String KEY_BUSINESS_PHONE = "business_phone";
    public static final String KEY_BUSINESS_ADDRESS = "business_address";



    private SQLiteDatabase ourdb;
    private DatabaseHelper2 ourhelper;
    private final Context ourctx;






    protected static class DatabaseHelper2 extends SQLiteOpenHelper {

        public DatabaseHelper2(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_USID + " INTEGER NOT NULL, " +
                    KEY_USER + " TEXT, " +
                    KEY_EMAIL + " TEXT NOT NULL, " +
                    KEY_PHONE + " TEXT NOT NULL, " +
                    KEY_BUS_STATUS + " TEXT NOT NULL, " +
                    KEY_BUSINESS_ID + " TEXT NOT NULL, " +
                    KEY_URL + " TEXT NOT NULL, " +
                    KEY_BUSINESS_NAME + " TEXT NOT NULL, " +
                    KEY_BUSINESS_PHONE + " TEXT NOT NULL, " +
                    KEY_BUSINESS_ADDRESS + " TEXT NOT NULL, " +
                    KEY_STATUS + " TINYINT NOT NULL DEFAULT 0);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }


    public dbhelp(Context ctx) {
        ourctx = ctx;
    }

    public dbhelp open() throws SQLException {
        ourhelper = new DatabaseHelper2(ourctx);
        ourdb = ourhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourhelper.close();
    }

    public void createuser(String id2, String name, String email,  String phone, String bus_status, String status, String business_id, String business_name, String url, String business_phone, String business_address) {

        ContentValues cv = new ContentValues();
        cv.put(KEY_USID, id2);
        cv.put(KEY_USER, name);
        cv.put(KEY_EMAIL, email);
        cv.put(KEY_PHONE, phone);
        cv.put(KEY_BUS_STATUS,bus_status);
        cv.put(KEY_STATUS, status);
        cv.put(KEY_BUSINESS_ID,business_id);
        cv.put(KEY_BUSINESS_NAME,business_name);
        cv.put(KEY_URL,url);
        cv.put(KEY_BUSINESS_PHONE,business_phone);
        cv.put(KEY_BUSINESS_ADDRESS,business_address);
        ourdb.insert(DATABASE_TABLE, null, cv);

    }
    public void deleteTable(){
        String delQuery="delete from "+DATABASE_TABLE;
        ourdb.delete(DATABASE_TABLE, null, null);
      //  ourdb.rawQuery(delQuery,null);
    }




    public void logout_user() {
        ourdb.delete(DATABASE_TABLE, null, null);
    }






}
