package dev.tmapps.konnection.utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.darwin.getifaddrs
import platform.darwin.ifaddrs
import platform.posix.NI_MAXHOST
import platform.posix.NI_NUMERICHOST
import platform.posix.getnameinfo
import platform.posix.sockaddr

internal interface IfaddrsInteractor {
    fun get(netInterfaces: Set<String>, saFamily: Int): String?
}

@OptIn(ExperimentalForeignApi::class)
internal class IfaddrsInteractorImpl(
    private val logger: (String, Throwable?) -> Unit
) : IfaddrsInteractor {

    private fun debugLog(message: String, error: Throwable? = null) = ::logger

    override fun get(netInterfaces: Set<String>, saFamily: Int): String? = memScoped {
        val ifaddr = alloc<ifaddrs>()
        if (getifaddrs(ifaddr.ptr.reinterpret()) == 0) {
            var addr = ifaddr.ifa_next?.reinterpret<ifaddrs>()

            while (addr != null) {
                val socketAddr = addr.pointed.ifa_addr?.reinterpret<sockaddr>()
                val currentSaFamily = socketAddr?.pointed?.sa_family

                if (currentSaFamily?.toInt() == saFamily) {
                    val ifaName = addr.pointed.ifa_name?.reinterpret<ByteVar>()?.toKString() ?: return@memScoped null
                    if (netInterfaces.contains(ifaName)) {
                        val saLen = socketAddr.pointed.sa_len
                        val hostname = allocArray<ByteVar>(length = NI_MAXHOST)
                        getnameinfo(socketAddr, saLen.toUInt(), hostname, NI_MAXHOST.toUInt(), null, 0u, NI_NUMERICHOST)
                        debugLog("hostname = ${hostname.pointed.value} | ipValue = ${hostname.toKString()}")
                        return@memScoped hostname.toKString()
                    }
                }

                addr = addr.pointed.ifa_next?.reinterpret<ifaddrs>()
            }
        }
        return null
    }
}
