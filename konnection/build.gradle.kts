import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.maven.publish)
}

group = "dev.tmapps"
version = "1.4.5"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm {
        withSourcesJar(publish = true)
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "Konnection"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.annotation)
            implementation(libs.androidx.startup)
        }

        androidUnitTest.dependencies {
            implementation(libs.mockk)
            implementation(libs.mockk.agent.jvm)
            implementation(libs.robolectric)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.jvm)
        }

        jvmTest.dependencies {
            implementation(libs.mockk)
            implementation(libs.mockk.agent.jvm)
        }
    }
}

android {
    namespace = "dev.tmapps.konnection"
    compileSdk = 36
    
    defaultConfig {
        minSdk = 21
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    testOptions {
        unitTests.all {
            it.jvmArgs(
                "--add-opens", "java.base/java.net=ALL-UNNAMED",
                "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
                "--add-opens=java.base/java.util=ALL-UNNAMED"
            )
        }
    }
}

// tasks.jvmTest {
//     jvmArgs(
//         "--add-opens", "java.base/java.net=ALL-UNNAMED",
//         "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
//         "--add-opens", "java.base/java.util=ALL-UNNAMED"
//     )
// }

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), project.name, version.toString())

    pom {
        name = project.name
        description = "A Kotlin Multiplatform library for Network Connection data."
        url = "https://github.com/TM-Apps/konnection"
        licenses {
            license {
                name = "The Apache Software License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "magnumrocha"
                name = "Magnum Rocha"
                organization = "TMApps"
                organizationUrl = "http://github.com/TM-Apps"
            }
        }
        scm {
            url = "https://github.com/TM-Apps/konnection"
            connection = "scm:git:git://github.com/TM-Apps/konnection.git"
            developerConnection = "scm:git:ssh://git@github.com/TM-Apps/konnection.git"
        }
    }
}
