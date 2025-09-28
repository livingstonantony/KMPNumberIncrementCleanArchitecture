package com.example.kmpnumberincrementcleanarchitecture.di

import com.example.kmpnumberincrementcleanarchitecture.viewmodel.CounterViewModelFactory

object AppModule {

    fun provideCounterViewModelFactory(): CounterViewModelFactory {
        return CounterViewModelFactory()
    }
}
