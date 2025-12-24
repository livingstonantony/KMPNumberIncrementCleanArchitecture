package com.example.shared.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shared.data.CounterRepositoryImpl
import com.example.shared.data.DummyCounterServiceImpl
import com.example.shared.domain.CounterRepository
import com.example.shared.domain.DummyCounterService
import com.example.shared.viewmodel.CounterViewModel


class AppContainer{
    private val service: DummyCounterService by lazy { DummyCounterServiceImpl() }
    private val repository: CounterRepository by lazy { CounterRepositoryImpl(service) }


    val mainViewModelFactory = viewModelFactory {
        initializer {
            CounterViewModel(repository = repository)
        }
    }

}



