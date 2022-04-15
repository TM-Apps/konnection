package scripts

import isMacOsMachine
import org.jetbrains.kotlin.gradle.tasks.PodspecTask
import summary
import url

plugins {
    kotlin("multiplatform") apply false
    kotlin("native.cocoapods")
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

    cocoapods {
        // configure fields required by CocoaPods
        version = project.version.toString()
        summary = project.summary
        homepage = project.url

        // set the name for the produced framework, the module name is applied by default
        name = moduleFrameworkName

        framework {
            baseName = moduleFrameworkName
            isStatic = false // SwiftUI preview requires dynamic framework
        }

        ios.deploymentTarget = "12.4"
        podfile = project.file("../samples/ios/Podfile")
    }
}

// apply podspec file adjustments...
tasks.getByName<PodspecTask>("podspec") {
    doLast {
        val podspecFile = file("${projectDir}/${moduleFrameworkName}.podspec")
        val newPodspecContent = mutableListOf<String>()
        var trimLineBefore: String? = null
        podspecFile.forEachLine { originalLine ->
            var line = originalLine
            val trimLine = line.trim()

            if (trimLine.startsWith("spec.name")) {
                line = line.replaceFirst("=.*".toRegex(), "= \'$moduleFrameworkName\'")
            }
            if (trimLine.startsWith("spec.vendored_frameworks")) {
                line = line.replaceFirst("=.*".toRegex(), "= \"build/cocoapods/framework/#{spec.name}.framework\"")
            }
            if (trimLine.startsWith(":name")) {
                line = line.replaceFirst("=.*".toRegex(), "=> 'Build $moduleFrameworkName library',")
            }
            if (trimLine != trimLineBefore) {
                newPodspecContent.add(line)
            }
            trimLineBefore = trimLine
        }
        podspecFile.writeText(newPodspecContent.joinToString("\n"))
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