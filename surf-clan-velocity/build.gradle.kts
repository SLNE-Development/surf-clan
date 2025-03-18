plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityClanPlugin"

    pluginDependencies {
        register("surf-api-velocity")
        register("surf-data-velocity")
        register("commandapi")
        register("tab")
    }
}

dependencies {
    api(project(":surf-clan-core"))
    api(libs.kaml)
    compileOnly("com.github.NEZNAMY:TAB-API:5.0.4")
}

configurations {
    runtimeClasspath {
        exclude(group = "org.reactivestreams", module = "reactive-streams")
    }
}