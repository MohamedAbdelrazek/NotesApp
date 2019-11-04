package com.dtag.notesapp.ui.addNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dtag.notesapp.persistence.Note;
import com.dtag.notesapp.repositories.NoteRepository;

public class AddNoteViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;


    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);

    }

    public void insert(Note note) {
        mNoteRepository.insert(note);
    }
}
