plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
    id("maven-publish")
    id("signing")
}

group = "dev.tmapps"
version = "1.1.11"

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, "io.mockative:mockative-processor:2.0.1")
        }
}

// publishing

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    // publishAllLibraryVariants()
    }
}

val libSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
// from(sourceSets.main.get().allSource)
}

val libJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
// from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        withType<MavenPublication>().all {
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

            if (name == "kotlinMultiplatform") {
                // artifact(libSourcesJar.get()) { archiveClassifier.set("sources") }
                artifact(libJavadocJar.get()) //{ archiveClassifier.set("javadoc") }
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
