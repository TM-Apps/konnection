package scripts

plugins {
    kotlin("multiplatform") apply false
    id("com.android.library")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
     // publishAllLibraryVariants()
    }

    sourceSets {
        val androidMain by getting {
            kotlin.srcDir("src/androidMain/kotlin")
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(Dependencies.androidXAnnotation)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(Dependencies.mockk)
                implementation(Dependencies.mockkAgentJvm)
                implementation(Dependencies.robolectric)
                implementation(Dependencies.turbine)
            }
        }
    }
}