package com.example.kmpnumberincrementcleanarchitecture.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kmpnumberincrementcleanarchitecture.data.CounterRepository
import com.example.kmpnumberincrementcleanarchitecture.data.DummyCounterServiceImpl
import com.example.kmpnumberincrementcleanarchitecture.domain.DummyCounterService
import com.example.kmpnumberincrementcleanarchitecture.viewmodel.CounterViewModel

class CounterViewModelFactory(
    private val repository: CounterRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CounterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

object AppModule {
    private val service: DummyCounterService by lazy { DummyCounterServiceImpl() }
    private val repository: CounterRepository by lazy { CounterRepository(service) }

    fun provideCounterViewModelFactory(): CounterViewModelFactory {
        return CounterViewModelFactory(repository)
    }
}