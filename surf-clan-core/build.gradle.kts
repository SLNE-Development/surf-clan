plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    api(project(":surf-clan-api"))
    api("dev.slne.surf:surf-redis:1.0.0-SNAPSHOT")
}