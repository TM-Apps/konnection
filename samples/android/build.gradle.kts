plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
}

repositories {
    google()
    mavenCentral()

    // temporary until final Compose is released to kotlin 1.6.20
    maven {
        setUrl("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    compileSdk = 33
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "dev.tmapps.konnection.sample"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    composeOptions {
        // kotlinCompilerVersion = kotlinVersion
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }

    testOptions.unitTests.isIncludeAndroidResources = true

    buildFeatures {
        compose = true

        // disable unused AGP features
        // buildConfig false
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.excludes.add("META-INF/common.kotlin_module")
        resources.excludes.add("META-INF/*.kotlin_module")

        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")

        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check",
            //"-P",
            //"plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":konnection"))

    implementation(Dependencies.kotlinJdk)
    implementation(Dependencies.kotlinCoroutinesAndroid)

    implementation(Dependencies.androidXAppCompact)
    implementation(Dependencies.androidXCoreKtx)

    implementation(Dependencies.composeCompiler)
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeUI)
    implementation(Dependencies.composeUITooling)
    implementation(Dependencies.composeMaterialDesign)
    implementation(Dependencies.composeMaterialIconsCore)
    implementation(Dependencies.composeMaterialIconsExtended)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeRuntime)
 // androidTestImplementation(Dependencies.composeUITests)

    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.androidCoreTesting)

    androidTestImplementation(Dependencies.androidTestRunner)
    androidTestImplementation(Dependencies.androidJunit)
    androidTestImplementation(Dependencies.androidExpressoCore)
}