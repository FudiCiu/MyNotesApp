package com.latihanandroid.mynotesapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME="dbnoteapp";
    public static final int DATABASE_VERSION=1;
    private static final String SQL_CREATE_TABLE_NOTE=String.format("CREATE TABLE %s"+
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_NOTE,
            DatabaseContract.NoteColumns._ID,
            DatabaseContract.NoteColumns.TITLE,
            DatabaseContract.NoteColumns.DATE,
            DatabaseContract.NoteColumns.DESCRIPTION
            );

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_NOTE);
        onCreate(sqLiteDatabase);
    }


}
