@file:Suppress("unused")

object ApplicationID {
    const val default = "com.alefimenko.iuttimetable"
    const val develop = "com.alefimenko.iuttimetable.develop"
}

object Modules {
    const val app = ":app"
    const val common = ":common"
    const val coreUi = ":coreui"
    const val data = ":data"
    const val navigation = ":navigation"
    const val presentation = ":presentation"
}

object Versions {
    const val minSdk = 19
    const val targetSdk = 28
    const val compileSdk = 28
    const val appVersionCode = 4202
    const val appVersionName = "3.0.0"

    const val kotlin = "1.3.41"
    const val androidPlugin = "3.6.0-alpha05"

    const val mviCore = "1.1.6"
    const val aac = "2.0.0"

    const val rxRelay = "2.1.0"
    const val rxBinding = "3.0.0-alpha2"
    const val rxKotlin = "2.2.0"
    const val rxAndroid = "2.0.2"
    const val room = "2.1.0"

    const val conductor = "3.0.0-rc1"

    const val leakCanary = "2.0-alpha-2"

    const val mvRx = "1.0.2"

    const val klock = "1.4.0"

    const val mobius = "1.2.2"

    const val jsoup = "1.11.3"

    const val groupie = "2.3.0"
    const val fastadapter = "4.0.1"
    const val retrofit = "2.4.0"
    const val gson = "2.8.5"
    const val koin = "2.0.1"
    const val timber = "4.7.1"
    const val materialDialogs = "3.1.0"
    const val junit = "4.12"
    const val testRunner = "1.1.0"
    const val espresso = "3.1.0"
    const val googleServices = "4.3.1"
}

object Deps {
    const val appcompat = "androidx.appcompat:appcompat:1.0.2"
    const val androidxCore = "androidx.core:core-ktx:1.0.2"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-beta02"
    const val material = "com.google.android.material:material:1.1.0-alpha09"
    const val constraint = "androidx.constraintlayout:constraintlayout:1.1.3"

    const val multidex = "com.android.support:multidex:1.0.3"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.aac}"
    const val lifecycleReactive = "androidx.lifecycle:lifecycle-reactivestreams:${Versions.aac}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.aac}"

    const val cicerone = "ru.terrakok.cicerone:cicerone:5.0.0"

    const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

    const val room = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"

    object Klock {
        const val core = "com.soywiz:klock-metadata:${Versions.klock}"
        const val jvm = "com.soywiz:klock-jvm:${Versions.klock}"
        const val android = "com.soywiz:klock-android:${Versions.klock}"
    }

    object FastAdapter {
        const val diff = "com.mikepenz:fastadapter-extensions-diff:${Versions.fastadapter}"
        const val drag = "com.mikepenz:fastadapter-extensions-drag:${Versions.fastadapter}"
        const val scroll = "com.mikepenz:fastadapter-extensions-scroll:${Versions.fastadapter}"
        const val swipe = "com.mikepenz:fastadapter-extensions-swipe:${Versions.fastadapter}"
        const val ui = "com.mikepenz:fastadapter-extensions-ui:${Versions.fastadapter}"
        const val utils = "com.mikepenz:fastadapter-extensions-utils:${Versions.fastadapter}"
    }

    object Groupie {
        const val core = "com.xwray:groupie:${Versions.groupie}"
        const val ktx = "com.xwray:groupie-kotlin-android-extensions:${Versions.groupie}"
    }

    object Retrofit {
        const val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
        const val rxJava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    }

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"

    object Koin {
        const val core = "org.koin:koin-core:${Versions.koin}"
        const val ext = "org.koin:koin-core-ext:${Versions.koin}"
        const val test = "org.koin:koin-test:${Versions.koin}"
        const val android = "org.koin:koin-android:${Versions.koin}"
        const val androidScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    }

    const val mvRx = "com.airbnb.android:mvrx:${Versions.mvRx}"

    object Mobius {
        const val core = "com.spotify.mobius:mobius-core:${Versions.mobius}"
        const val test = "com.spotify.mobius:mobius-test:${Versions.mobius}"
        const val rx = "com.spotify.mobius:mobius-rx2:${Versions.mobius}"
        const val android = "com.spotify.mobius:mobius-android:${Versions.mobius}"
        const val extras = "com.spotify.mobius:mobius-extras:${Versions.mobius}"
    }

    object RxBinding {
        const val core = "com.jakewharton.rxbinding3:rxbinding-core:${Versions.rxBinding}"
        const val appCompat = "com.jakewharton.rxbinding3:rxbinding-appcompat:${Versions.rxBinding}"
        const val leanBack = "com.jakewharton.rxbinding3:rxbinding-leanback:${Versions.rxBinding}"
        const val recycler = "com.jakewharton.rxbinding3:rxbinding-recyclerview:${Versions.rxBinding}"
        const val viewPager = "com.jakewharton.rxbinding3:rxbinding-viewpager:${Versions.rxBinding}"
        const val material = "com.jakewharton.rxbinding3:rxbinding-material:${Versions.rxBinding}"
    }

    object Conductor {
        const val core = "com.bluelinelabs:conductor:${Versions.conductor}"
        const val support = "com.bluelinelabs:conductor-support:${Versions.conductor}"
        const val rx2 = "com.bluelinelabs:conductor-rxlifecycle2:${Versions.conductor}"
        const val lifecycle = "com.bluelinelabs:conductor-archlifecycle:${Versions.conductor}"
    }

    object Navigation {
        const val fragment = "android.arch.navigation:navigation-fragment-ktx:1.0.0-rc02"
        const val ui = "android.arch.navigation:navigation-ui-ktx:1.0.0-rc02"
    }

    object Firebase {
        const val core = "com.google.firebase:firebase-core:17.1.0"
        const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.10.1"
    }

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    const val leakSentry = "com.squareup.leakcanary:leaksentry:${Versions.leakCanary}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val materialDialogs = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}"

    const val junit = "junit:junit:${Versions.junit}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}
