package dev.tmapps.konnection.sample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import dev.tmapps.konnection.Konnection
import dev.tmapps.konnection.NetworkConnection
import dev.tmapps.konnection.sample.ui.theme.SampleTheme

@Composable
fun LoadingState(overlayColor: Color = Color.Transparent) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = overlayColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Home(initialState: NetworkConnection = NetworkConnection.NONE) {
    val context = AmbientContext.current
    val konnection = Konnection(context)
    val state = konnection.observeConnection().collectAsState(initialState)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                colorFilter = ColorFilter.tint(Color.White),
                imageVector = state.value.icon,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = state.value.message),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "loading state")
@Composable
private fun LoadingStatePreview() {
    SampleTheme {
        LoadingState()
    }
}

@Preview(name = "home")
@Composable
private fun HomePreview() {
    SampleTheme {
        Home(initialState = NetworkConnection.WIFI)
    }
}
