package com.example.shared.data

import com.example.shared.domain.DummyCounterService

class CounterRepository(
    private val service: DummyCounterService
) {
    suspend fun increment(current: Int): Result<Int> = service.increment(current)
    suspend fun reset(): Int = service.reset()
}