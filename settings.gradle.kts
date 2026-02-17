plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "surf-clan"

include("surf-clan-api")
include("surf-clan-core")
include("surf-clan-velocity")
include("surf-clan-runtime")
include("surf-clan-placeholder-expansion")
include("surf-clan-paper")