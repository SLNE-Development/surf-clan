plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

//allOpen {
//    annotation("jakarta.persistence.Entity")
//    annotation("jakarta.persistence.MappedSuperclass")
//    annotation("jakarta.persistence.Embeddable")
//}

dependencies {
    api(project(":surf-clan-api"))

    compileOnlyApi("dev.slne:surf-data-api:1.21.4+")
}