package com.dtag.notesapp.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dtag.notesapp.persistence.Note;
import com.dtag.notesapp.persistence.NoteDao;
import com.dtag.notesapp.persistence.NoteDataBase;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        NoteDataBase noteDataBase = NoteDataBase.getInstance(application);
        mNoteDao = noteDataBase.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public void insert(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mNoteDao.insert(note);
                return null;
            }
        }.execute();
    }

    public void update(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mNoteDao.update(note);
                return null;
            }
        }.execute();
    }

    public void delete(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mNoteDao.delete(note);
                return null;
            }
        }.execute();
    }

    public void deleteAllNotes() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mNoteDao.delete();
                return null;
            }
        }.execute();
    }

    // Room DB executes LiveData Types in the back ground so we don't need to wrap this fun in AsyncTask....
    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }
}