package com.latihanandroid.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.latihanandroid.mynotesapp.MainActivity;
import com.latihanandroid.mynotesapp.db.DatabaseContract;
import com.latihanandroid.mynotesapp.db.NoteHelper;

public class NoteProvider extends ContentProvider {
    private static final int NOTE=1;
    private static final int NOTE_ID=2;
    private static final UriMatcher sUriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
    private NoteHelper noteHelper;
    static {
        sUriMatcher.addURI(DatabaseContract.AUTHORITY,DatabaseContract.TABLE_NOTE,NOTE);
        sUriMatcher.addURI(DatabaseContract.AUTHORITY,DatabaseContract.TABLE_NOTE+"/#",NOTE_ID);
    }
    @Override
    public boolean onCreate() {
        noteHelper=NoteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        noteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case NOTE:
                cursor=noteHelper.queryProvider();
                break;
            case NOTE_ID:
                cursor=noteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor=null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        noteHelper.open();
        long added;
        switch (sUriMatcher.match(uri)){
            case NOTE:
                added=noteHelper.insertProvider(contentValues);
                break;
            default:
                added=0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.NoteColumns.CONTENT_URI,new MainActivity.DataObserver(new Handler(),getContext()));
        return Uri.parse(DatabaseContract.NoteColumns.CONTENT_URI+"/"+added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        noteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)){
            case NOTE_ID:
                deleted=noteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted=0;
                break;
        }

        getContext().getContentResolver().notifyChange(DatabaseContract.NoteColumns.CONTENT_URI,new MainActivity.DataObserver(new Handler(),getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        noteHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)){
            case NOTE_ID:
                updated=noteHelper.updateProvider(uri.getLastPathSegment(),contentValues);
                break;
            default:
                updated=0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.NoteColumns.CONTENT_URI,new MainActivity.DataObserver(new Handler(),getContext()));
        return updated;
    }
}
