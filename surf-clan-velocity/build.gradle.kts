import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule

plugins {
    id("dev.slne.surf.api.gradle.velocity")
    id("dev.slne.surf.microservice")
}

surfVelocityApi {
    withCoreVelocity()
    withSurfRedis()
}

surfMicroservice {
    withRabbitModule(RabbitModule.CLIENT_API)
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityMain"

    pluginDependencies {
//        register("miniplaceholders")
    }

    authors = listOf("Ammo", "red", "twisti")
}

dependencies {
    api(projects.surfClanCore.surfClanCoreClient)
}