package dev.slne.clan.paper.commands.subcommands.member.role

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.commands.arguments.ClanMemberArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.playerExecutorSuspend
import dev.slne.surf.surfapi.core.api.command.args.awaiting
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService

fun CommandAPICommand.clanDemoteMemberCommand() = subcommand("demote") {
    withPermission(ClanPermissions.CLAN_DEMOTE_MEMBER_COMMAND)

    argument(ClanMemberArgument("member"))

    playerExecutorSuspend { player, args ->
        val member = args.awaiting<ClanMember>("member")
        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.isMember(member.uuid)) {
            throw CommandAPI.failWithString("Der Spieler ist nicht in deinem Clan.")
        }

        if (member.uuid == player.uniqueId) {
            throw CommandAPI.failWithString("Du kannst dich nicht selbst degradieren.")
        }

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.DEMOTE)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, diesen Spieler zu degradieren.")
        }

        val executorMember = clan.getMember(player.uniqueId)
            ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (member.role >= executorMember.role) {
            throw CommandAPI.failWithString("Du kannst keinen Spieler degradieren, der den selben oder einen höheren Rang hat.")
        }

        if (!member.role.hasPreviousRole()) {
            throw CommandAPI.failWithString("Der Spieler hat bereits den niedrigsten Rang.")
        }

        val oldRole = member.role
        val newRole = member.role.previousRole()
        val changedRole = member.changeRole(newRole)

        if (!changedRole) {
            throw CommandAPI.failWithString("Fehler beim Ändern der Rolle des Spielers.")
        }

        val memberName = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()

        clan.broadcast(buildText {
            appendInfoPrefix()
            variableValue(memberName)
            info(" wurde durch ")
            variableValue(player.name)
            info(" von ")
            append(oldRole)
            info(" zu ")
            append(newRole)
            info(" degradiert.")
        })
    }
}