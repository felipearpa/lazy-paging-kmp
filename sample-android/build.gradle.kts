val projectCompileSdk: String by project
val projectMinSdk: String by project
val projectVersion: String by project

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinCompose)
}

android {
    namespace = "com.felipearpa.ui.lazy.sample.android"
    compileSdk = projectCompileSdk.toInt()

    defaultConfig {
        applicationId = "com.felipearpa.ui.lazy.sample"
        minSdk = projectMinSdk.toInt()
        targetSdk = projectCompileSdk.toInt()
        versionCode = 1
        versionName = projectVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":sample"))
    implementation(libs.androidx.activity.compose)
}
