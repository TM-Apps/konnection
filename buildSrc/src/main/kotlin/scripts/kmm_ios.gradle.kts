package scripts

import isMacOsMachine

plugins {
    kotlin("multiplatform") apply false
    id("com.chromaticnoise.multiplatform-swiftpackage")
}

if (isMacOsMachine()) {

val moduleFrameworkName = project.name.capitalize()

kotlin {
    // add a platform switching to have an IDE support
 // const val buildForDevice = project.findProperty('kotlin.native.cocoapods.target') == 'ios_arm'
 // const val buildForDevice = project.findProperty('device')?.toBoolean() ?: false

    ios {
        binaries {
            framework {
                baseName = moduleFrameworkName
            }
        }
    }

    multiplatformSwiftPackage {
        packageName(moduleFrameworkName)
        swiftToolsVersion("5.3")
        targetPlatforms {
            iOS { v("13") }
        }
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