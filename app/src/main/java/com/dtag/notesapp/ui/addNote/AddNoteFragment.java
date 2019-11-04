package com.dtag.notesapp.ui.addNote;

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
import com.dtag.notesapp.databinding.FragmentAddNoteBinding;
import com.dtag.notesapp.persistence.Note;

public class AddNoteFragment extends Fragment implements View.OnClickListener {

    private AddNoteViewModel mViewModel;
    private FragmentAddNoteBinding mAddNoteBinding;


    public static AddNoteFragment newInstance() {
        return new AddNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mAddNoteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false);
        View rootView = mAddNoteBinding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddNoteBinding.saveButton.setOnClickListener(this);
        mAddNoteBinding.cancelButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save_button:
                saveNote();
                break;
            case R.id.cancel_button:
                getActivity().onBackPressed();
                break;


        }
    }

    private void saveNote() {

        String title = mAddNoteBinding.noteTitleEditText.getText().toString().trim();
        String desc = mAddNoteBinding.noteDescEditText.getText().toString().trim();
        int priority = mAddNoteBinding.noteNumberPicker.getValue();

        if (validate(title, desc)) {


            mViewModel.insert(new Note(title, desc, priority));
            Toast.makeText(getContext(), "Note Added Successfully!", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }


    }

    private boolean validate(String title, String desc) {
        boolean valid = true;

        if (title.isEmpty()) {
            mAddNoteBinding.noteTitleEditText.setError("title can't be empty !");
            valid = false;
        } else {
            mAddNoteBinding.noteTitleEditText.setError(null);
        }
        if (desc.isEmpty()) {
            mAddNoteBinding.noteDescEditText.setError("desc can't be empty !");
            valid = false;
        } else {
            mAddNoteBinding.noteDescEditText.setError(null);
        }
        return valid;
    }
}
