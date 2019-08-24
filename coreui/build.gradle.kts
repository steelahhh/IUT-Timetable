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
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(Modules.common))
    api(Deps.constraint)
    api(Deps.appcompat)
    api(Deps.androidxCore)
    api(Deps.recyclerView)

    api(Deps.material)
    api(Deps.materialDialogs)

    api(Deps.Conductor.core)
    api(Deps.Conductor.rx2)
    api(Deps.Conductor.support)
    api(Deps.Conductor.lifecycle)

    api(Deps.RxBinding.core)
    api(Deps.RxBinding.material)
    api(Deps.RxBinding.appCompat)
}
