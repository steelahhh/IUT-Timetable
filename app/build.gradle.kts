import com.github.triplet.gradle.play.PlayPublisherExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.github.triplet.play") version "3.2.0-agp4.2-2"
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
        features = setOf("parcelize")
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

configure<PlayPublisherExtension> {
    serviceAccountCredentials.set(file("google-publisher-key.json"))

    track.set("internal")

    releaseStatus.set(com.github.triplet.gradle.androidpublisher.ReleaseStatus.DRAFT)
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
        kotlin("stdlib-jdk8", Versions.kotlin),
        Deps.timber,
        Deps.ribs.android,
        Deps.dagger.core,
        Deps.multidex,
        Deps.firebase.core,
        Deps.firebase.crashlytics,
        Deps.klock.android,
        Deps.retrofit,
        Deps.room
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
