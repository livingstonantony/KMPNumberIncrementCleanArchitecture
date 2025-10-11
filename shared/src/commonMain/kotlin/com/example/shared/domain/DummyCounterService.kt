package com.example.shared.domain

interface DummyCounterService {
    suspend fun increment(current: Int): Result<Int>
    suspend fun reset(): Int
}