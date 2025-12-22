import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

velocityPluginFile {
    main = "dev.slne.clan.velocity.VelocityClanPlugin"

    pluginDependencies {
        register("miniplaceholders")
    }
}

dependencies {
    api(project(":surf-clan-core"))
    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.3.0")

    runtimeOnly(project(":surf-clan-fallback"))
}