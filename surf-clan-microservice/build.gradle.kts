plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

surfCoreApi {
    withSurfRedis()
    withSurfDatabaseR2dbc("1.3.0", "dev.slne.surf.clan.libs")
}

dependencies {
//    api(projects.surfClanCore.surfClanCoreCommon)
}