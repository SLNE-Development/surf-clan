package dev.slne.clan.paper.commands.subcommands.member.role

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
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

fun CommandAPICommand.clanPromoteMemberCommand() = subcommand("promote") {
    withPermission(ClanPermissions.CLAN_PROMOTE_MEMBER_COMMAND)

    arguments(ClanMemberArgument("member"))

    playerExecutorSuspend { player, args ->
        val member = args.awaiting<ClanMember>("member")
        val clan = Clan.byPlayer(player.uniqueId)
            ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)
        val isInClan = clan.isMember(member.uuid)

        if (!isInClan) {
            throw CommandAPI.failWithString(Messages.PLAYER_NOT_IN_YOUR_CLAN)
        }

        if (member.uuid == player.uniqueId) {
            throw CommandAPI.failWithString(CANNOT_PROMOTE_SELF)
        }

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.PROMOTE)) {
            throw CommandAPI.failWithString(NO_PROMOTE_PERMISSION)
        }

        val executorMember = clan.getMember(player.uniqueId)
            ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)

        if (executorMember.role <= member.role) {
            throw CommandAPI.failWithString(CANNOT_PROMOTE_SAME_OR_HIGHER_ROLE)
        }

        if (!member.role.hasNextRole()) {
            throw CommandAPI.failWithString(ALREADY_HIGHEST_ROLE)
        }

        val oldRole = member.role
        val newRole = member.role.nextRole()
        val changedRole = member.changeRole(newRole)

        if (!changedRole) {
            throw CommandAPI.failWithString(ROLE_CHANGE_FAILED)
        }

        val memberName = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()
        clan.broadcast(
            memberRoleChangedMessage(
                memberName,
                player.name,
                oldRole,
                newRole,
                RoleChange.PROMOTED
            )
        )
    }
}
