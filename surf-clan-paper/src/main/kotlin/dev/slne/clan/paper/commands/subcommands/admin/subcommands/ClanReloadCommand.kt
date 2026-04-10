package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.clan.core.config.ClanConfig

fun CommandAPICommand.clanReloadCommand() = subcommand("reload") {
    withPermission(ClanPermissions.CLAN_ADMIN_RELOAD_COMMAND)

    anyExecutor { source, args ->
        ClanConfig.reloadFromFile()

        source.sendText {
            appendSuccessPrefix()
            success("Die Clan-Konfiguration wurde neu geladen.")
        }
    }
}