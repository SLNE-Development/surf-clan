plugins {
    id("dev.slne.surf.surfapi.gradle.core")
    `common-conventions`

    kotlin("plugin.serialization")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    api(project(":surf-clan-api"))

    compileOnlyApi(libs.kaml)

    compileOnlyApi("org.springframework.boot:spring-boot-starter")
    compileOnlyApi("com.fasterxml.jackson.core:jackson-core")
    compileOnlyApi("com.fasterxml.jackson.core:jackson-databind")
    compileOnlyApi("jakarta.persistence:jakarta.persistence-api")
    compileOnlyApi("org.springframework.boot:spring-boot-starter-data-jpa")
}