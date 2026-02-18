import dev.slne.surf.surfapi.gradle.util.slneReleases

plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    api("dev.slne.surf.bitmap:surf-bitmap-provider-common:2.1.3-SNAPSHOT")
}

publishing {
    repositories {
        slneReleases()
    }
}