package dev.slne.clan.minestom.command.subcommands.member.role

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.minestom.command.arguments.clanMemberArgument
import dev.slne.clan.minestom.command.resolveClanMember
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
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
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (!clan.isMember(member.uuid)) {
                CommandAPI.failWithString(Messages.PLAYER_NOT_IN_YOUR_CLAN)
            }

            if (member.uuid == player.uuid) {
                CommandAPI.failWithString(CANNOT_DEMOTE_SELF)
            }

            if (!clan.hasMemberPermission(player.uuid, ClanPermission.DEMOTE)) {
                CommandAPI.failWithString(NO_DEMOTE_PERMISSION)
            }

            val executorMember = clan.getMember(player.uuid)
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (member.role >= executorMember.role) {
                CommandAPI.failWithString(CANNOT_DEMOTE_SAME_OR_HIGHER_ROLE)
            }

            if (!member.role.hasPreviousRole()) {
                CommandAPI.failWithString(ALREADY_LOWEST_ROLE)
            }

            val oldRole = member.role
            val newRole = member.role.previousRole()
            val changedRole = member.changeRole(newRole)

            if (!changedRole) {
                CommandAPI.failWithString(ROLE_CHANGE_FAILED)
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
