plugins {
    kotlin("multiplatform")
    id(BuildPlugins.detekt).version(BuildPlugins.Versions.detektVersion)
}
group = "dev.tmapps"
version = projectVersion

repositories {
    google()
    mavenCentral()
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
                progressiveMode = true
                experimentalAnnotations.forEach { optIn(it) }
                //  languageVersion = "1.6" // possible values: '1.0', '1.1', .., '1.6'
                //  apiVersion = "1.6" // possible values: '1.0', .., '1.6'
                //  enableLanguageFeature("InlineClasses") // language feature name
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Dependencies.kotlinCoroutinesCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Dependencies.turbine)
                // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
    }

    sourceSets.matching { it.name.endsWith("Test") }
        .configureEach {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
}

// Internal Script plugins
apply(plugin = ScriptPlugins.kmmAndroid)
apply(plugin = ScriptPlugins.kmmIoS)
apply(plugin = ScriptPlugins.publish)