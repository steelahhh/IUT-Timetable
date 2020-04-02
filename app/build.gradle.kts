import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("com.google.gms.google-services")
    id("io.fabric")
}

fun getProperty(fileName: String, prop: String): Any? {
    val propsFile = rootProject.file(fileName)
    if (propsFile.exists()) {
        val props = Properties()
        props.load(FileInputStream(propsFile))
        return props[prop]
    }
    return null
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

    signingConfigs {
        create("release") {
            keyAlias = "${getProperty("local.properties", "key_alias")}"
            keyPassword = "${getProperty("local.properties", "key_password")}"
            storeFile = file("${getProperty("local.properties", "key_location")}")
            storePassword = "${getProperty("local.properties", "store_password")}"
        }
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
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
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
    arrayOf(
        Modules.common,
        Modules.coreUi,
        Modules.data,
        Modules.pickgroup,
        Modules.schedule,
        Modules.settings
    ).forEach { dependency ->
        implementation(project(dependency))
    }

    arrayOf(
        kotlin("stdlib-jdk7", Versions.kotlin),
        Deps.timber,
        Deps.ribs.android,
        Deps.dagger.core,
        Deps.multidex,
        Deps.firebase.core,
        Deps.firebase.crashlytics,
        Deps.klock.core,
        Deps.klock.android
    ).forEach {
        implementation(it)
    }

    debugImplementation(Deps.leakCanary)

    kapt(Deps.dagger.compiler)
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
