package com.dtag.notesapp.adapters.notesadapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.notesapp.databinding.NotesSingleItemsBinding;
import com.dtag.notesapp.persistence.Note;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    NotesSingleItemsBinding mNotesSingleItemsBinding;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        // ButterKnife.bind(this, itemView);

        mNotesSingleItemsBinding = DataBindingUtil.bind(itemView);
    }

    public void bind(Note note) {
        mNotesSingleItemsBinding.notesSingleItemTitleTv.setText(note.getTitle());
        mNotesSingleItemsBinding.notesSingleItemDescTv.setText(note.getDescription());
        mNotesSingleItemsBinding.notesSingleItemPriorityTv.setText("" + note.getPriority());
    }
}
