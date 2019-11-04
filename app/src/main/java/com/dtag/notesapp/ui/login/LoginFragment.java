
package com.dtag.notesapp.ui.login;

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

import com.dtag.notesapp.livedatawrapper.NoteState;
import com.dtag.notesapp.R;
import com.dtag.notesapp.databinding.LoginFragmentBinding;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private String TAG = "zoka";
    private NavController mNavController;
    private LoginViewModel mViewModel;
    private LoginFragmentBinding mLoginFragmentBinding;
    private ProgressDialog dialog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mLoginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        View rootView = mLoginFragmentBinding.getRoot();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mNavController = Navigation.findNavController(view);

        mLoginFragmentBinding.loginLaterButton.setOnClickListener(this);
        mLoginFragmentBinding.registerTextView.setOnClickListener(this);
        mLoginFragmentBinding.loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_text_view) {
            mNavController.navigate(R.id.action_loginFragment_to_registerFragment);
        } else if (v.getId() == R.id.login_later_button) {
            mNavController.navigate(R.id.action_loginFragment_to_notesListFragment);
        } else if (v.getId() == R.id.login_button) {


            String userName = mLoginFragmentBinding.email.getText().toString().trim();
            String password = mLoginFragmentBinding.password.getText().toString();

            if (validate(userName, password)) {
                mViewModel.login(userName, password);
                dialog.show();
                mViewModel.islogedIn.observe(this, new Observer<NoteState<Boolean>>() {
                    @Override
                    public void onChanged(NoteState<Boolean> booleanStateData) {
                        switch (booleanStateData.getStatus()) {
                            case SUCCESS:
                                //
                                // nav to list fragment.....
                                dialog.dismiss();
                                Toast.makeText(getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                                mNavController.navigate(LoginFragmentDirections.actionLoginFragmentToNotesListFragment());

                                break;
                            case ERROR:
                                Throwable e = booleanStateData.getError();
                                //   Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
            }
        }
    }

    private boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mLoginFragmentBinding.email.setError("Enter a valid Email Address !");
            valid = false;
        } else {
            mLoginFragmentBinding.email.setError(null);
        }
        if (password.isEmpty() || password.length() < 8) {
            mLoginFragmentBinding.password.setError("password too short");
            valid = false;
        } else {
            mLoginFragmentBinding.password.setError(null);
        }
        return valid;
    }
}