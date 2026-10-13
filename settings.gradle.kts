pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://reposilite.slne.dev/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.slne.surf.api.gradle.settings") version "+"
}

rootProject.name = "surf-clan"

include("surf-clan-api")
include("surf-clan-core")
include("surf-clan-core:surf-clan-core-client")
include("surf-clan-placeholder-expansion")
include("surf-clan-paper")
include("surf-clan-minestom")
include("surf-clan-velocity")
include("surf-clan-microservice")
