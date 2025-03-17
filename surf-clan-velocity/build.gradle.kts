plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

dependencies {
    api(project(":surf-clan-core"))

    compileOnly("com.github.NEZNAMY:TAB-API:5.0.7")
}