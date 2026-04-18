import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val projectCompileSdk: String by project
val projectMinSdk: String by project

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinCompose)
}

kotlin {
    android {
        namespace = "com.felipearpa.ui.lazy.sample"
        compileSdk = projectCompileSdk.toInt()
        minSdk = projectMinSdk.toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "SampleApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":lazy-paging"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.paging.common)
            implementation(libs.paging.compose)
        }
    }
}
