package com.example.shared.domain

import com.example.shared.domain.DummyCounterService

interface CounterRepository {
   suspend fun increment(current: Int): Result<Int>
   suspend fun reset(): Int
}