package dev.slne.clan.minestom.command.subcommands.member

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

fun CommandAPICommand.clanKickMemberCommand(): CommandAPICommand = withSubcommand(
    subcommand("kick") {
        withPermission(ClanPermissions.CLAN_KICK_MEMBER_COMMAND)

        clanMemberArgument("member")

        playerExecutorSuspend { player, args ->
            val member = args.resolveClanMember("member")
            val clan = Clan.byPlayer(player.uuid)
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (player.uuid == member.uuid) {
                CommandAPI.failWithString(CANNOT_KICK_SELF)
            }

            if (!clan.hasMemberPermission(player.uuid, ClanPermission.KICK)) {
                CommandAPI.failWithString(NO_KICK_PERMISSION)
            }

            val executorMember = clan.getMember(player.uuid)
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (member.role >= executorMember.role) {
                CommandAPI.failWithString(CANNOT_KICK_SAME_OR_HIGHER_ROLE)
            }

            val removed = clan.removeMember(member)

            if (!removed) {
                CommandAPI.failWithString(KICK_FAILED)
            }

            val memberName = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()

            clan.broadcast(memberKickedMessage(memberName, player.username))
        }
    }
)
