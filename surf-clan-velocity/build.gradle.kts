plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
    `common-conventions`
}

tasks.shadowJar {
    exclude("kotlin/**")
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