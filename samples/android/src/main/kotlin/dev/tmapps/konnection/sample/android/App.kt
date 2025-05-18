package dev.tmapps.konnection.sample.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.tmapps.konnection.ConnectionInfo
import dev.tmapps.konnection.Konnection
import dev.tmapps.konnection.sample.android.theme.SampleTheme
import kotlinx.coroutines.CancellationException

@Composable
fun App(
    enableDebugLog: Boolean = false
) {
    val konnection = Konnection.createInstance(enableDebugLog)
    val networkState = konnection.observeNetworkConnection().collectAsState(null)

    @Suppress("ProduceStateDoesNotAssignValue")
    val ipInfo = produceState<ConnectionInfo?>(null, networkState.value) {
        try {
            this.value = konnection.getInfo()
        } catch (error: Throwable) {
            if (error !is CancellationException) {
                throw error
            }
        }
    }

    var currentConnectionValue by remember { mutableStateOf(konnection.getCurrentNetworkConnection()) }

    SampleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    colorFilter = ColorFilter.tint(Color.White),
                    imageVector = networkState.value.icon,
                    modifier = Modifier.size(120.dp),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = networkState.value.message,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                ipInfo(ipInfo.value?.ipV4Info ?: "No IPv4 Info")
                ipInfo(ipInfo.value?.ipV6Info ?: "No IPv6 Info", addPadding = true)
                ipInfo(ipInfo.value?.externalIpInfo ?: "No External IP Info", addPadding = true)
            }
        }
    }
}

@Composable
fun ipInfo(
    ipInfo: String?,
    addPadding: Boolean = false
) {
    if (ipInfo == null) return

    if (addPadding) {
        Spacer(modifier = Modifier.height(8.dp))
    }

    Text(
        text = ipInfo,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}
