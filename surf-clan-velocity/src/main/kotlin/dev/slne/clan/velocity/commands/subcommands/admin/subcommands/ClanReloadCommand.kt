package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.core.config.ClanConfig
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun CommandAPICommand.clanReloadCommand() = subcommand("reload") {
    withPermission(ClanPermissions.CLAN_ADMIN_RELOAD_COMMAND)

    anyExecutor { source, args ->
        ClanConfig.reloadFromFile()

        source.sendText {
            appendPrefix()
            success("Die Clan-Konfiguration wurde neu geladen.")
        }
    }
}