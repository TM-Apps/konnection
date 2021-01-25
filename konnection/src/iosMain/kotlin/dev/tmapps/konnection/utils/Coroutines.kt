package dev.tmapps.konnection.utils

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_t

class CoroutineDispatcherBuilder(
    private val dispatchQueue: dispatch_queue_t
) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue) {
            block.run()
        }
    }
}

val MainDispatcher: CoroutineDispatcher =
    CoroutineDispatcherBuilder(dispatch_get_main_queue())

internal class MainScope: CoroutineScope {
    private val dispatcher = MainDispatcher
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job
}
