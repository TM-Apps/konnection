package scripts

plugins {
    id("maven-publish")
    id("signing")
}

val sonatypeUser = System.getenv("SONATYPE_USER")
val sonatypePassword = System.getenv("SONATYPE_PASSWORD")

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
        withType<MavenPublication>().forEach { publication ->
            with(publication.pom) {
                withXml {
                    val root = asNode()
                    root.appendNode("name", project.name)
                    root.appendNode("description", "A Kotlin Multiplatform Mobile library to emit Network Connection status.")
                    root.appendNode("url", "https://github.com/TM-Apps/konnection")
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
                // publication.artifact(libSourcesJar.get()) { archiveClassifier.set("sources") }
                publication.artifact(libJavadocJar.get()) //{ archiveClassifier.set("javadoc") }
            }
        }
    }
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = sonatypeUser
                password = sonatypePassword
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
