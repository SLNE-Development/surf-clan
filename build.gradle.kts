import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    java
    `java-library`
    `maven-publish`
    `kotlin-dsl`

    id("net.linguica.maven-settings") version "0.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    implementation("com.github.retrooper.packetevents:spigot:2.3.0")
}

paper {
    apiVersion = "1.20.6"
    main = "dev.slne.surf.surfessentials.SurfEssentials"
    bootstrapper = "dev.slne.surf.surfessentials.SurfEssentialsBootstrap"
    loader = "dev.slne.surf.surfessentials.SurfEssentialsLoader"

    authors = listOf("twisti")
    description = "A collection of essential features for the Surf server"

    generateLibrariesJson = true
    prefix = "SurfEssentials"

    dependencies {
        serverDependencies {
            registerDepend("ProtocolLib")
            registerDepend("ProtocolSupport")
            registerDepend("ViaVersion")
            registerDepend("ViaBackwards")
            registerDepend("ViaRewind")
            registerDepend("Geyser-Spigot")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-parameters")
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    runServer {
        minecraftVersion("1.20.6")
    }

    shadowJar {
        relocate("com.github.retrooper.packetevents", "dev.slne.surf.surfessentials.libs.packetevents.api")
        relocate("io.github.retrooper.packetevents", "dev.slne.surf.surfessentials.libs.packetevents.api")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "slne-space"
            url = uri(
                System.getenv("REPOSITORY_URL") ?: "https://packages.slne.dev/maven/p/surf/maven"
            )
            credentials {
                username = System.getenv("JB_SPACE_CLIENT_ID")
                password = System.getenv("JB_SPACE_CLIENT_SECRET")
            }
        }
    }
}

/**
 * Registers a soft dependency.
 *
 * @param name The name of the dependency.
 * @param joinClassPath Whether the dependency should be joined to the classpath.
 * @param loadOrder The load order of the dependency.
 */
fun NamedDomainObjectContainerScope<PaperPluginDescription.DependencyDefinition>.registerSoft(
    name: String,
    joinClassPath: Boolean = true,
    loadOrder: PaperPluginDescription.RelativeLoadOrder = PaperPluginDescription.RelativeLoadOrder.BEFORE
) = register(name) {
    this.required = false
    this.joinClasspath = joinClassPath
    this.load = loadOrder
}

/**
 * Registers a required dependency.
 *
 * @param name The name of the dependency.
 * @param joinClassPath Whether the dependency should be joined to the classpath.
 * @param loadOrder The load order of the dependency.
 */
fun NamedDomainObjectContainerScope<PaperPluginDescription.DependencyDefinition>.registerDepend(
    name: String,
    joinClassPath: Boolean = true,
    loadOrder: PaperPluginDescription.RelativeLoadOrder = PaperPluginDescription.RelativeLoadOrder.BEFORE
) = register(name) {
    this.required = true
    this.joinClasspath = joinClassPath
    this.load = loadOrder
}

