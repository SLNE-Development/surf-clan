plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnlyApi(libs.surf.cloud.api.server)
    api(project(":surf-clan-api:surf-clan-api-common"))
}