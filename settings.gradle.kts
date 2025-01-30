plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "surf-clan"

include("surf-clan-core")
include("surf-clan-api")
include("surf-clan-velocity")
