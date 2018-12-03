import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.androidPlugin}")
        classpath(kotlin("gradle-plugin", version = Versions.kotlin))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}