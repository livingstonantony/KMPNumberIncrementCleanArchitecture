package com.example.shared.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shared.data.CounterRepository
import com.example.shared.data.DummyCounterServiceImpl
import com.example.shared.domain.DummyCounterService
import com.example.shared.viewmodel.CounterViewModel


private val service: DummyCounterService by lazy { DummyCounterServiceImpl() }
private val repository: CounterRepository by lazy { CounterRepository(service) }


val mainViewModelFactory = viewModelFactory {
    initializer {
        CounterViewModel(repository = repository)
    }
}



