package dev.tmapps.konnection

import io.mockk.every
import io.mockk.mockk
import java.net.InetAddress
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.util.Enumeration

/**
 * Extension function for converting a {@link List} to an {@link Enumeration}
 */
fun <T> List<T>.toEnumeration(): Enumeration<T> =
    object : Enumeration<T> {
        var count = 0

        override fun hasMoreElements(): Boolean = this.count < size

        override fun nextElement(): T {
            if (this.count < size) return get(this.count++)
            throw NoSuchElementException("List enumeration asked for more elements than present")
        }
    }

fun mockNetworkInterface(
    index: Int,
    name: String,
    displayName: String = "",
    inetAddresses: List<InetAddress> = emptyList(),
    interfaceAddresses: List<InterfaceAddress> = emptyList(),
    parent: NetworkInterface? = null,
    isUp: Boolean = false,
    isLoopback: Boolean = false,
    isPointToPoint: Boolean = false,
    supportsMulticast: Boolean = false,
    hardwareAddress: ByteArray = ByteArray(size = 0),
    mtu: Int = 0,
    isVirtual: Boolean = false
): NetworkInterface = mockk<NetworkInterface>().apply {
    every { getName() } returns name
    every { getInetAddresses() } answers { inetAddresses.toEnumeration() }
    every { getInterfaceAddresses() } returns interfaceAddresses
    every { getParent() } returns parent
    every { getIndex() } returns index
    every { getDisplayName() } returns displayName
    every { isUp() } returns isUp
    every { isLoopback() } returns isLoopback
    every { isPointToPoint() } returns isPointToPoint
    every { supportsMulticast() } returns supportsMulticast
    every { getHardwareAddress() } returns hardwareAddress
    every { getMTU() } returns mtu
    every { isVirtual() } returns isVirtual
}

fun mockInet4Address(
    address: ByteArray = ByteArray(size = 0),
    hostAddress: String = "",
    isMulticastAddress: Boolean = false,
    isAnyLocalAddress: Boolean = false,
    isLoopbackAddress: Boolean = false,
    isLinkLocalAddress: Boolean = false,
    isSiteLocalAddress: Boolean = false,
    isMCGlobal: Boolean = false,
    isMCNodeLocal: Boolean = false,
    isMCLinkLocal: Boolean = false,
    isMCSiteLocal: Boolean = false,
    isMCOrgLocal: Boolean = false
): Inet4Address = mockk<Inet4Address>().apply {
    every { isMulticastAddress() } returns isMulticastAddress
    every { isAnyLocalAddress() } returns isAnyLocalAddress
    every { isLoopbackAddress() } returns isLoopbackAddress
    every { isLinkLocalAddress() } returns isLinkLocalAddress
    every { isSiteLocalAddress() } returns isSiteLocalAddress
    every { isMCGlobal() } returns isMCGlobal
    every { isMCNodeLocal() } returns isMCNodeLocal
    every { isMCLinkLocal() } returns isMCLinkLocal
    every { isMCSiteLocal() } returns isMCSiteLocal
    every { isMCOrgLocal() } returns isMCOrgLocal
    every { getAddress() } returns address
    every { getHostAddress() } returns hostAddress
}

fun mockInet6Address(
    address: ByteArray = ByteArray(size = 0),
    hostAddress: String = "",
    scopeId: Int = 0,
    scopedInterface: NetworkInterface? = null,
    isMulticastAddress: Boolean = false,
    isAnyLocalAddress: Boolean = false,
    isLoopbackAddress: Boolean = false,
    isLinkLocalAddress: Boolean = false,
    isSiteLocalAddress: Boolean = false,
    isMCGlobal: Boolean = false,
    isMCNodeLocal: Boolean = false,
    isMCLinkLocal: Boolean = false,
    isMCSiteLocal: Boolean = false,
    isMCOrgLocal: Boolean = false,
    isIPv4CompatibleAddress: Boolean = false
): Inet6Address = mockk<Inet6Address>().apply {
    every { isMulticastAddress() } returns isMulticastAddress
    every { isAnyLocalAddress() } returns isAnyLocalAddress
    every { isLoopbackAddress() } returns isLoopbackAddress
    every { isLinkLocalAddress() } returns isLinkLocalAddress
    every { isSiteLocalAddress() } returns isSiteLocalAddress
    every { isMCGlobal() } returns isMCGlobal
    every { isMCNodeLocal() } returns isMCNodeLocal
    every { isMCLinkLocal() } returns isMCLinkLocal
    every { isMCSiteLocal() } returns isMCSiteLocal
    every { isMCOrgLocal() } returns isMCOrgLocal
    every { getAddress() } returns address
    every { getScopeId() } returns scopeId
    every { getScopedInterface() } returns scopedInterface
    every { getHostAddress() } returns hostAddress
    every { isIPv4CompatibleAddress() } returns isIPv4CompatibleAddress
}
