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
    implementation(project(Modules.common))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.data))
    implementation(project(Modules.navigation))

    implementation(Deps.multidex)

    implementation(Deps.mviCore.core)
    implementation(Deps.mviCore.android)

    implementation(Deps.lifecycleExtensions)

    implementation(Deps.fastAdapterCore)
    implementation(Deps.fastAdapterCommons)

    implementation(Deps.klock.core)
//    implementation(Deps.klock.jvm)
    implementation(Deps.klock.android)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.espresso)
    testImplementation(Deps.koin.test)
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
