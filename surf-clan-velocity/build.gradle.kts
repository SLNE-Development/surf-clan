plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

surfVelocityApi {
    withCoreVelocity()
    withSurfRedis()
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityMain"

    pluginDependencies {
//        register("miniplaceholders")
    }

    authors = listOf("Ammo", "red", "twisti")
}

dependencies {
    api(project(":surf-clan-core"))
    runtimeOnly(project(":surf-clan-runtime"))
}