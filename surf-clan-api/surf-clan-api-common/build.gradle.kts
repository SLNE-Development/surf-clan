plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnlyApi(libs.surf.cloud.api.common)
    compileOnlyApi(libs.bitmaps)
}