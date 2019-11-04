package com.dtag.notesapp.ui.updateNote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dtag.notesapp.persistence.Note;
import com.dtag.notesapp.repositories.NoteRepository;

public class UpdateNoteViewModel extends AndroidViewModel {

    private NoteRepository mNoteRepository;


    public UpdateNoteViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);

    }

    public void update(Note note) {
        mNoteRepository.update(note);

    }
}
