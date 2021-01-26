# konnection

A Kotlin Multiplatform Mobile library to emit Network Connection status.

![Sample Android](art/sample_android.gif) ![Sample iOS](art/sample_ios.gif)

## Add to your project

### common
```groovy
implementation "dev.tmapps:konnection:1.0.0"
```

### iOS
```groovy
implementation "dev.tmapps:konnection-ios:1.0.0"
```

### Android
```groovy
implementation "dev.tmapps:konnection-android:1.0.0"
```

## Usage

### on iOS

```kotlin
// instantiate the Konnection class
val konnection = Konnection()

// get the immediate connection state
val hasNetworkConnection = konnection.isConnected()

// emits current NetworkConnection (WIFI, MOBILE, NONE) state
fun networkConnectionObservation(callback: (NetworkConnection) -> Unit) {
    konnection.observeConnection()
        .onEach { callback(it) }
        .launchIn(...)
}

// stops the publishing of connection state.
// this is necessary because to stops and clear the internal SCNetworkReachability references
// and free the created pointers on native heap memory  
konnection.stop()
```

### on Android
```kotlin
// instantiate the Konnection class with a android.content.Context
val konnection = Konnection(context)

// get the immediate connection state
val hasNetworkConnection = konnection.isConnected()

// observes current NetworkConnection (WIFI, MOBILE, NONE) state.
konnection.observeConnection()
    .collect { state -> ... }
```