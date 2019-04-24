@file:Suppress("unused")

object ApplicationID {
    const val default = "com.alefimenko.iuttimetable"
    const val develop = "com.alefimenko.iuttimetable.develop"
}

object Modules {
    const val app = ":app"

    const val common = ":common"
}

object Versions {
    const val minSdk = 19
    const val targetSdk = 28
    const val compileSdk = 28
    const val appVersionCode = 3000
    const val appVersionName = "2.2.0"

    const val kotlin = "1.3.30"
    const val androidPlugin = "3.4.0"

    const val mviCore = "1.1.6"
    const val aac = "2.0.0"

    const val rxBinding = "3.0.0-alpha2"
    const val rxKotlin = "2.2.0"
    const val rxAndroid = "2.0.2"
    const val room = "2.1.0-alpha02"

    const val conductor = "3.0.0-rc1"

    const val leakCanary = "1.6.3"

    const val klock = "1.4.0"

    const val jsoup = "1.11.3"
    const val fastadapter = "3.3.1"
    const val retrofit = "2.4.0"
    const val gson = "2.8.5"
    const val koin = "1.0.2"
    const val timber = "4.7.1"
    const val materialDialogs = "2.7.0"
    const val junit = "4.12"
    const val testRunner = "1.1.0"
    const val espresso = "3.1.0"
}

object Deps {
    val vers = Versions

    const val appcompat = "androidx.appcompat:appcompat:1.0.2"
    const val androidxCore = "androidx.core:core-ktx:1.0.1"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
    const val material = "com.google.android.material:material:1.1.0-alpha04"
    const val constraint = "androidx.constraintlayout:constraintlayout:1.1.3"

    const val multidex = "com.android.support:multidex:1.0.3"

    const val databindingCompiler = "com.android.databinding:compiler:${vers.androidPlugin}"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${vers.aac}"
    const val lifecycleReactive = "androidx.lifecycle:lifecycle-reactivestreams:${vers.aac}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${vers.aac}"

    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${vers.rxKotlin}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${vers.rxAndroid}"

    const val room = "androidx.room:room-runtime:${vers.room}"
    const val roomCompiler = "androidx.room:room-compiler:${vers.room}"
    const val roomRxJava = "androidx.room:room-rxjava2:${vers.room}"

    val klock = Klock

    object Klock {
        const val core = "com.soywiz:klock-metadata:${vers.klock}"
        const val jvm = "com.soywiz:klock-jvm:${vers.klock}"
        const val android = "com.soywiz:klock-android:${vers.klock}"
    }

    const val fastAdapterCore = "com.mikepenz:fastadapter:${vers.fastadapter}"
    const val fastAdapterCommons = "com.mikepenz:fastadapter-commons:${vers.fastadapter}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${vers.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${vers.retrofit}"
    const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${vers.retrofit}"
    const val gson = "com.google.code.gson:gson:${vers.gson}"

    const val jsoup = "org.jsoup:jsoup:${vers.jsoup}"

    val koin = Koin

    object Koin {
        const val core = "org.koin:koin-core:${vers.koin}"
        const val ext = "org.koin:koin-core-ext:${vers.koin}"
        const val test = "org.koin:koin-test:${vers.koin}"
        const val android = "org.koin:koin-android:${vers.koin}"
        const val androidScope = "org.koin:koin-androidx-scope:${vers.koin}"
    }

    val rxBinding = RxBinding

    object RxBinding {
        const val core = "com.jakewharton.rxbinding3:rxbinding-core:${vers.rxBinding}"
        const val appCompat = "com.jakewharton.rxbinding3:rxbinding-appcompat:${vers.rxBinding}"
        const val leanBack = "com.jakewharton.rxbinding3:rxbinding-leanback:${vers.rxBinding}"
        const val recycler = "com.jakewharton.rxbinding3:rxbinding-recyclerview:${vers.rxBinding}"
        const val viewPager = "com.jakewharton.rxbinding3:rxbinding-viewpager:${vers.rxBinding}"
        const val material = "com.jakewharton.rxbinding3:rxbinding-material:${vers.rxBinding}"
    }

    val mviCore = MviCore

    object MviCore {
        const val core = "com.github.badoo.mvicore:mvicore:${vers.mviCore}"
        const val android = "com.github.badoo.mvicore:mvicore-android:${vers.mviCore}"
    }

    val conductor = Conductor

    object Conductor {
        const val core = "com.bluelinelabs:conductor:${vers.conductor}"
        const val support = "com.bluelinelabs:conductor-support:${vers.conductor}"
        const val rx2 = "com.bluelinelabs:conductor-rxlifecycle2:${vers.conductor}"
        const val lifecycle = "com.bluelinelabs:conductor-archlifecycle:${vers.conductor}"
    }

    val navigation = Navigation

    object Navigation {
        const val fragment = "android.arch.navigation:navigation-fragment-ktx:1.0.0-rc02"
        const val ui = "android.arch.navigation:navigation-ui-ktx:1.0.0-rc02"
    }

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${vers.leakCanary}"
    const val leakCanaryNoOp = "com.squareup.leakcanary:leakcanary-android-no-op:${vers.leakCanary}"

    const val timber = "com.jakewharton.timber:timber:${vers.timber}"
    const val materialDialogs = "com.afollestad.material-dialogs:core:${vers.materialDialogs}"

    const val junit = "junit:junit:${vers.junit}"
    const val testRunner = "androidx.test:runner:${vers.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${vers.espresso}"
}
