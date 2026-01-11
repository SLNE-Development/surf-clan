plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

surfCoreApi {
    withSurfRedis()
    withSurfDatabaseR2dbc("1.0.0-SNAPSHOT", "dev.slne.surf.clan.libs")
}

dependencies {
    api(project(":surf-clan-core"))
}