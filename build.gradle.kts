buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.androidGradlePlugin)
    }
}

allprojects {
    // the only place where HostManager could be instantiated
    project.extra.apply {
        set("hostManager", hostManager)
    }

    repositories {
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
