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
    implementation(project(Modules.coreUi))
    implementation(project(Modules.data))
    implementation(project(Modules.navigation))

    listOf(
        Deps.FastAdapter.ui,
        Deps.FastAdapter.diff,
        Deps.FastAdapter.drag,
        Deps.FastAdapter.scroll,
        Deps.FastAdapter.swipe,
        Deps.FastAdapter.utils
    ).forEach { dependency ->
        implementation(dependency)
    }

    implementation(Deps.Groupie.core)
    implementation(Deps.Groupie.ktx)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
    testImplementation(Deps.Koin.test)
}
