import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val projectCompileSdk: String by project
val projectMinSdk: String by project
val projectVersion: String by project

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinCompose)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    android {
        namespace = "com.felipearpa.ui.lazy"
        compileSdk = projectCompileSdk.toInt()
        minSdk = projectMinSdk.toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.paging.common)
            implementation(libs.paging.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotest.assertions.core)
        }
    }
}

mavenPublishing {
    configure(KotlinMultiplatform(javadocJar = com.vanniktech.maven.publish.JavadocJar.Empty()))

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("com.felipearpa", "lazy-paging", projectVersion)

    pom {
        name.set("lazy-paging")
        description.set("Kotlin Multiplatform Compose lazy column components with built-in loading, error, empty, and refresh states backed by Paging.")
        url.set("https://github.com/felipearpa/lazy-paging-kmp")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("felipearpa")
                name.set("Felipe")
                url.set("https://felipearpa.com")
            }
        }

        scm {
            url.set("https://github.com/felipearpa/lazy-paging-kmp")
            connection.set("scm:git:git://github.com/felipearpa/lazy-paging-kmp.git")
            developerConnection.set("scm:git:ssh://git@github.com/felipearpa/lazy-paging-kmp.git")
        }
    }
}

group = "com.felipearpa"
version = projectVersion
