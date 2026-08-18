package dev.slne.clan.paper.commands.subcommands.member

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.commands.arguments.ClanMemberArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*

fun CommandAPICommand.clanKickMemberCommand() = subcommand("kick") {
    withPermission(ClanPermissions.CLAN_KICK_MEMBER_COMMAND)

    argument(ClanMemberArgument("member"))

    playerExecutorSuspend { player, args ->
        val member = args.awaiting<ClanMember>("member")
        val clan = Clan.byPlayer(player.uniqueId)
            ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)

        if (player.uniqueId == member.uuid) {
            throw CommandAPI.failWithString(CANNOT_KICK_SELF)
        }

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.KICK)) {
            throw CommandAPI.failWithString(NO_KICK_PERMISSION)
        }

        val executorMember = clan.getMember(player.uniqueId)
            ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)

        if (member.role >= executorMember.role) {
            throw CommandAPI.failWithString(CANNOT_KICK_SAME_OR_HIGHER_ROLE)
        }

        val removed = clan.removeMember(member)

        if (!removed) {
            throw CommandAPI.failWithString(KICK_FAILED)
        }

        val memberName = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()

        clan.broadcast(memberKickedMessage(memberName, player.name))
    }
}
