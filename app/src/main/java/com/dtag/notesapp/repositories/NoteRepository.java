package com.dtag.notesapp.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.dtag.notesapp.livedatawrapper.NoteStateLiveData;
import com.dtag.notesapp.models.UserModel;
import com.dtag.notesapp.persistence.Note;
import com.dtag.notesapp.persistence.NoteDao;
import com.dtag.notesapp.persistence.NoteDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;
    private FirebaseAuth mAuth;
    private String TAG = "zoka";
    DatabaseReference myRef;


    public NoteRepository(Application application) {
        NoteDataBase noteDataBase = NoteDataBase.getInstance(application);
        mNoteDao = noteDataBase.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users");

    }

    public NoteRepository() {
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users");
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

    public NoteStateLiveData<Boolean> signUpUser(final String email, final String password, final String displayName) {
        final NoteStateLiveData<Boolean> registerState = new NoteStateLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            registerState.postSuccess(true);

                            String uid = mAuth.getCurrentUser().getUid();
                            myRef.child(uid).child("UserInfo ").setValue(new UserModel(email, displayName));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            registerState.postError(task.getException());
                        }
                    }
                });
        return registerState;
    }

    public NoteStateLiveData<Boolean> signInUser(final String email, final String password) {

        final NoteStateLiveData<Boolean> loginState = new NoteStateLiveData<>();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            loginState.postSuccess(true);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                            loginState.postError(task.getException());
                        }

                        // ...
                    }
                });
        return loginState;
    }
}