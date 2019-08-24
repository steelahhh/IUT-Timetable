plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(Versions.compileSdk)

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
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    api(kotlin("stdlib-jdk7", Versions.kotlin))

    api(Deps.mvRx)

    api(Deps.Koin.core)
    api(Deps.Koin.ext)
    api(Deps.Koin.android)
    api(Deps.Koin.androidScope)

    api(Deps.Mobius.core)
    api(Deps.Mobius.rx)
    api(Deps.Mobius.extras)
    api(Deps.Mobius.android)
    api(Deps.rxRelay)

    api(Deps.Klock.core)
    api(Deps.Klock.android)

    api(Deps.timber)

    api(Deps.rxKotlin)
    api(Deps.rxAndroid)

    api(Deps.leakSentry)
    debugApi(Deps.leakCanary)
}
