package dev.slne.clan.paper.commands.subcommands.member

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaitingOrNull
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.clanMembersPagination
import dev.slne.surf.clan.core.client.command.collectClanMemberData

fun CommandAPICommand.clanMembersCommand() = subcommand("members") {
    withPermission(ClanPermissions.CLAN_VIEW_MEMBERS_COMMAND)

    optionalArgument(ClanByClanTagArgument("clan"))

    playerExecutorSuspend { player, args ->
        val clan = args.awaitingOrNull<Clan>("clan") ?: run {
            Clan.byPlayer(player.uniqueId)
                ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)
        }

        val data = collectClanMemberData(clan)
        val pagination = clanMembersPagination(clan as ClanImpl)

        player.sendMessage(pagination.renderComponent(data))
    }
}
