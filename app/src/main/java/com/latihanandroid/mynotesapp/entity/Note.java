package com.latihanandroid.mynotesapp.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.latihanandroid.mynotesapp.db.DatabaseContract;

public class Note implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String date;

    public Note() {
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Note(int id, String title, String description, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Note(Cursor cursor){
        this.id= DatabaseContract.NoteColumns.getColumnInt(cursor,DatabaseContract.NoteColumns._ID);
        this.title=DatabaseContract.NoteColumns.getColumnString(cursor,DatabaseContract.NoteColumns.TITLE);
        this.description=DatabaseContract.NoteColumns.getColumnString(cursor,DatabaseContract.NoteColumns.DESCRIPTION);
        this.date=DatabaseContract.NoteColumns.getColumnString(cursor,DatabaseContract.NoteColumns.DATE);
    }

}
