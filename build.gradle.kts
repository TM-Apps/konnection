import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.mokkery) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.detekt)
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        parallel = true
        config.setFrom(file("$rootDir/config/detekt.yml"))
        buildUponDefaultConfig = true
        ignoredBuildTypes = listOf("release")
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        exclude("**/build/**")
        exclude("**/generated/**")
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude("**/build/**")
        exclude("**/generated/**")
    }

    afterEvaluate {
        if (plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            val subTasks = mutableListOf("detektMetadataCommonMain")
            if (tasks.findByName("detektAndroidDebug") != null) {
                subTasks.add("detektAndroidDebug")
            }
            if (tasks.findByName("detektIosX64Main") != null) {
                subTasks.add("detektIosX64Main")
            }

            tasks.register("detektAll") {
                group = "detekt"
                dependsOn(*subTasks.toTypedArray())
            }
        }
    }
}
