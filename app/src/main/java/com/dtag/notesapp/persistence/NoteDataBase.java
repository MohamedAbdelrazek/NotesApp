package com.dtag.notesapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDataBase extends RoomDatabase {

    public abstract NoteDao noteDao();

    public static NoteDataBase instance;

    public static synchronized NoteDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDataBase.class, "note_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}