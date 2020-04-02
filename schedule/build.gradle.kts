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
    arrayOf(
        Modules.common,
        Modules.coreUi,
        Modules.data,
        Modules.settings
    ).forEach { dependency ->
        implementation(project(dependency))
    }

    arrayOf(
        kotlin("stdlib-jdk7", Versions.kotlin),
        Deps.timber,
        Deps.ribs.android,
        Deps.rxRelay,
        Deps.constraint,
        Deps.androidxCore,
        Deps.recyclerView,
        Deps.material,
        Deps.dagger.core,
        Deps.groupie.core,
        Deps.groupie.ktx,
        Deps.materialDialogs,
        Deps.klock.core,
        Deps.klock.android,
        Deps.rxKotlin,
        Deps.rxAndroid,
        Deps.retrofit,
        Deps.gson,
        Deps.room
    ).forEach { dependency ->
        implementation(dependency)
    }

    kapt(Deps.dagger.compiler)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
}
