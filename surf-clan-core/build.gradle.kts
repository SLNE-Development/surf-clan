plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

surfVelocityApi {
    withSurfRedis()
    withCoreCommon()
}

dependencies {
    api(project(":surf-clan-api"))
}