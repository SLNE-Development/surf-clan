plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityClanPlugin"
}

dependencies {
    api(project(":surf-clan-core"))

    compileOnly("com.github.NEZNAMY:TAB-API:5.0.7")
}