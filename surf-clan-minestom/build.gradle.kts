import dev.slne.surf.api.gradle.util.slneReleases
import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule

plugins {
    id("dev.slne.surf.api.gradle.minestom")
    id("dev.slne.surf.microservice")
}

surfMinestomApi {
    withCoreMinestom()
    withSurfRedis()
}

minestomPluginFile {
    main = "dev.slne.clan.minestom.ClanMinestomPlugin"
    authors = listOf("twisti")

    pluginDependencies {
        register("surf-rabbitmq-minestom")
        register("surf-redis-minestom")
        register("surf-chat-minestom")
    }
}

surfMicroservice {
    withRabbitModule(RabbitModule.CLIENT_API)
}

dependencies {
    api(projects.surfClanCore.surfClanCoreClient)
    compileOnly("dev.slne.surf.chat:surf-chat-api:+")
}

publishing {
    repositories {
        slneReleases()
    }
}
