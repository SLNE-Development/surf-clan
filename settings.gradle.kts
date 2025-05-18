plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "surf-clan"

include("surf-clan-api:surf-clan-api-common")
include("surf-clan-api:surf-clan-api-server")
include("surf-clan-api:surf-clan-api-client")

include("surf-clan-core:surf-clan-core-client")
include("surf-clan-core:surf-clan-core-common")

include("surf-clan-paper")
include("surf-clan-velocity")
include("surf-clan-standalone")
