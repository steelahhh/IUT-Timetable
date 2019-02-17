import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Versions.compileSdk)
    androidExtensions {
        configure(delegateClosureOf<AndroidExtensionsExtension> {
            isExperimental = true
        })
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = ApplicationID.default
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

    flavorDimensions ("default")

    productFlavors {
        create("stable") {
            applicationId = ApplicationID.default
        }
        create("develop") {
            applicationId = ApplicationID.develop
            versionNameSuffix = "d"
        }
    }
}

dependencies {
    implementation(Deps.multidex)
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))
    implementation(Deps.appcompat)
    implementation(Deps.androidxCore)
    implementation(Deps.recyclerView)
    implementation(Deps.constraint)
    implementation(Deps.material)
    implementation(Deps.materialDialogs)

    implementation(Deps.roxie)

    implementation(Deps.lifecycleExtensions)
    implementation(Deps.rxKotlin)
    implementation(Deps.rxAndroid)
    implementation(Deps.rxBinding.core)
    implementation(Deps.rxBinding.material)

    implementation(Deps.room)
    kapt(Deps.roomCompiler)
    implementation(Deps.roomRxJava)

    implementation(Deps.fastAdapterCore)
    implementation(Deps.fastAdapterCommons)

    implementation(Deps.retrofit)
    implementation(Deps.retrofitGson)
    implementation(Deps.retrofitRxJava)
    implementation(Deps.gson)

    implementation(Deps.jsoup)

    implementation(Deps.koin.core)
    implementation(Deps.koin.ext)
    testImplementation(Deps.koin.test)
    implementation(Deps.koin.android)
    implementation(Deps.koin.androidScope)

    implementation(Deps.timber)

    debugImplementation(Deps.leakCanary)
    releaseImplementation(Deps.leakCanaryNoOp)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
}
