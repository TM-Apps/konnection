/**
 * Project dependencies, makes it easy to include external binaries or
 * other library modules to build.
 */
object Dependencies {
    const val kotlinJdk = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"

    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val kotlinCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"

    // Jetpack Compose libraries
    const val composeCompiler = "androidx.compose.compiler:compiler:$composeCompilerVersion"
    const val composeBom = "androidx.compose:compose-bom:$composeVersion"
    const val composeUI = "androidx.compose.ui:ui"
    // Tooling support (Previews, etc.)
    const val composeUIToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeUITooling = "androidx.compose.ui:ui-tooling"
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    const val composeFoundation = "androidx.compose.foundation:foundation"
    // Material Design 2
    const val composeMaterialDesign = "androidx.compose.material:material"
    // Material design icons
    const val composeMaterialIconsCore = "androidx.compose.material:material-icons-core"
    const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended"
    // Compose APIs for Activity
    const val composeActivity = "androidx.activity:activity-compose:1.5.0"
    // Integration with observables
    const val composeRuntime = "androidx.compose.runtime:runtime"
 // const val composeLiveData = "androidx.compose.runtime:runtime-livedata"
    // UI tests
    const val composeUITests = "androidx.compose.ui:ui-test"

    const val androidXAppCompact = "androidx.appcompat:appcompat:1.5.0"
    const val androidXAnnotation = "androidx.annotation:annotation:1.4.0"
    const val androidXCoreKtx = "androidx.core:core-ktx:1.8.0"

    const val androidCoreTesting = "androidx.arch.core:core-testing:2.1.0"
    const val androidTestRunner = "androidx.test:runner:1.4.0"
    const val androidJunit = "androidx.test.ext:junit:1.1.3"
    const val androidExpressoCore = "androidx.test.espresso:espresso-core:3.4.0"

    const val junit = "junit:junit:4.13.2"
    const val turbine = "app.cash.turbine:turbine:0.9.0"
    const val robolectric = "org.robolectric:robolectric:4.8.1"

    private const val mockativeVersion = "1.2.6"
    const val mockative = "io.mockative:mockative:$mockativeVersion"
    const val mockativeProcessor = "io.mockative:mockative-processor:$mockativeVersion"

    private const val mockkVersion = "1.12.5"
    const val mockk = "io.mockk:mockk:$mockkVersion"
    const val mockkAgentJvm = "io.mockk:mockk-agent-jvm:$mockkVersion"
}
