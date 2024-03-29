package com.latihanandroid.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.latihanandroid.mynotesapp.entity.Note;

import java.util.ArrayList;

public class NoteHelper{
    private static final String DATABASE_TABLE= DatabaseContract.TABLE_NOTE;
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    private NoteHelper(Context context) {
        databaseHelper= new DatabaseHelper(context);

    }

    public static NoteHelper getInstance(Context context){
        if (INSTANCE==null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE==null){
                    INSTANCE=new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database=databaseHelper.getWritableDatabase();
    }
    public void close(){
        databaseHelper.close();

        if (database.isOpen()){
            database.close();
        }
    }

    public ArrayList<Note> getAllNotes(){
        ArrayList<Note> arrayList=new ArrayList<>();
        Cursor cursor= database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.NoteColumns._ID+" ASC");
        cursor.moveToFirst();
        Note note;
        if (cursor.getCount()>0){
            do {
                note=new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.NoteColumns._ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseContract.NoteColumns.TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseContract.NoteColumns.DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.NoteColumns.DATE)));
                arrayList.add(note);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertNote(Note note){
        ContentValues args=new ContentValues();
        args.put(DatabaseContract.NoteColumns.TITLE,note.getTitle());
        args.put(DatabaseContract.NoteColumns.DESCRIPTION,note.getDescription());
        args.put(DatabaseContract.NoteColumns.DATE,note.getDate());
        return database.insert(DATABASE_TABLE,null,args);
    }

    public int updateNote(Note note){
        ContentValues args=new ContentValues();
        args.put(DatabaseContract.NoteColumns.TITLE,note.getTitle());
        args.put(DatabaseContract.NoteColumns.DESCRIPTION,note.getDescription());
        args.put(DatabaseContract.NoteColumns.DATE,note.getDate());
        return database.update(DATABASE_TABLE,args,DatabaseContract.NoteColumns._ID+"='"+note.getId()+"'",null);
    }

    public int deleteNote(int id){
        return database.delete(DATABASE_TABLE,DatabaseContract.NoteColumns._ID+"='"+id+"'",null);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null,
                DatabaseContract.NoteColumns._ID+" =?",
                new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
        ,null
        ,null
        ,null
        ,null
        ,null
        ,DatabaseContract.NoteColumns._ID+" ASC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }

    public int updateProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE,values,
                DatabaseContract.NoteColumns._ID+" =?",
                new String[]{id});
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,DatabaseContract.NoteColumns._ID+" =?",
                new String[]{id});
    }

}
