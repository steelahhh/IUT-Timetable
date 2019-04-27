import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Versions.compileSdk)
    androidExtensions {
        isExperimental = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.appVersionCode
        versionName = Versions.appVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isUseProguard = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            isUseProguard = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))
    implementation(project(Modules.common))

    api(Deps.room)
    kapt(Deps.roomCompiler)
    api(Deps.roomRxJava)

    api(Deps.gson)

    api(Deps.retrofit)
    api(Deps.retrofitGson)
    api(Deps.retrofitRxJava)

    api(Deps.jsoup)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
    testImplementation(Deps.koin.test)
}
