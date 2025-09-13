plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

surfVelocityApi {
    withCloudClientVelocity()
}

dependencies {
    api(project(":surf-clan-core:surf-clan-core-client"))
}