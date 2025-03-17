plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    api(project(":surf-clan-api"))

    implementation(libs.surf.database)
}