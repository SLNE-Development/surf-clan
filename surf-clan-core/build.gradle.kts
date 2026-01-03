plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

surfVelocityApi {
    withSurfRedis()
}

dependencies {
    api(project(":surf-clan-api"))
}