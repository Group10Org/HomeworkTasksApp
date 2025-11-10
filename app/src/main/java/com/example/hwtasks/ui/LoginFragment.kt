package com.example.hwtasks.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hwtasks.R
import com.example.hwtasks.data.AppDatabase
import com.example.hwtasks.data.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels {
        val database = AppDatabase.get(requireContext())
        val repository = UserRepository(database.userEntryDao())
        LoginViewModelFactory(repository)
    }

    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: MaterialButton
    private lateinit var textViewRegister: View
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextUsername = view.findViewById(R.id.editTextUsername)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonLogin = view.findViewById(R.id.buttonLogin)
        textViewRegister = view.findViewById(R.id.textViewRegister)
        progressBar = view.findViewById(R.id.progressBar)

        setupClickListeners()
        observeLoginState()
    }

    private fun setupClickListeners() {
        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString()
            viewModel.login(username, password)
        }

        textViewRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    when (state) {
                        is LoginState.Idle -> {
                            progressBar.visibility = View.GONE
                            buttonLogin.isEnabled = true
                        }
                        is LoginState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            buttonLogin.isEnabled = false
                        }
                        is LoginState.Success -> {
                            progressBar.visibility = View.GONE
                            buttonLogin.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                "Welcome back, ${state.user.username}!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Save user session
                            saveUserSession(state.user.id)
                            // Navigate to main screen
                            navigateToMainScreen()
                        }
                        is LoginState.Error -> {
                            progressBar.visibility = View.GONE
                            buttonLogin.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserSession(userId: Long) {
        val sharedPref = requireActivity().getSharedPreferences(
            "HwTasksPrefs",
            Context.MODE_PRIVATE
        )
        sharedPref.edit().apply {
            putLong("current_user_id", userId)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    private fun navigateToMainScreen() {
        // TODO: Navigate to your main task screen
        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
    }
}