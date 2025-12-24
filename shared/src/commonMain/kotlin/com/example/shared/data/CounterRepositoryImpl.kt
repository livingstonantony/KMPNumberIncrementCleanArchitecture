package com.example.shared.data

import com.example.shared.domain.CounterRepository
import com.example.shared.domain.DummyCounterService

class CounterRepositoryImpl(
    private val service: DummyCounterService
) : CounterRepository {
    override suspend fun increment(current: Int): Result<Int> = service.increment(current)
    override suspend fun reset(): Int = service.reset()

}