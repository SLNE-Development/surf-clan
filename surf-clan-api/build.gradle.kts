import dev.slne.surf.surfapi.gradle.util.slneReleases

plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

publishing {
    repositories {
        slneReleases()
    }
}

dependencies {
    api("dev.slne.surf.bitmap:surf-bitmap-provider-common:2.1.3-SNAPSHOT")
}