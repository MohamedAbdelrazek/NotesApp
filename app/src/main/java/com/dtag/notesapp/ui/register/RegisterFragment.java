package com.dtag.notesapp.ui.register;


import android.app.ProgressDialog;
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

import com.dtag.notesapp.R;
import com.dtag.notesapp.databinding.RegisterFragmentBinding;
import com.dtag.notesapp.livedatawrapper.NoteState;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private RegisterViewModel mViewModel;
    private String TAG = "zoka";
    private NavController mNavController;
    private RegisterFragmentBinding mRegisterFragmentBinding;
    private ProgressDialog dialog;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mRegisterFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false);
        View rootView = mRegisterFragmentBinding.getRoot();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Creating your account ...");
        dialog.setCancelable(false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        mNavController = Navigation.findNavController(view);
        mRegisterFragmentBinding.registerButton.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

    }

    @Override
    public void onClick(View v) {

        String userName = mRegisterFragmentBinding.email.getText().toString().trim();
        String password = mRegisterFragmentBinding.password.getText().toString();
        String displayName = mRegisterFragmentBinding.displayName.getText().toString().trim();

        if (validate(userName, password, displayName)) {
            dialog.show();
            mViewModel.registerUser(userName, password, displayName);
            mViewModel.registerState.observe(this, new Observer<NoteState<Boolean>>() {
                @Override
                public void onChanged(NoteState<Boolean> booleanNoteState) {
                    switch (booleanNoteState.getStatus()) {
                        case SUCCESS:
                            Toast.makeText(getContext(), "Welcome !", Toast.LENGTH_SHORT).show();
                            mNavController.popBackStack(R.id.notesListFragment, true);
                            //navController.navigate(R.id.fragment_company);
                            mNavController.navigate(RegisterFragmentDirections.actionRegisterFragmentToNotesListFragment());
                            dialog.dismiss();
                            // nav to list fragment.....

                            break;
                        case ERROR:
                            Throwable e = booleanNoteState.getError();
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
    }

    private boolean validate(String email, String password, String displayName) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mRegisterFragmentBinding.email.setError("Enter a valid Email Address !");
            valid = false;
        } else {
            mRegisterFragmentBinding.email.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            mRegisterFragmentBinding.password.setError("password too short");
            valid = false;
        } else {
            mRegisterFragmentBinding.password.setError(null);
        }
        if (displayName.isEmpty()) {
            mRegisterFragmentBinding.displayName.setError("Pls enter your name !");
            valid = false;
        } else {
            mRegisterFragmentBinding.displayName.setError(null);
        }
        return valid;
    }
}
