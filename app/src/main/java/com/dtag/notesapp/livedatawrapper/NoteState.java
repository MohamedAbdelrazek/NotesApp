package com.dtag.notesapp.livedatawrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//T boolean
public class NoteState<T> {
    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    @Nullable
    private Throwable error;


    public NoteState() {
        this.status = DataStatus.CREATED;
        this.data = null;
        this.error = null;
    }


    public NoteState<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.error = null;
        return this;
    }

    public NoteState<T> error(@NonNull Throwable error) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    @NonNull
    public DataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public enum DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
    }

}
