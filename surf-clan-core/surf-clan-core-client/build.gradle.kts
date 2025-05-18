plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    api(project(":surf-clan-api:surf-clan-api-client"))
    api(project(":surf-clan-core:surf-clan-core-common"))
}