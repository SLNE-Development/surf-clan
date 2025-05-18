plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnlyApi(platform(libs.surf.cloud.bom))
    compileOnlyApi(libs.surf.cloud.api.common)
    compileOnlyApi(libs.bitmaps)
}