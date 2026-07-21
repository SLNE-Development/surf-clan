import dev.slne.surf.microservice.gradle.plugin.rabbit.RabbitModule

plugins {
    id("dev.slne.surf.api.gradle.core")
    id("dev.slne.surf.microservice")
}

surfCoreApi {
    withSurfRedis()
    withSurfDatabaseR2dbc("2.3.1", "dev.slne.surf.clan.libs.db")
}

surfMicroservice {
    withRabbitModule(RabbitModule.SERVER_API, true)
    withMicroserviceApi()
}

dependencies {
    implementation(projects.surfClanCore)
}
