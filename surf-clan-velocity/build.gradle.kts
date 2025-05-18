plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    api(project(":surf-clan-core:surf-clan-core-client"))
}

//surfVelocityPluginApi {
//    mainClass("dev.slne.surf.cloud.bukkit.BukkitMain")
//    authors.add("Ammo")
//}