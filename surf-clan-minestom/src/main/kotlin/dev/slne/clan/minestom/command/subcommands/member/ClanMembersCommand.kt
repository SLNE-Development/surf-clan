package dev.slne.clan.minestom.command.subcommands.member

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.minestom.command.arguments.clanByClanTagArgument
import dev.slne.clan.minestom.command.resolveClanArgumentOrOwn
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.command.clanMembersPagination
import dev.slne.surf.clan.core.client.command.collectClanMemberData
import dev.slne.surf.clan.core.client.permission.ClanPermissions

private const val CLAN_NODE = "clan"

fun CommandAPICommand.clanMembersCommand(): CommandAPICommand = withSubcommand(
    subcommand("members") {
        withPermission(ClanPermissions.CLAN_VIEW_MEMBERS_COMMAND)

        clanByClanTagArgument(CLAN_NODE, optional = true)

        playerExecutorSuspend { player, args ->
            val clan: Clan = args.resolveClanArgumentOrOwn(CLAN_NODE, player.uuid)

            val data = collectClanMemberData(clan)
            val pagination = clanMembersPagination(clan as ClanImpl)

            player.sendMessage(pagination.renderComponent(data))
        }
    }
)
