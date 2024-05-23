# konnection

A Kotlin Multiplatform Mobile library to emit Network Connection status.

![Sample Android](art/sample_android.gif) ![Sample iOS](art/sample_ios.gif)

## Add to your project

### build.gradle.kts
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.tmapps:konnection:1.3.1")
}
```

### Multiplatform (Common, JVM, Android, iOS)
In common code that should get compiled for different platforms, you can add dependency right to the `commonMain` source set:
```groovy
commonMain {
    dependencies {
        // works as common dependency as well as the platform one
        implementation("dev.tmapps:konnection:1.3.1")
    }
}
```

## Usage

### on Kotlin common code
```kotlin
// Get a default Konnection instance or create an instance.
-> val konnection = Konnection.instance
-> val konnection = Konnection.createInstance(enableDebugLog = true)
// NOTE: It is strongly recommended to work with only one Konnection instance on the App.

// get the immediate connection state
val hasNetworkConnection = konnection.isConnected()
// or observe it...
konnection.observeHasConnection()
    .collect { hasConnection -> ... }

// return ip info for Wifi (ipv4 and ipv6) and Mobile (host and external) connections
val currentIpInfo = konnection.getCurrentIpInfo()

// observes current NetworkConnection state (WIFI, MOBILE, ETHERNET or null).
konnection.observeConnection()
    .collect { state -> ... }
```

### on Android
``` kotlin
// It's possible to create a Konnection single instance with
// a specific `android.content.Context` instance
val konnection = Konnection.createInstance(context, enableDebugLog = true, context)
```

### on iOS
```kotlin
// Get a Konnection instance.
val konnection = Konnection.createInstance(enableDebugLog = true)

// Create Swift friendly APIs on Kotlin iOS source code.

fun hasConnectionObservation(callback: (Boolean) -> Unit) {
    konnection.observeHasConnection()
        .onEach { callback(it) }
        .launchIn(...)
}

fun networkConnectionObservation(callback: (NetworkConnection) -> Unit) {
    konnection.observeConnection()
        .onEach { callback(it) }
        .launchIn(...)
}

// Stops the publishing of connection state.
// This is necessary to stop and clear the internal SCNetworkReachability references
// and free the created pointers on native heap memory
Konnection.stop()
```

### on JVM
```kotlin
// It's possible to create a Konnection single instance with some extra parameters.
// - `connectionCheckTime = [duration]`: allow the control of the check connection time.
// - `pingHostCheckers`: list of hosts to ping on connection check, eg. "google.com", "apple.com", ...
val konnection = Konnection.createInstance(connectionCheckTime = 5.seconds, pingHostCheckers = listOf("myhost.com"))
```

## License

    Copyright 2021 TMApps
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.