plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-clan-core:surf-clan-core-client"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.cloud.bukkit.BukkitMain")
    authors.add("Ammo")
    generateLibraryLoader(false)
}