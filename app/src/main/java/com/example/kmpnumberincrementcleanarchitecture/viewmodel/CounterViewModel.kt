package com.example.kmpnumberincrementcleanarchitecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kmpnumberincrementcleanarchitecture.data.CounterRepository
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CounterUiState {
    data class Success(val count: Int) : CounterUiState()
    object Loading : CounterUiState()
    data class Error(val count: Int, val message: String, val canReset: Boolean) : CounterUiState()
}
class CounterViewModel(
    private val repository: CounterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CounterUiState>(CounterUiState.Success(0))
    val uiState: StateFlow<CounterUiState> = _uiState

    private var currentCount = 0

    fun increment() {
        viewModelScope.launch {
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
    }

    fun reset() {
        viewModelScope.launch {
            _uiState.value = CounterUiState.Loading
            currentCount = repository.reset()
            _uiState.value = CounterUiState.Success(currentCount)
        }
    }
}

