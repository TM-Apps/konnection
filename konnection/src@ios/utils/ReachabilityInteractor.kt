package dev.tmapps.konnection.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.SystemConfiguration.SCNetworkReachabilityFlags
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.SCNetworkReachabilityRef

@OptIn(ExperimentalForeignApi::class)
interface ReachabilityInteractor {
    fun getReachabilityFlags(
        reachabilityRef: SCNetworkReachabilityRef
    ): SCNetworkReachabilityFlags?
}

@OptIn(ExperimentalForeignApi::class)
internal open class ReachabilityInteractorImpl : ReachabilityInteractor {

    override fun getReachabilityFlags(
        reachabilityRef: SCNetworkReachabilityRef
    ): SCNetworkReachabilityFlags? = memScoped {
        // val flags = allocArray<SCNetworkReachabilityFlagsVar>(10) -> not working
        // val flags = allocArrayOf<SCNetworkReachabilityFlagsVar>() -> not working
        val flags = alloc<SCNetworkReachabilityFlagsVar>()
        return if (SCNetworkReachabilityGetFlags(reachabilityRef, flags.ptr)) flags.value else null
    }
}
