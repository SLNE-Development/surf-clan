plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.4+")
    }
}

include("surf-clan-api:surf-clan-api-common")
include("surf-clan-api:surf-clan-api-server")
include("surf-clan-api:surf-clan-api-client")

include("surf-clan-core:surf-clan-core-client")
include("surf-clan-core:surf-clan-core-common")

include("surf-clan-paper")
include("surf-clan-velocity")
include("surf-clan-standalone")
