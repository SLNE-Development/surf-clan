package dev.slne.clan.core.spring

import dev.slne.clan.core.dataDirectory
import dev.slne.clan.core.spring.database.DatabaseConfig

object DatabaseConfigHolder : ConfigHolder<DatabaseConfig>(
    DatabaseConfig::class,
    dataDirectory,
    "database-config.yml"
) {
    override val defaultConfig = DatabaseConfig.default()
}