package com.dtag.notesapp.persistence;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title_column")
    private String title;

    @ColumnInfo(name = "description_column")
    private String description;

    @ColumnInfo(name = "priority_column")
    private int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }


    @Ignore
    public Note(int id, String title, String description, int priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
