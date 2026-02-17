plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnly(project(":surf-clan-api"))
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:3.1.0")
}