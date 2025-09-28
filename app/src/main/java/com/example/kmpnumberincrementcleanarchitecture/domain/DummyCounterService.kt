package com.example.kmpnumberincrementcleanarchitecture.domain

interface DummyCounterService {
    suspend fun increment(current: Int): Result<Int>
    suspend fun reset(): Int
}