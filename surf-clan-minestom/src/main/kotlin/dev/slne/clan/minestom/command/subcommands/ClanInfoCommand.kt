package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.minestom.command.arguments.clanByClanTagArgument
import dev.slne.clan.minestom.command.resolveClanArgumentOrOwn
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.clan.core.client.permission.ClanPermissions

private const val CLAN_TAG_NODE = "clanTag"

fun CommandAPICommand.clanInfoCommand(): CommandAPICommand = withSubcommand(
    subcommand("info") {
        withPermission(ClanPermissions.CLAN_INFO_COMMAND)

        clanByClanTagArgument(CLAN_TAG_NODE, optional = true)

        playerExecutorSuspend { player, args ->
            val clan: Clan = args.resolveClanArgumentOrOwn(CLAN_TAG_NODE, player.uuid)

            player.sendMessage(Components.Clan.renderClanInformation(clan as ClanImpl))
        }
    }
)
