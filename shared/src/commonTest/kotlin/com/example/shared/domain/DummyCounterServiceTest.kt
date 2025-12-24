package com.example.shared.domain

import com.example.shared.data.DummyCounterServiceImpl
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DummyCounterServiceTest {

    val service: DummyCounterService = DummyCounterServiceImpl()

    @Test
    fun testIncrementSuccess() = runBlocking {
        val result = service.increment(5)
        assertTrue(result.isSuccess)
        assertEquals(6, result.getOrNull())
    }

    @Test
    fun testIncrementFailure() = runBlocking {
        val result = service.increment(10)
        assertTrue(result.isFailure)
    }

    @Test
    fun testResetRunTest() = runBlocking {
        val result = service.reset()
        assertEquals(0, result)
    }
}