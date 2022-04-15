package scripts

import developerId
import developerName
import license
import licenseUrl
import organization
import organizationUrl
import scm
import summary
import url

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
        withType<MavenPublication>().all {
            pom {
                withXml {
                    asNode().apply {
                        appendNode("name", project.name)
                        appendNode("description", project.summary)
                        appendNode("url", project.url)
                    }
                }
                licenses {
                    license {
                        name.set(project.license)
                        url.set(project.licenseUrl)
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set(project.developerId)
                        name.set(project.developerName)
                        organization.set(project.organization)
                        organizationUrl.set(project.organizationUrl)
                    }
                }
                scm {
                    url.set(project.scm)
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
