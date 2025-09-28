package com.example.kmpnumberincrementcleanarchitecture.data

import com.example.kmpnumberincrementcleanarchitecture.domain.DummyCounterService
import kotlinx.coroutines.delay

class DummyCounterServiceImpl : DummyCounterService {
    private val max = 10

    override suspend fun increment(current: Int): Result<Int> {
        delay(500) // simulate a network/db delay
        return if (current < max) {
            Result.success(current + 1)
        } else {
            Result.failure(IllegalStateException("Maximum count reached"))
        }
    }

    override suspend fun reset(): Int {
        delay(500) // simulate a shorter reset delay
        return 0
    }
}