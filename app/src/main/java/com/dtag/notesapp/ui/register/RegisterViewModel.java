package com.dtag.notesapp.ui.register;

import androidx.lifecycle.ViewModel;

import com.dtag.notesapp.livedatawrapper.NoteStateLiveData;
import com.dtag.notesapp.repositories.NoteRepository;

public class RegisterViewModel extends ViewModel {

    private NoteRepository mNoteRepository;

    public NoteStateLiveData<Boolean> registerState;

    public RegisterViewModel() {
        mNoteRepository = new NoteRepository();
    }

    public void registerUser(String userName, String password, String displayName) {
        registerState=  mNoteRepository.signUpUser(userName, password, displayName);
    }
}
