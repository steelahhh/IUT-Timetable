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

    api(Deps.ribs)

    implementation(Deps.dagger.core)

    api(Deps.rxRelay)

    api(Deps.klock.core)
    api(Deps.klock.android)

    api(Deps.timber)

    api(Deps.rxKotlin)
    api(Deps.rxAndroid)

    debugApi(Deps.leakCanary)
}
