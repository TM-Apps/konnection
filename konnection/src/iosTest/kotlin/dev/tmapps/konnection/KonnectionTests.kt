package dev.tmapps.konnection

import dev.tmapps.konnection.utils.IfaddrsInteractor
import dev.tmapps.konnection.utils.ReachabilityInteractor
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.eq
import io.mockative.given
import io.mockative.mock
import kotlinx.coroutines.test.runTest
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsIsWWAN
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable
import platform.posix.AF_INET
import platform.posix.AF_INET6
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KonnectionTests {

    @Mock private val externalIpResolver = mock(classOf<ExternalIpResolver>())
    @Mock private val reachabilityInteractor = mock(classOf<ReachabilityInteractor>())
    @Mock private val ifaddrsInteractor = mock(classOf<IfaddrsInteractor>())

    private val konnection by lazy {
        Konnection(externalIpResolvers = listOf(externalIpResolver)).apply {
            reachabilityInteractor = this@KonnectionTests.reachabilityInteractor
            ifaddrsInteractor = this@KonnectionTests.ifaddrsInteractor
        }
    }

    @Test
    fun `isConnected should returns true when device has connection`() {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsReachable }

        assertTrue(konnection.isConnected())
    }

    @Test
    fun `isConnected should returns false when device has no connection`() {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsConnectionRequired }

        assertFalse(konnection.isConnected())
    }

    @Test
    fun `getCurrentNetworkConnection should returns null when device has no connection`() {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsConnectionRequired }

        assertEquals(null, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection should returns WIFI when device connection type is WiFi`() {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsReachable }

        assertEquals(NetworkConnection.WIFI, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection should returns MOBILE when device connection type is Mobile`() {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsReachable + kSCNetworkReachabilityFlagsIsWWAN }

        assertEquals(NetworkConnection.MOBILE, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentIpInfo should returns WifiIpInfo true when the current connection type is Wifi`() = runTest {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsReachable }

        val ipv4 = "255.255.255.255"
        val ipv6 = "805B:2D9D:DC28:0000:0000:0000:D4C8:1FFF"

        given(ifaddrsInteractor).function(ifaddrsInteractor::get)
            .whenInvokedWith(any(), eq(AF_INET))
            .thenReturn(ipv4)

        given(ifaddrsInteractor).function(ifaddrsInteractor::get)
            .whenInvokedWith(any(), eq(AF_INET6))
            .thenReturn(ipv6)

        assertEquals(IpInfo.WifiIpInfo(ipv4 = ipv4, ipv6 = ipv6), konnection.getCurrentIpInfo())
    }

    @Test
    fun `getCurrentIpInfo should returns MobileIpInfo when the current connection type is Mobile`() = runTest {
        given(reachabilityInteractor).function(reachabilityInteractor::getReachabilityFlags)
            .whenInvokedWith(any())
            .then { kSCNetworkReachabilityFlagsReachable + kSCNetworkReachabilityFlagsIsWWAN }

        val ipv4 = "255.255.255.255"
        val externalIpV4 = "192.192.192.192"

        given(ifaddrsInteractor).function(ifaddrsInteractor::get)
            .whenInvokedWith(any(), eq(AF_INET))
            .thenReturn(ipv4)

        given(ifaddrsInteractor).function(ifaddrsInteractor::get)
            .whenInvokedWith(any(), eq(AF_INET6))
            .thenReturn(null)

        given(externalIpResolver).suspendFunction(externalIpResolver::get)
            .whenInvoked()
            .then { externalIpV4 }

        assertEquals(IpInfo.MobileIpInfo(hostIpv4 = ipv4, externalIpV4 = externalIpV4), konnection.getCurrentIpInfo())
    }
}
