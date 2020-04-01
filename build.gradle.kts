buildscript {
    val kotlin_version by extra("1.3.71")
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.fabric.io/public")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidPlugin}")
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.0.0")
        classpath("com.google.gms:google-services:${Versions.googleServices}")
        classpath("io.fabric.tools:gradle:1.31.0")
    }
}

allprojects {
    val ktlint = configurations.create("ktlint")

    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io/")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/soywiz/soywiz")
    }

    dependencies {
        ktlint("com.github.shyiko:ktlint:0.30.0")
    }

    task<JavaExec>("ktlintCheck") {
        description = "Check Kotlin code style."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args = listOf("src/**/*.kt", "src/**/*Test.kt", "-a")
    }

    task<JavaExec>("ktlintFormat") {
        description = "Fix Kotlin code style deviations."
        classpath = ktlint
        main = "com.github.shyiko.ktlint.Main"
        args = listOf("src/**/*.kt", "src/**/*Test.kt", "-a", "-F")
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = "io.gitlab.arturbosch.detekt")
apply(plugin = "org.jetbrains.kotlin.android.extensions")
