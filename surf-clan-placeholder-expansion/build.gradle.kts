plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    compileOnly(project(":surf-clan-api"))
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.0.1")
}