package com.example.kmpnumberincrementcleanarchitecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CounterUiState {
    object Loading : CounterUiState()
    data class Success(val count: Int, val canReset: Boolean) : CounterUiState()
    data class Error(val message: String, val canReset: Boolean, val count: Int) : CounterUiState()
}

class CounterViewModel : ViewModel() {
    private val _uiState =
        MutableStateFlow<CounterUiState>(CounterUiState.Success(count = 0, canReset = false))
    val uiState: StateFlow<CounterUiState> = _uiState

    private var count = 0
    private val maxCount = 10

    fun increment() {
        if (count >= maxCount) {
            _uiState.value = CounterUiState.Error(
                "Maximum count reached. Reset to continue.",
                canReset = true,
                count = count
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = CounterUiState.Loading

            delay(500) // simulate async work
            count++

            if (count >= maxCount) {
                _uiState.value = CounterUiState.Error(
                    "Reached maximum count: $maxCount",
                    canReset = true,
                    count = count
                )
            } else {
                _uiState.value = CounterUiState.Success(count = count, canReset = count >= maxCount)
            }
        }
    }

    fun reset() {
        count = 0
        _uiState.value = CounterUiState.Success(count = count, canReset = false)
    }
}


class CounterViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

