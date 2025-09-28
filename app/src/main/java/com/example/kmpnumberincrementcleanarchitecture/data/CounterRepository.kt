package com.example.kmpnumberincrementcleanarchitecture.data

import com.example.kmpnumberincrementcleanarchitecture.domain.DummyCounterService

class CounterRepository(
    private val service: DummyCounterService
) {
    suspend fun increment(current: Int): Result<Int> = service.increment(current)
    suspend fun reset(): Int = service.reset()
}