package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.commands.arguments.ClanMemberRoleArgument
import dev.slne.clan.paper.commands.failCommand
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.command.args.awaitingOrNull
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.adminAdd
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.paper.command.argument.surfOfflinePlayerArgument

fun CommandAPICommand.clanAdminAddCommand() = subcommand("add") {
    withPermission(ClanPermissions.CLAN_ADMIN_ADD_COMMAND)

    argument(ClanByClanTagArgument("clan"))
    surfOfflinePlayerArgument("player")
    optionalArgument(ClanMemberRoleArgument("role"))

    anyExecutorSuspend { sender, args ->
        val clan = args.awaiting<Clan>("clan")
        val player = args.awaitingOrNull<SurfPlayer?>("player") ?: failCommand(Messages.PLAYER_NOT_FOUND)
        val role = args.awaitingOrNull<ClanMemberRole>("role") ?: ClanMemberRole.MEMBER
        val playerName = player.lastKnownName ?: player.uuid.toString()

        sender.sendClanAdminResult(adminAdd(sender.clanAdminActor(), clan, player.uuid, playerName, role))
    }
}
