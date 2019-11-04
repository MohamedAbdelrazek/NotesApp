package com.dtag.notesapp.ui.updateNote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dtag.notesapp.R;
import com.dtag.notesapp.databinding.UpdateNoteFragmentBinding;
import com.dtag.notesapp.persistence.Note;

public class UpdateNoteFragment extends Fragment implements View.OnClickListener {

    private UpdateNoteViewModel mViewModel;
    private UpdateNoteFragmentBinding mUpdateNoteFragmentBinding;
    private int noteID;

    public static UpdateNoteFragment newInstance() {
        return new UpdateNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mUpdateNoteFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.update_note_fragment, container, false);
        View rootView = mUpdateNoteFragmentBinding.getRoot();
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Note note = UpdateNoteFragmentArgs.fromBundle(getArguments()).getNoteArgument();
        mUpdateNoteFragmentBinding.setNote(note);
         noteID=note.getId();
        Toast.makeText(getContext(), "id "+note.getId(), Toast.LENGTH_SHORT).show();
        mUpdateNoteFragmentBinding.updateButton.setOnClickListener(this);
        mUpdateNoteFragmentBinding.cancelButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UpdateNoteViewModel.class);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.update_button) {
            updateNote();

        } else if (v.getId() == R.id.cancel_button) {
            getActivity().onBackPressed();
        }
    }

    private void updateNote() {

        String title = mUpdateNoteFragmentBinding.noteTitleEditText.getText().toString().trim();
        String desc = mUpdateNoteFragmentBinding.noteDescEditText.getText().toString().trim();
        int priority = mUpdateNoteFragmentBinding.noteNumberPicker.getValue();

        if (validate(title, desc)) {
            mViewModel.update(new Note(noteID,title, desc, priority));
            Toast.makeText(getContext(), "Note Added Successfully!", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
    }


    private boolean validate(String title, String desc) {
        boolean valid = true;

        if (title.isEmpty()) {
            mUpdateNoteFragmentBinding.noteTitleEditText.setError("title can't be empty !");
            valid = false;
        } else {
            mUpdateNoteFragmentBinding.noteTitleEditText.setError(null);
        }
        if (desc.isEmpty()) {
            mUpdateNoteFragmentBinding.noteDescEditText.setError("desc can't be empty !");
            valid = false;
        } else {
            mUpdateNoteFragmentBinding.noteDescEditText.setError(null);
        }
        return valid;
    }
}
