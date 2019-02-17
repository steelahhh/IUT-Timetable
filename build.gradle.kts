import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

buildscript {

    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidPlugin}")
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
    }
}

allprojects {
    val ktlint = configurations.create("ktlint")

    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://jitpack.io/")
        }
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
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = "org.jetbrains.kotlin.android.extensions")