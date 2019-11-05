package com.dtag.notesapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    long insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("Delete From notes_table")
    void delete();


    @Query("Select * From notes_table order by priority_column asc")
    LiveData<List<Note>> getAllNotes();

    @Insert
    void insert(List<Note> allNotes);
}

