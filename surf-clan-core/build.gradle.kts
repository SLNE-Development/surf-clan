import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule

plugins {
    id("dev.slne.surf.surfapi.gradle.core")
    id("dev.slne.surf.microservice")
}

surfMicroservice {
    withRabbitModule(RabbitModule.CLIENT_API)
}

surfCoreApi {
    withSurfRedis()
}

dependencies {
    api(projects.surfClanApi)
    api(projects.surfClanMicroserviceProtocol)
}