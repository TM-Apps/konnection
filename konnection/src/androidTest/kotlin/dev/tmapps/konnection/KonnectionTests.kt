package dev.tmapps.konnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers
import java.net.NetworkInterface

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class KonnectionTests {
    private val context = mockk<Context>(relaxed = true)
    private val connectivityManager = mockk<ConnectivityManager>(relaxed = true)
    private val externalIpResolver = mockk<ExternalIpResolver>()

    private val konnection by lazy {
        Konnection(
            context = context,
            externalIpResolvers = listOf(externalIpResolver)
        )
    }

    @Before
    fun setup() {
        setAndroidVersion(Build.VERSION_CODES.P)
        every { context.getSystemService(any()) } returns connectivityManager
    }

    @Test @Suppress("DEPRECATION")
    fun `isConnected() should returns true when Android API less than 23 and it has connection`() {
        setAndroidVersion(Build.VERSION_CODES.LOLLIPOP)

        val networkInfo = mockk<NetworkInfo>()

        every { networkInfo.type } returns ConnectivityManager.TYPE_WIFI
        every { connectivityManager.activeNetworkInfo } returns networkInfo

        Assert.assertTrue(konnection.isConnected())
    }

    @Test
    fun `isConnected() should returns true when Android API higher than 23 and it has connection`() {
        setAndroidVersion(Build.VERSION_CODES.P)

        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { capabilities.hasCapability(any()) } returns true
        every { capabilities.hasTransport(any()) } returns true
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { connectivityManager.activeNetwork } returns network

        Assert.assertTrue(konnection.isConnected())
    }

    @Test @Suppress("DEPRECATION")
    fun `getCurrentNetworkConnection() should returns null when Android API less than 23 and has no connection`() {
        setAndroidVersion(Build.VERSION_CODES.LOLLIPOP)

        every { connectivityManager.activeNetworkInfo } returns null

        Assert.assertEquals(null, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection() should returns null when Android API higher than 23 and has no connection`() {
        setAndroidVersion(Build.VERSION_CODES.P)

        every { connectivityManager.getNetworkCapabilities(any()) } returns null
        every { connectivityManager.activeNetwork } returns null

        Assert.assertEquals(null, konnection.getCurrentNetworkConnection())
    }

    @Test @Suppress("DEPRECATION")
    fun `getCurrentNetworkConnection() should returns WIFI when Android API less than 23 and connection type is WiFi`() {
        setAndroidVersion(Build.VERSION_CODES.LOLLIPOP)

        val networkInfo = mockk<NetworkInfo>()

        every { networkInfo.type } returns ConnectivityManager.TYPE_WIFI
        every { connectivityManager.activeNetworkInfo } returns networkInfo

        Assert.assertEquals(NetworkConnection.WIFI, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection() should returns WIFI when Android API higher than 23 and connection type is WiFi`() {
        setAndroidVersion(Build.VERSION_CODES.P)

        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { capabilities.hasCapability(any()) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { connectivityManager.activeNetwork } returns network

        Assert.assertEquals(NetworkConnection.WIFI, konnection.getCurrentNetworkConnection())
    }

    @Test @Suppress("DEPRECATION")
    fun `getCurrentNetworkConnection() should returns MOBILE when Android API less than 23 and connection type is Mobile`() {
        setAndroidVersion(Build.VERSION_CODES.LOLLIPOP)

        val networkInfo = mockk<NetworkInfo>()

        every { networkInfo.type } returns ConnectivityManager.TYPE_MOBILE
        every { connectivityManager.activeNetworkInfo } returns networkInfo

        Assert.assertEquals(NetworkConnection.MOBILE, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentNetworkConnection() should returns MOBILE when Android API higher than 23 and connection type is Mobile`() {
        setAndroidVersion(Build.VERSION_CODES.P)

        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { capabilities.hasCapability(any()) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { connectivityManager.activeNetwork } returns network

        Assert.assertEquals(NetworkConnection.MOBILE, konnection.getCurrentNetworkConnection())
    }

    @Test
    fun `getCurrentIpInfo() should returns WifiIpInfo true when the current connection type is Wifi`() = runTest {
        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { capabilities.hasCapability(any()) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { connectivityManager.activeNetwork } returns network

        val ipv4 = "255.255.255.255"
        val ipv6 = "805B:2D9D:DC28:0000:0000:0000:D4C8:1FFF"

        val networkInterfaces = listOf(
            mockNetworkInterface(
                index = 0,
                name = "fake-NetworkInterface-0",
                inetAddresses = listOf(
                    mockInet4Address(hostAddress = "255.255.255.255", isLoopbackAddress = false),
                    mockInet6Address(hostAddress = ipv6, isLoopbackAddress = false)
                )
            )
        )

        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns networkInterfaces.toEnumeration()

        Assert.assertEquals(IpInfo.WifiIpInfo(ipv4 = ipv4, ipv6 = ipv6), konnection.getCurrentIpInfo())
    }

    @Test @Suppress("DEPRECATION")
    fun `getCurrentIpInfo() should returns MobileIpInfo when the current connection type is Mobile`() = runTest {
        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities>()

        every { capabilities.hasCapability(any()) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
        every { connectivityManager.activeNetwork } returns network

        val ipv4 = "255.255.255.255"

        val networkInterfaces = listOf(
            mockNetworkInterface(
                index = 0,
                name = "fake-NetworkInterface-0",
                inetAddresses = listOf(
                    mockInet4Address(hostAddress = "255.255.255.255", isLoopbackAddress = false)
                )
            )
        )

        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns networkInterfaces.toEnumeration()

        val externalIpV4 = "192.192.192.192"
        coEvery { externalIpResolver.get() } returns externalIpV4

        Assert.assertEquals(IpInfo.MobileIpInfo(hostIpv4 = ipv4, externalIpV4 = externalIpV4), konnection.getCurrentIpInfo())
    }

    private fun setAndroidVersion(version: Int) {
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", version)
    }
}
