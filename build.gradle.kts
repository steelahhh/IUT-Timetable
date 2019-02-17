buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidPlugin}")
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.0.0-RC13")
    }
}

allprojects {
    val ktlint = configurations.create("ktlint")

    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io/")
        maven(url = "https://plugins.gradle.org/m2/")
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