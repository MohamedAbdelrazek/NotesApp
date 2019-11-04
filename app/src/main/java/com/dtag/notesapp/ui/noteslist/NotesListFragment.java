package com.dtag.notesapp.ui.noteslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.notesapp.R;
import com.dtag.notesapp.adapters.notesadapter.NotesAdapter;
import com.dtag.notesapp.adapters.notesadapter.OnItemClicked;
import com.dtag.notesapp.databinding.NotesListFragmentBinding;
import com.dtag.notesapp.persistence.Note;

import java.util.List;

public class NotesListFragment extends Fragment implements View.OnClickListener, OnItemClicked {

    private NotesListViewModel mViewModel;
    private NotesListFragmentBinding mNotesListFragmentBinding;
    private NavController mNavController;
    private NotesAdapter mNotesAdapter;


    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mNotesListFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.notes_list_fragment, container, false);
        View rootView = mNotesListFragmentBinding.getRoot();
        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        mNotesListFragmentBinding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotesListFragmentBinding.notesRecyclerView.setHasFixedSize(true);
        mNotesAdapter = new NotesAdapter(this);
        mNotesListFragmentBinding.notesRecyclerView.setAdapter(mNotesAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = mNotesAdapter.getNoteAtPosition(viewHolder.getAdapterPosition());
                mViewModel.delete(note);
                Toast.makeText(getContext(), "Note Deleted Successfully!", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView( mNotesListFragmentBinding.notesRecyclerView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        mNotesListFragmentBinding.noteFloatingActionBtn.setOnClickListener(this);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NotesListViewModel.class);
        mViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mNotesAdapter.updateNotes(notes);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.note_floating_action_btn) {
            mNavController.navigate(R.id.action_notesListFragment_to_addNoteFragment);
        }
    }

    @Override
    public void onClick(Note note) {

        Toast.makeText(getContext(), "" + note.getId(), Toast.LENGTH_SHORT).show();

        mNavController.navigate(NotesListFragmentDirections.actionNotesListFragmentToUpdateNoteFragment(note));

    }
}
