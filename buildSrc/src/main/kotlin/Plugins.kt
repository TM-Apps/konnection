object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "7.1.2"
        const val detektVersion = "1.20.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val detekt = "io.gitlab.arturbosch.detekt"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
}

object ScriptPlugins {
    const val kmmAndroid = "scripts.kmm_android"
    const val kmmIoS = "scripts.kmm_ios"
    const val publish = "scripts.publish"
}
