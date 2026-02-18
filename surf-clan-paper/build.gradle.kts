import dev.slne.surf.surfapi.gradle.util.registerSoft

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    withCorePaper()
    withSurfRedis()
    mainClass("dev.slne.clan.paper.PaperMain")

    serverDependencies {
        registerSoft("MiniPlaceholders")
    }

    authors.addAll(listOf("twisti"))
}

dependencies {
    api(project(":surf-clan-core"))
    runtimeOnly(project(":surf-clan-runtime"))
}