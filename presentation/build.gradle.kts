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
        multiDexEnabled = true
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
    implementation(project(Modules.common))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.data))
    implementation(project(Modules.navigation))

    implementation(Deps.fastAdapter.ui)
    implementation(Deps.fastAdapter.diff)
    implementation(Deps.fastAdapter.drag)
    implementation(Deps.fastAdapter.scroll)
    implementation(Deps.fastAdapter.swipe)
    implementation(Deps.fastAdapter.utils)

    implementation(Deps.groupie.core)
    implementation(Deps.groupie.ktx)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
    testImplementation(Deps.koin.test)
}
