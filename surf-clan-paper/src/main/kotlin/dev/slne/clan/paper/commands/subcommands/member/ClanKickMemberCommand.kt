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
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend

fun CommandAPICommand.clanKickMemberCommand() = subcommand("kick") {
    withPermission(ClanPermissions.CLAN_KICK_MEMBER_COMMAND)

    argument(ClanMemberArgument("member"))

    playerExecutorSuspend { player, args ->
        val member = args.awaiting<ClanMember>("member")
        val clan = Clan.byPlayer(player.uniqueId)
            ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (player.uniqueId == member.uuid) {
            throw CommandAPI.failWithString("Du kannst dich nicht selbst rauswerfen.")
        }

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.KICK)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, diesen Spieler aus dem Clan zu entfernen.")
        }

        val executorMember = clan.getMember(player.uniqueId)
            ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (member.role >= executorMember.role) {
            throw CommandAPI.failWithString("Du kannst keine Spieler mit der selben oder einer höheren Rolle rauswerfen.")
        }

        val removed = clan.removeMember(member)

        if (!removed) {
            throw CommandAPI.failWithString("Fehler beim Entfernen des Spielers aus dem Clan.")
        }

        val memberName = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()

        val message = buildText {
            appendInfoPrefix()
            variableValue(memberName)
            info(" wurde von ")
            variableValue(player.name)
            info(" aus dem Clan entfernt.")
        }

        clan.broadcast(message)
    }
}