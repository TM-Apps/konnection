plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "2.0.0-RC3-1.0.20"
    id("maven-publish")
    id("signing")
}

group = "dev.tmapps"
version = "1.3.1"

ksp {
    arg("io.mockative:mockative:opt-in:dev.tmapps.konnection.utils.ReachabilityInteractor", "kotlinx.cinterop.ExperimentalForeignApi")
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.mockative.processor)
        }
}

tasks.jvmTest {
    jvmArgs(
        "--add-opens", "java.base/java.net=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED"
    )
}

// publishing

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
     // publishAllLibraryVariants()
    }
    jvm {
        withSourcesJar(publish = true)
    }
}

//val libSourcesJar by tasks.registering(Jar::class) {
//    archiveClassifier.set("sources")
// // from(sourceSets.main.get().allSource)
//}

// Workaround for gradle issue: https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
 // from(sourceSets.main.get().allSource)
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
