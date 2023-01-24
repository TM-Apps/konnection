package scripts

import isMacOsMachine
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform") apply false
}

if (isMacOsMachine()) {

val moduleFrameworkName = project.name.capitalize()

kotlin {
    // add a platform switching to have an IDE support
 // const val buildForDevice = project.findProperty('kotlin.native.cocoapods.target') == 'ios_arm'
 // const val buildForDevice = project.findProperty('device')?.toBoolean() ?: false

    val xcf = XCFramework(moduleFrameworkName)

    fun nativeTargetConfig(): KotlinNativeTarget.() -> Unit = {
        binaries {
            framework(moduleFrameworkName) {
                //baseName = "HumanKt"
                isStatic = true

                xcf.add(this)
            }
        }
    }

    ios(configure = nativeTargetConfig())
    iosSimulatorArm64(configure = nativeTargetConfig())

    sourceSets {
        val iosMain by getting

        val iosSimulatorArm64Main by getting
        iosSimulatorArm64Main.dependsOn(iosMain)

        val iosTest by sourceSets.getting
        val iosSimulatorArm64Test by sourceSets.getting
        iosSimulatorArm64Test.dependsOn(iosTest)
    }
}

// https://youtrack.jetbrains.com/issue/KT-46257
// MPP: Stdlib included more than once for an enabled hierarchical commonization
afterEvaluate {
    println("compilations: ${kotlin.targets["metadata"].compilations}")
    val compilation = kotlin.targets["metadata"].compilations["iosMain"]
    compilation.compileKotlinTask.doFirst {
        compilation.compileDependencyFiles = files(
            compilation.compileDependencyFiles.filterNot { it.absolutePath.endsWith("klib/common/stdlib") }
        )
    }
}

}