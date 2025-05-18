import dev.slne.surf.surfapi.gradle.util.slnePublic

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.4+")
    }
}

plugins {
    java
}

allprojects {
    afterEvaluate {
        apply(plugin = "java")

        repositories {
            slnePublic()
        }

        dependencies {
            implementation(platform(libs.surf.cloud.bom))
        }
    }
}