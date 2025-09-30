import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("dev.slne.surf.surfapi.gradle.velocity")
}

surfVelocityApi {
    withCloudClientVelocity()
}

dependencies {
    api(project(":surf-clan-core:surf-clan-core-client"))
}

tasks.withType<ShadowJar> {
    destinationDirectory.set(rootProject.file("output"))
}