import dev.slne.surf.api.gradle.util.registerRequired
import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule
import dev.slne.surf.api.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.api.gradle.paper-plugin")
    id("dev.slne.surf.microservice")
}

surfMicroservice {
    withRabbitModule(RabbitModule.CLIENT_API)
}

surfPaperPluginApi {
    withCorePaper()
    withSurfRedis()
    mainClass("dev.slne.clan.paper.PaperMain")

    foliaSupported(true)

    serverDependencies {
        registerSoft("MiniPlaceholders")
        registerSoft("surf-chat-paper")
        registerRequired("surf-transaction-paper")
    }

    authors.addAll(listOf("twisti"))
}

dependencies {
    api(projects.surfClanCore.surfClanCoreClient)
    compileOnly("dev.slne.surf.chat:surf-chat-api:+")
    compileOnly("dev.slne.surf.transaction:surf-transaction-api:4.+")
}
