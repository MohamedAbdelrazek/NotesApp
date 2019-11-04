package com.dtag.notesapp.ui.login;

import androidx.lifecycle.ViewModel;

import com.dtag.notesapp.livedatawrapper.NoteStateLiveData;
import com.dtag.notesapp.repositories.NoteRepository;

public class LoginViewModel extends ViewModel {
    private NoteRepository mNoteRepository;

    public NoteStateLiveData<Boolean> islogedIn;

    public LoginViewModel() {
        mNoteRepository = new NoteRepository();
    }

    public NoteStateLiveData<Boolean> login(String userName, String password) {

        islogedIn = mNoteRepository.signInUser(userName, password);
        return islogedIn;
    }

}
