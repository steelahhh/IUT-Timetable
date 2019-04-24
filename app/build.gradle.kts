plugins {
    id("com.android.application")
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

    flavorDimensions("default")

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
    implementation(project(Modules.common))

    implementation(Deps.multidex)
    implementation(Deps.appcompat)
    implementation(Deps.androidxCore)
    implementation(Deps.recyclerView)
    implementation(Deps.constraint)

    implementation(Deps.mviCore.core)
    implementation(Deps.mviCore.android)

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

    implementation(Deps.klock.core)
//    implementation(Deps.klock.jvm)
    implementation(Deps.klock.android)

    implementation(Deps.koin.core)
    implementation(Deps.koin.ext)
    testImplementation(Deps.koin.test)
    implementation(Deps.koin.android)
    implementation(Deps.koin.androidScope)

    debugImplementation(Deps.leakCanary)
    releaseImplementation(Deps.leakCanaryNoOp)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
}

detekt {
    filters = ".*/resources/.*,.*/build/.*"
    config = files("../detekt-config.yml")
    val userHome = System.getProperty("user.home")

    idea {
        path = "$userHome/.idea"
        codeStyleScheme = "$userHome/.idea/idea-code-style.xml"
        inspectionsProfile = "$userHome/.idea/inspect.xml"
        report = "project.projectDir/reports"
        mask = "*.kt"
    }
}
