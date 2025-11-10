package com.example.hwtasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hwtasks.data.UserEntry
import com.example.hwtasks.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: UserEntry) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading

                // Validation
                if (username.isBlank()) {
                    _registerState.value = RegisterState.Error("Username cannot be empty")
                    return@launch
                }

                if (username.length < 3) {
                    _registerState.value = RegisterState.Error("Username must be at least 3 characters")
                    return@launch
                }

                if (password.isBlank()) {
                    _registerState.value = RegisterState.Error("Password cannot be empty")
                    return@launch
                }

                if (password.length < 4) {
                    _registerState.value = RegisterState.Error("Password must be at least 4 characters")
                    return@launch
                }

                // Check if username already exists
                val users = userRepository.entries().first()
                if (users.any { it.username == username }) {
                    _registerState.value = RegisterState.Error("Username already exists")
                    return@launch
                }

                // Create new user
                val newUser = UserEntry(
                    dateCreated = LocalDate.now(),
                    username = username,
                    password = password,
                    numOfCompletedTasks = 0,
                    privacySetting = false
                )

                val userId = userRepository.upsert(newUser)
                _registerState.value = RegisterState.Success(newUser.copy(id = userId))
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}

class RegisterViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}