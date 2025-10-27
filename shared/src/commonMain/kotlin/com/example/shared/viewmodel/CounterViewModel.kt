package com.example.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.CounterRepository

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CounterUiState {
    data class Success(val count: Int) : CounterUiState()
    object Loading : CounterUiState()
    data class Error(val count: Int, val message: String, val canReset: Boolean) : CounterUiState()
}

sealed class CounterIntent {
    object Increment : CounterIntent()
    object Reset : CounterIntent()

}

class CounterViewModel(
    private val repository: CounterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CounterUiState>(CounterUiState.Success(0))
    val uiState: StateFlow<CounterUiState> = _uiState

    private var currentCount = 0


    fun process(counterIntent: CounterIntent) {
        viewModelScope.launch {
            when (counterIntent) {
                CounterIntent.Increment -> increment()
                CounterIntent.Reset -> reset()
            }
        }
    }

    suspend fun increment() {
        _uiState.value = CounterUiState.Loading
        val result = repository.increment(currentCount)
        _uiState.value = result.fold(
            onSuccess = {
                currentCount = it
                CounterUiState.Success(it)
            },
            onFailure = {
                CounterUiState.Error(currentCount, it.message ?: "Unknown error", canReset = true)
            }
        )
    }

    suspend fun reset() {
        _uiState.value = CounterUiState.Loading
        currentCount = repository.reset()
        _uiState.value = CounterUiState.Success(currentCount)
    }
}

