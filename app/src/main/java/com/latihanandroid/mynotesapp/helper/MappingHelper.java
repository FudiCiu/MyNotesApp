package com.latihanandroid.mynotesapp.helper;

import android.database.Cursor;

import com.latihanandroid.mynotesapp.db.DatabaseContract;
import com.latihanandroid.mynotesapp.entity.Note;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList(Cursor notesCursor){
        ArrayList<Note> noteList=new ArrayList<>();

        while (notesCursor.moveToNext()){
            int id= notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String title=notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE));
            String description= notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION));
            String date= notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));
            noteList.add(new Note(id,title,description,date));
        }
        return noteList;
    }
}
