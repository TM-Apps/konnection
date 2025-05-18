rootProject.name = "konnection"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google() /*{
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }*/
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google() /*{
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }*/
    }
}

include(":konnection")
include(":konnection-swift")
include(":samples:compose")
include(":samples:android")