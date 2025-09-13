plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    withCloudClientPaper()
    mainClass("dev.slne.surf.clan.paper.PaperMain")
    bootstrapper("dev.slne.surf.clan.paper.PaperBootstrap")
}

dependencies {
    api(project(":surf-clan-core:surf-clan-core-client"))
}