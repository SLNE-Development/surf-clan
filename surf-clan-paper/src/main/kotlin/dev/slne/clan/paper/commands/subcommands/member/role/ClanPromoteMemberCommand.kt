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
import dev.slne.surf.surfapi.bukkit.api.command.executors.playerExecutorSuspend
import dev.slne.surf.surfapi.core.api.command.args.awaiting
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService

fun CommandAPICommand.clanPromoteMemberCommand() = subcommand("promote") {
    withPermission(ClanPermissions.CLAN_PROMOTE_MEMBER_COMMAND)

    arguments(ClanMemberArgument("member"))

    playerExecutorSuspend { player, args ->
        val member = args.awaiting<ClanMember>("member")
        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")
        val isInClan = clan.isMember(member.uuid)

        if (!isInClan) {
            throw CommandAPI.failWithString("Der Spieler ist nicht in deinem Clan.")
        }

        if (member.uuid == player.uniqueId) {
            throw CommandAPI.failWithString("Du kannst dich nicht selbst befördern.")
        }

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.PROMOTE)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, diesen Spieler zu befördern.")
        }

        val executorMember = clan.getMember(player.uniqueId)
            ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (executorMember.role <= member.role) {
            throw CommandAPI.failWithString("Du kannst keinen Spieler befördern, der den selben oder einen höheren Rang hat.")
        }

        if (!member.role.hasNextRole()) {
            throw CommandAPI.failWithString("Der Spieler hat bereits die höchste Rolle im Clan.")
        }

        val oldRole = member.role
        val newRole = member.role.nextRole()
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
            info(" befördert.")
        })
    }
}