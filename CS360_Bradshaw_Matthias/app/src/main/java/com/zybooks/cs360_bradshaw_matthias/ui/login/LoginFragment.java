package com.zybooks.cs360_bradshaw_matthias.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.cs360_bradshaw_matthias.data.LoginDataSource;
import com.zybooks.cs360_bradshaw_matthias.data.Result;
import com.zybooks.cs360_bradshaw_matthias.databinding.FragmentLoginBinding;

import com.zybooks.cs360_bradshaw_matthias.R;


/* LoginFragment
 *
 * UI screen for user login and account creation.
 *
 *   collects username/password input
 *   shows validation errors
 *   calls LoginViewModel.login()
 *   navigates to the main inventory screen on success
 */
public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(requireContext()))
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.Username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        final Button newUserButton = binding.btnNewUser;
        final Button createUserButton = binding.btnCreateUser;

        // on click listener for new user button
        newUserButton.setOnClickListener(v -> {

                    // make confirm password text and create user button visible and hide login and create user buttons
                    binding.confirmPassword.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    createUserButton.setVisibility(View.VISIBLE);
                    newUserButton.setVisibility(View.GONE);
        });

        // on click listener for navigation to main page fragment from create user button
        createUserButton.setOnClickListener(v -> {

            // declares variables for user input
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();

            // checks if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // creates user in database
            LoginDataSource dataSource = new LoginDataSource(requireContext());
            Result<Boolean> result = dataSource.createUser(username, password);

            if (result instanceof Result.Success) {
                Toast.makeText(getContext(), "User created", Toast.LENGTH_SHORT).show();

                binding.confirmPassword.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                createUserButton.setVisibility(View.GONE);
                newUserButton.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Error creating user", Toast.LENGTH_SHORT).show();
            }
        });

        // observe validation to enable or disable login button
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        // observe login result and navigate if successful
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    Toast.makeText(getContext(), "Login failed -- Incorrect username or password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    // navigate to main page when login is successful
                    androidx.navigation.Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainPageFragment);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        // on click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName() + " !";
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}