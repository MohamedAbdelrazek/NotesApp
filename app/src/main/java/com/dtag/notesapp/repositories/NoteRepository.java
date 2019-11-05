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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;
    private FirebaseAuth mAuth;
    private String TAG = "zoka";
    DatabaseReference mUserDbRef;
    DatabaseReference mNoteDbRef;
    private ValueEventListener valueEventListener;


    public NoteRepository(Application application) {
        NoteDataBase noteDataBase = NoteDataBase.getInstance(application);
        mNoteDao = noteDataBase.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
        mAuth = FirebaseAuth.getInstance();
        mUserDbRef = FirebaseDatabase.getInstance().getReference("Users");
        if (null != FirebaseAuth.getInstance().getCurrentUser()) {
            mNoteDbRef = mUserDbRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Notes");
            getFirebaseNotes();
        }


    }

    public NoteRepository() {
        mAuth = FirebaseAuth.getInstance();
        mUserDbRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void insert(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                long id = mNoteDao.insert(note);
                note.setId((int) id);
                mNoteDbRef.child(String.valueOf(id)).setValue(note);
                return null;
            }
        }.execute();
    }

    public void insert(final List<Note> note) {
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
                mNoteDbRef.child("" + note.getId()).setValue(note);
                return null;
            }
        }.execute();
    }

    public void delete(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mNoteDao.delete(note);
                mNoteDbRef.child("" + note.getId()).removeValue();
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
                            registerState.postSuccess(true);
                            String uid = mAuth.getCurrentUser().getUid();
                            mUserDbRef.child(uid).child("UserInfo ").setValue(new UserModel(email, displayName));


                        } else {

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

                            loginState.postSuccess(true);


                        } else {
                            // If sign in fails, display a message to the user.
                            loginState.postError(task.getException());
                        }

                        // ...
                    }
                });
        return loginState;
    }


    private void getFirebaseNotes() {
        valueEventListener = mNoteDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Note> allNotes = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Note note = postSnapshot.getValue(Note.class);
                    allNotes.add(note);
                }
                syncNotes(allNotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void syncNotes(List<Note> remote) {
        try {
            List<Note> local = getAllNotes().getValue();
            if (equalLists(local, remote)) {

                return;
            }


            List<Note> allNotes = new ArrayList<>(local);

            for (int i = 0; i < local.size(); i++) {
                for (int j = 0; j < remote.size(); j++) {

                    if (local.get(i).getId() == remote.get(j).getId()) {
                        allNotes.remove(local.get(i));
                    }
                }
            }
            allNotes.addAll(remote);

            updateFirebaseAndLocalDataBase(allNotes);
        } catch (NullPointerException e) {
            Log.i(TAG, "syncNotes: " + e.getMessage());

        }
    }

    private void updateFirebaseAndLocalDataBase(List<Note> allNotes) {
        mNoteDbRef.removeEventListener(valueEventListener);
        if (getAllNotes().getValue().size() == 0) {
            insert(allNotes);
            return;
        }
        //update Room db
        deleteAllNotes();
        insert(allNotes);

        mNoteDbRef.removeValue();

        for (Note note : allNotes) {
            mNoteDbRef.child(String.valueOf(note.getId())).setValue(note);
        }
    }

    public boolean equalLists(List<Note> a, List<Note> b) {
        boolean isEqual = true;
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (a.get(i).getId() == b.get(j).getId()) {
                    isEqual = true;
                    continue;
                } else {
                    isEqual = false;
                }
            }
        }
        return isEqual;
    }
}