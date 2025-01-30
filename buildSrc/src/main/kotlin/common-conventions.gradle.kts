dependencies {
    "implementation"(platform("org.springframework.boot:spring-boot-dependencies:3.3.6"))

    "compileOnly"("org.springframework.boot:spring-boot-configuration-processor:3.3.6")
    "kapt"("org.springframework.boot:spring-boot-configuration-processor:3.3.6")
}

group = "dev.slne.surf.clan"
version = findProperty("version") as String? ?: "1.0.0-SNAPSHOT"