object BuildPlugins {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.2"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val detekt = "io.gitlab.arturbosch.detekt"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"

    const val googleKsp = "com.google.devtools.ksp"
}

object ScriptPlugins {
    const val kmmAndroid = "scripts.kmm_android"
    const val kmmIoS = "scripts.kmm_ios"
    const val publish = "scripts.publish"
}
