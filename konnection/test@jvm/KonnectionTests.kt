package dev.tmapps.konnection

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.InetAddress
import java.net.NetworkInterface

class KonnectionTests {
    private val externalIpResolver = mockk<IpResolver>()

    private val konnection by lazy {
        Konnection(ipResolvers = listOf(externalIpResolver))
    }

    @Test
    fun `isConnected() should returns false when has no internet connection`() {
        mockkStatic(InetAddress::class)
        every { InetAddress.getByName(any()).isReachable(any()) } returns false
        Assert.assertFalse(konnection.isConnected())
    }

    @Test
    fun `isConnected() should returns true when has internet connection`() {
        mockkStatic(InetAddress::class)
        every { InetAddress.getByName(any()).isReachable(any()) } returns true
        Assert.assertTrue(konnection.isConnected())
    }
    
    @Test
    fun `getCurrentNetworkConnection() should returns null when has no connection`() {
        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns emptyList<NetworkInterface>().toEnumeration()
        Assert.assertEquals(null, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection() should returns WIFI when available connection type is WiFi`() {
        val networkInterfaces = listOf(
            mockNetworkInterface(
                index = 0,
                name = "fake-NetworkInterface-0",
                displayName = "wi-fi",
                isUp = true,
                isLoopback = false
            )
        )

        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } answers { networkInterfaces.toEnumeration() }

        Assert.assertEquals(NetworkConnection.WIFI, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection() should returns ETHERNET when available connection type is WiFi`() {
        val networkInterfaces = listOf(
            mockNetworkInterface(
                index = 0,
                name = "fake-NetworkInterface-0",
                displayName = "lan",
                isUp = true,
                isLoopback = false
            )
        )

        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } answers { networkInterfaces.toEnumeration() }

        Assert.assertEquals(NetworkConnection.ETHERNET, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentIpInfo() should returns WifiIpInfo true when the current connection type is Wifi`() = runTest {
        val ipv4 = "255.255.255.255"
        val ipv6 = "805B:2D9D:DC28:0000:0000:0000:D4C8:1FFF"

        val networkInterfaces = listOf(
            mockNetworkInterface(
                index = 0,
                name = "fake-NetworkInterface-0",
                displayName = "wi-fi",
                isUp = true,
                isLoopback = false,
                inetAddresses = listOf(
                    mockInet4Address(hostAddress = ipv4, isLoopbackAddress = false),
                    mockInet6Address(hostAddress = ipv6, isLoopbackAddress = false)
                )
            )
        )

        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } answers { networkInterfaces.toEnumeration() }

        Assert.assertEquals(IpInfo.WifiIpInfo(ipv4 = ipv4, ipv6 = ipv6), konnection.getCurrentIpInfo())
    }

    @Test
    fun `getCurrentIpInfo() should returns EthernetIpInfo true when the current connection type is Ethernet`() = runTest {
        val ipv4 = "255.255.255.255"
        val ipv6 = "805B:2D9D:DC28:0000:0000:0000:D4C8:1FFF"

        val networkInterfaces = listOf(
            mockNetworkInterface(
                index = 0,
                name = "fake-NetworkInterface-0",
                displayName = "ethernet",
                isUp = true,
                isLoopback = false,
                inetAddresses = listOf(
                    mockInet4Address(hostAddress = ipv4, isLoopbackAddress = false),
                    mockInet6Address(hostAddress = ipv6, isLoopbackAddress = false)
                )
            )
        )

        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } answers { networkInterfaces.toEnumeration() }

        Assert.assertEquals(IpInfo.EthernetIpInfo(ipv4 = ipv4, ipv6 = ipv6), konnection.getCurrentIpInfo())
    }
}
