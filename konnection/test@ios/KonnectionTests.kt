package dev.tmapps.konnection

import dev.tmapps.konnection.utils.IfaddrsInteractor
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.eq
import io.mockative.every
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import platform.posix.AF_INET
import platform.posix.AF_INET6
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KonnectionTests {

    @Mock private val networkMonitor = mock(classOf<NetworkMonitor>())
    @Mock private val externalIpResolver = mock(classOf<IpResolver>())
    @Mock private val ifaddrsInteractor = mock(classOf<IfaddrsInteractor>())

    private val konnection by lazy {
        Konnection.createInstance(ipResolvers = listOf(externalIpResolver)).apply {
            this.networkMonitor = this@KonnectionTests.networkMonitor
            this.ifaddrsInteractor = this@KonnectionTests.ifaddrsInteractor
        }
    }

    @Test
    fun `isConnected should returns true when Network Monitor isConnected returns true`() {
        every { networkMonitor.isConnected() }.returns(true)
        assertTrue(konnection.isConnected())
    }

    @Test
    fun `isConnected should returns false when Network Monitor isConnected returns false`() {
        every { networkMonitor.isConnected() }.returns(false)
        assertFalse(konnection.isConnected())
    }

    @Test
    fun `getCurrentNetworkConnection should returns null when Network Monitor getCurrentNetworkConnection returns null`() {
        every { networkMonitor.getCurrentNetworkConnection() }.returns(null)
        assertEquals(null, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection should returns WIFI when Network Monitor getCurrentNetworkConnection returns WiFi`() {
        every { networkMonitor.getCurrentNetworkConnection() }.returns(NetworkConnection.WIFI)
        assertEquals(NetworkConnection.WIFI, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `SCNetworkReachabilityNetworkMonitor - getCurrentNetworkConnection should returns MOBILE when device connection type is Mobile`() {
        every { networkMonitor.getCurrentNetworkConnection() }.returns(NetworkConnection.MOBILE)
        assertEquals(NetworkConnection.MOBILE, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentIpInfo should returns WifiIpInfo true when the current connection type is Wifi`() = runTest {
        every { networkMonitor.getCurrentNetworkConnection() }.returns(NetworkConnection.WIFI)

        val ipv4 = "255.255.255.255"
        val ipv6 = "805B:2D9D:DC28:0000:0000:0000:D4C8:1FFF"

        every { ifaddrsInteractor.get(any(), eq(AF_INET)) }.returns(ipv4)
        every { ifaddrsInteractor.get(any(), eq(AF_INET6)) }.returns(ipv6)
        coEvery { externalIpResolver.get() }.returns(null)

        assertEquals(ConnectionInfo(connection = NetworkConnection.WIFI, ipv4 = ipv4, ipv6 = ipv6), konnection.getInfo())
    }

    @Test
    fun `getCurrentIpInfo should returns MobileIpInfo when the current connection type is Mobile`() = runTest {
        every { networkMonitor.getCurrentNetworkConnection() }.returns(NetworkConnection.MOBILE)

        val ipv4 = "255.255.255.255"
        val externalIpV4 = "192.192.192.192"

        every { ifaddrsInteractor.get(any(), eq(AF_INET)) }.returns(ipv4)
        every { ifaddrsInteractor.get(any(), eq(AF_INET6)) }.returns(null)
        coEvery { externalIpResolver.get() }.returns(externalIpV4)

        assertEquals(ConnectionInfo(connection = NetworkConnection.MOBILE, ipv4 = ipv4, externalIpV4 = externalIpV4), konnection.getInfo())
    }

    @Test
    fun `getCurrentIpInfo should returns MobileIpInfo when the current connection type is Ethernet`() = runTest {
        every { networkMonitor.getCurrentNetworkConnection() }.returns(NetworkConnection.ETHERNET)

        val ipv4 = "255.255.255.255"
        val ipv6 = "2001:0000:130F:0000:0000:09C0:876A:130B"
        val externalIpV4 = "192.192.192.192"

        every { ifaddrsInteractor.get(any(), eq(AF_INET)) }.returns(ipv4)
        every { ifaddrsInteractor.get(any(), eq(AF_INET6)) }.returns(ipv6)
        coEvery { externalIpResolver.get() }.returns(externalIpV4)

        assertEquals(ConnectionInfo(connection = NetworkConnection.ETHERNET, ipv4 = ipv4, ipv6 = ipv6, externalIpV4 = externalIpV4), konnection.getInfo())
    }
}
