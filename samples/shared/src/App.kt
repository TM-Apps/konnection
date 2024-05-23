package dev.tmapps.konnection.sample

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.tmapps.konnection.IpInfo
import dev.tmapps.konnection.Konnection
import dev.tmapps.konnection.sample.theme.SampleTheme

@Composable
fun App(
    enableDebugLog: Boolean = false
) {
    val konnection by remember { mutableStateOf(Konnection.createInstance(enableDebugLog)) }
    val networkState = konnection.observeNetworkConnection().collectAsState(null)

    val ipInfo = produceState<IpInfo?>(null, networkState.value) { value = konnection.getCurrentIpInfo() }

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

                Text(
                    text = ipInfo.value?.ipInfo1 ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = ipInfo.value?.ipInfo2 ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
