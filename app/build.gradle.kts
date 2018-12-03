import com.android.build.gradle.api.ApplicationVariant
import org.gradle.kotlin.dsl.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Versions.compileSdk)

    dataBinding.isEnabled = true
    androidExtensions.isExperimental = true
    
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
            isMinifyEnabled = true
            isUseProguard = true
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
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))
    implementation(Deps.appcompat)
    implementation(Deps.androidxCore)
    implementation(Deps.recyclerView)
    implementation(Deps.constraint)
    implementation(Deps.material)
    implementation(Deps.materialDialogs)

    kapt(Deps.databindingCompiler)

    implementation(Deps.lifecycleExtensions)
    implementation(Deps.lifecycleReactive)
    kapt(Deps.lifecycleCompiler)

    implementation(Deps.room)
    kapt(Deps.roomCompiler)
    implementation(Deps.roomRxJava)

    implementation(Deps.fastAdapterCore)
    implementation(Deps.fastAdapterCommons)

    implementation(Deps.retrofit)
    implementation(Deps.gson)

    implementation(Deps.dagger)
    kapt(Deps.daggerCompiler)

    implementation(Deps.timber)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
}
