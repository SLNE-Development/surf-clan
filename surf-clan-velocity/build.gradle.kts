plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityMain"

    pluginDependencies {
        register("miniplaceholders")
    }
}

dependencies {
    api(project(":surf-clan-core"))
    runtimeOnly(project(":surf-clan-fallback"))
}