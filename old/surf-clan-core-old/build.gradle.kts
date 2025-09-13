plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    api(project(":surf-clan-api"))
    api(libs.kaml)

    compileOnlyApi("dev.slne:surf-data-api:1.21.4+")
}