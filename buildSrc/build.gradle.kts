repositories {
    mavenCentral()
    google()
    // Needed only for SNAPSHOT versions
    // maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

plugins {
//  `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    implementation("com.android.tools.build:gradle:7.4.0")
}