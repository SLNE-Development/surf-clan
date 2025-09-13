plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnlyApi(libs.surf.bitmap.common)
}

surfCoreApi {
    withCloudCommon()
}