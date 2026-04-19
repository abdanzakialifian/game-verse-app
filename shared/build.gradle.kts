import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.client.logging)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation)
            implementation(libs.koin.annotations)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.napier)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }

    koinCompiler {
        userLogs = true
    }
}

android {
    namespace = "com.gameverse.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    dependencies {
        debugImplementation(libs.compose.uiTooling)
    }
}

