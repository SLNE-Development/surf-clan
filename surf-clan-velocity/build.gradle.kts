plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityClanPlugin"

    pluginDependencies {
        register("surf-data-velocity")
        register("commandapi")
        register("miniplaceholders")
    }
}

dependencies {
    api(project(":surf-clan-core"))
    api(libs.kaml)
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.3.0")
}

configurations {
    runtimeClasspath {
        exclude(group = "org.reactivestreams", module = "reactive-streams")
    }
}