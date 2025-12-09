package com.example.hwtasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StreamViewModelFactory(private val apiKey: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StreamViewModel(apiKey) as T
    }
}