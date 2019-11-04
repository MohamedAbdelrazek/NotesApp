package com.dtag.notesapp.livedatawrapper;

import androidx.lifecycle.MutableLiveData;

public class NoteStateLiveData<T> extends MutableLiveData<NoteState<T>> {


    /**
     * Use this to put the Data on a ERROR DataStatus
     *
     * @param throwable the error to be handled
     */
    public void postError(Throwable throwable) {
        postValue(new NoteState<T>().error(throwable));
    }

    /**
     * Use this to put the Data on a SUCCESS DataStatus
     *
     * @param data
     */
    public void postSuccess(T data) {
        postValue(new NoteState<T>().success(data));
    }


}
