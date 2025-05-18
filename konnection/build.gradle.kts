import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.mokkery)
    id("maven-publish")
    id("signing")
}

group = "dev.tmapps"
version = "1.4.4"

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
    compileSdk = 35
    
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

// Workaround for gradle issue: https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())

        pom {
            withXml {
                asNode().apply {
                    appendNode("name", project.name)
                    appendNode("description", "A Kotlin Multiplatform library for Network Connection data.")
                    appendNode("url", "https://github.com/TM-Apps/konnection")
                }
            }
            licenses {
                license {
                    name.set("The Apache Software License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("magnumrocha")
                    name.set("Magnum Rocha")
                    organization.set("TMApps")
                    organizationUrl.set("http://github.com/TM-Apps")
                }
            }
            scm {
                url.set("https://github.com/TM-Apps/konnection")
            }
        }
    }
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("SONATYPE_USER")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}

val signingKey = System.getenv("SIGN_KEY_PRIVATE")

if (signingKey != null) {
    val signingKeyId = System.getenv("SIGN_KEY_ID")
    val signingPassword = System.getenv("SIGN_KEY_PASSPHRASE")

    signing {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}
