package dev.tmapps.konnection

import app.cash.turbine.test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CoroutinesTests {

    @Test
    fun `test MutableStateFlow of TriggerEvent`() = runTest {
        val myStateFlow = MutableStateFlow(TriggerEvent)
        var emissions: Int = 1 // already start with one emission

        myStateFlow.test {
            myStateFlow.emit(TriggerEvent)
            assertNotEquals(TriggerEvent, awaitItem())
            emissions += 1
            myStateFlow.emit(TriggerEvent)
            assertNotEquals(TriggerEvent, awaitItem())
            emissions += 1
            cancelAndConsumeRemainingEvents()
        }

        assertEquals(3, emissions)
    }
}
