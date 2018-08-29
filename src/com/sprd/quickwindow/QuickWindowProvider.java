package com.sprd.quickwindow;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class QuickWindowProvider extends ContentProvider {

    private final static String TAG = "provider";
    private SQLiteDatabase mDatabase = null;
    private static final String TABLE_NAME = "HallValue";

    @Override
    public boolean onCreate() {
        QuickWindowOpenHelper helper = new QuickWindowOpenHelper(getContext(), "hallvalue.db", null, 1);
        mDatabase = helper.getWritableDatabase();
        return false;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        return mDatabase.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder) ;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mDatabase.insert(TABLE_NAME, null, values);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        mDatabase.delete(TABLE_NAME, selection, selectionArgs);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        mDatabase.update(TABLE_NAME, values, selection, selectionArgs);
        return 0;
    }

    class QuickWindowOpenHelper extends SQLiteOpenHelper {

        public QuickWindowOpenHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String CREATE_TABLE_CMD = "create table " + TABLE_NAME + "(" +
                    "id integer primary key," + "hallvalue integer );";
            final String INSERT_FIRST_VALUE = "insert into HallValue (id,hallvalue)values(?,?);";
            db.execSQL(CREATE_TABLE_CMD);
            db.execSQL(INSERT_FIRST_VALUE, new Object[]{1,-1});
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }
    }
}
