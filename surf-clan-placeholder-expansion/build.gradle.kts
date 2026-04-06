plugins {
    id("dev.slne.surf.api.gradle.core")
}

dependencies {
    compileOnly(projects.surfClanApi)
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.1.0")
}