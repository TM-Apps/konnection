package dev.tmapps.konnection

import kotlinx.coroutines.CoroutineScope

expect fun runTest(skip: Boolean = false, block: suspend CoroutineScope.() -> Unit)
