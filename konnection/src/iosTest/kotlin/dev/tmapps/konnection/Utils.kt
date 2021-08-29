package dev.tmapps.konnection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun runTest(skip: Boolean, block: suspend CoroutineScope.() -> Unit) =
    runBlocking {
        if (skip) {
            println("Skip the test!")
            return@runBlocking
        }
        block()
    }
