plugins {
    id("com.android.library")
    kotlin("android")
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

    api(Deps.koin.core)
    api(Deps.koin.ext)
    api(Deps.koin.android)
    api(Deps.koin.androidScope)

    api(Deps.timber)

    api(Deps.rxKotlin)
    api(Deps.rxAndroid)

    testApi(Deps.koin.test)
}
