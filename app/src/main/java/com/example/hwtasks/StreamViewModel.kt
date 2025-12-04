package com.example.hwtasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StreamViewModel(private val apiKey: String) : ViewModel() {

    private val repo = ShortsRepository(apiKey)

    val shorts = MutableStateFlow<List<ShortItem>>(emptyList())
    var isLoading = false

    fun loadMore() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            val newShorts = repo.loadShorts()

            shorts.value = shorts.value + newShorts

            isLoading = false
        }
    }
}