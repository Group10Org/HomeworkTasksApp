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

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels {
        val database = AppDatabase.get(requireContext())
        val repository = UserRepository(database.userEntryDao())
        RegisterViewModelFactory(repository)
    }

    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonRegister: MaterialButton
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextUsername = view.findViewById(R.id.editTextUsername)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonRegister = view.findViewById(R.id.buttonRegister)
        progressBar = view.findViewById(R.id.progressRegister)

        setupClickListeners()
        observeRegisterState()
    }

    private fun setupClickListeners() {
        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString()
            viewModel.register(username, password)
        }
    }

    private fun observeRegisterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect { state ->
                    when (state) {
                        is RegisterState.Idle -> {
                            progressBar.visibility = View.GONE
                            buttonRegister.isEnabled = true
                        }
                        is RegisterState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            buttonRegister.isEnabled = false
                        }
                        is RegisterState.Success -> {
                            progressBar.visibility = View.GONE
                            buttonRegister.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                "Account created! Welcome, ${state.user.username}!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Save user session
                            saveUserSession(state.user.id)
                            // Navigate to main screen
                            navigateToMainScreen()
                        }
                        is RegisterState.Error -> {
                            progressBar.visibility = View.GONE
                            buttonRegister.isEnabled = true
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
        // TODO: Navigation
        Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
    }
}