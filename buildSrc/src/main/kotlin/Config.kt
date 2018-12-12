object ApplicationID {
    val default = "com.alefimenko.iuttimetable"
    val develop = "com.alefimenko.iuttimetable.develop"
}

object Versions {
    val minSdk = 19
    val targetSdk = 28
    val compileSdk = 28
    val appVersionCode = 3000
    val appVersionName = "2.2.0"

    val kotlin = "1.3.11"
    val androidPlugin = "3.4.0-alpha07"

    val aac = "2.0.0"
    val room = "2.1.0-alpha02"
    var fastadapter = "3.3.1"
    val retrofit = "2.4.0"
    val gson = "2.8.5"
    val dagger = "2.11"
    val timber = "4.7.1"
    val materialDialogs = "2.0.0-rc3"
    val junit = "4.12"
    val testRunner = "1.1.0"
    val espresso = "3.1.0"
}

object Deps {
    val appcompat = "androidx.appcompat:appcompat:1.0.2"
    val androidxCore = "androidx.core:core-ktx:1.0.1"
    val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
    val material = "com.google.android.material:material:1.0.0"
    val constraint = "androidx.constraintlayout:constraintlayout:1.1.3"

    val multidex = "com.android.support:multidex:1.0.3"

    val databindingCompiler = "com.android.databinding:compiler:${Versions.androidPlugin}"

    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.aac}"
    val lifecycleReactive = "androidx.lifecycle:lifecycle-reactivestreams:${Versions.aac}"
    val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.aac}"

    val room = "androidx.room:room-runtime:${Versions.room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"

    val fastAdapterCore = "com.mikepenz:fastadapter:${Versions.fastadapter}"
    val fastAdapterCommons = "com.mikepenz:fastadapter-commons:${Versions.fastadapter}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val gson = "com.google.code.gson:gson:${Versions.gson}"

    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val materialDialogs = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}"

    val junit = "junit:junit:${Versions.junit}"
    val testRunner = "androidx.test:runner:${Versions.testRunner}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}