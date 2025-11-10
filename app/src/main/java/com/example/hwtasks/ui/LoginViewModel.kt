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

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserEntry) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // Validation
                if (username.isBlank()) {
                    _loginState.value = LoginState.Error("Username cannot be empty")
                    return@launch
                }
                if (password.isBlank()) {
                    _loginState.value = LoginState.Error("Password cannot be empty")
                    return@launch
                }

                // Get all users and check credentials
                val users = userRepository.entries().first()
                val user = users.find {
                    it.username == username && it.password == password
                }

                if (user != null) {
                    _loginState.value = LoginState.Success(user)
                } else {
                    _loginState.value = LoginState.Error("Invalid username or password")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // Validation
                if (username.isBlank()) {
                    _loginState.value = LoginState.Error("Username cannot be empty")
                    return@launch
                }
                if (password.length < 4) {
                    _loginState.value = LoginState.Error("Password must be at least 4 characters")
                    return@launch
                }

                // Check if username already exists
                val users = userRepository.entries().first()
                if (users.any { it.username == username }) {
                    _loginState.value = LoginState.Error("Username already exists")
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
                _loginState.value = LoginState.Success(newUser.copy(id = userId))
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

class LoginViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}