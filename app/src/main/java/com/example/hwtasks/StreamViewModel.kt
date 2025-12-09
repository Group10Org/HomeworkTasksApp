package com.example.hwtasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StreamViewModel(private val apiKey: String) : ViewModel() {

    private val repo = ShortsRepository(apiKey)
    val shorts = MutableStateFlow<List<ShortItem>>(emptyList())
    var isLoading = false

    // Hardcoded class names
    private val classNames = listOf("datastructures", "android app development", "discrete math")

    fun loadShortsForClasses() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            val newShorts = repo.loadShortsForClasses(classNames)
            shorts.value = shorts.value + newShorts
            isLoading = false
        }
    }
}
