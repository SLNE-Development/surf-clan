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
    api("org.springframework.boot:spring-boot-starter")
    api("com.fasterxml.jackson.core:jackson-core")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("jakarta.persistence:jakarta.persistence-api")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    compileOnly("com.github.NEZNAMY:TAB-API:5.0.4")
}

configurations {
    runtimeClasspath {
        exclude(group = "org.reactivestreams", module = "reactive-streams")
    }
}