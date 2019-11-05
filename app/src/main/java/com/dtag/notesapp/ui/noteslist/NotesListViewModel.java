package com.dtag.notesapp.ui.noteslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dtag.notesapp.persistence.Note;
import com.dtag.notesapp.repositories.NoteRepository;

import java.util.List;

public class NotesListViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private LiveData<List<Note>> mAllNotes;

    public NotesListViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mAllNotes = mNoteRepository.getAllNotes();
    }


    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void delete(Note note) {
        mNoteRepository.delete(note);
    }


}
