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

    testOptions.unitTests.isIncludeAndroidResources = true

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
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
             // implementation("junit:junit:4.13.2")
            }
        }
    }
}