package dev.slne.clan.minestom.command.subcommands.member.role

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.minestom.command.arguments.clanMemberArgument
import dev.slne.clan.minestom.command.failCommand
import dev.slne.clan.minestom.command.resolveClanMember
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanDemoteMemberCommand(): CommandAPICommand = withSubcommand(
    subcommand("demote") {
        withPermission(ClanPermissions.CLAN_DEMOTE_MEMBER_COMMAND)

        clanMemberArgument("member")

        playerExecutorSuspend { player, args ->
            val member = args.resolveClanMember("member")
            val clan = Clan.byPlayer(player.uuid)
                ?: failCommand(Messages.NOT_IN_CLAN)

            if (!clan.isMember(member.uuid)) {
                failCommand(Messages.PLAYER_NOT_IN_YOUR_CLAN)
            }

            if (member.uuid == player.uuid) {
                failCommand(CANNOT_DEMOTE_SELF)
            }

            if (!clan.hasMemberPermission(player.uuid, ClanPermission.DEMOTE)) {
                failCommand(NO_DEMOTE_PERMISSION)
            }

            val executorMember = clan.getMember(player.uuid)
                ?: failCommand(Messages.NOT_IN_CLAN)

            if (member.role >= executorMember.role) {
                failCommand(CANNOT_DEMOTE_SAME_OR_HIGHER_ROLE)
            }

            if (!member.role.hasPreviousRole()) {
                failCommand(ALREADY_LOWEST_ROLE)
            }

            val oldRole = member.role
            val newRole = member.role.previousRole()
            val changedRole = member.changeRole(newRole)

            if (!changedRole) {
                failCommand(ROLE_CHANGE_FAILED)
            }

            val memberName = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()

            clan.broadcast(
                memberRoleChangedMessage(
                    memberName,
                    player.username,
                    oldRole,
                    newRole,
                    RoleChange.DEMOTED
                )
            )
        }
    }
)
