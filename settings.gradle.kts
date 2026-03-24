pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.slne.surf.surfapi.gradle.settings") version "1.21.11+"
}

rootProject.name = "surf-clan"

include("surf-clan-api")
include("surf-clan-core")
include("surf-clan-placeholder-expansion")
include("surf-clan-paper")
include("surf-clan-velocity")
include("surf-clan-microservice")

include("surf-clan-microservice-protocol")
include("surf-clan-core:surf-clan-core-client")