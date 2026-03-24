import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule
import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
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
    }

    authors.addAll(listOf("twisti"))
}

dependencies {
//    api(projects.surfClanCore.surfClanCoreClient)
}
