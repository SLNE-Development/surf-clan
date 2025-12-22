package dev.slne.clan.velocity.commands.subcommands.member.role

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.commands.arguments.ClanMemberArgument
import dev.slne.clan.velocity.commands.arguments.clanMemberArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component

class ClanDemoteMemberCommand :
    CommandAPICommand("demote") {
    init {
        withPermission("surf.clan.demote")

        clanMemberArgument()

        playerExecutor { player, args ->
            plugin.container.launch {
                val memberName = args[0] as String
                val clan = player.findClan()

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                val member = ClanMemberArgument.clanMember(clan, args)

                if (member == null) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler ist nicht in deinem Clan.")
                    }

                    return@launch
                }

                val memberNameComponent =
                    member.playerOrNull?.realName() ?: Component.text(
                        memberName,
                        Colors.VARIABLE_VALUE
                    )

                if (!clan.hasPermission(player, ClanPermission.DEMOTE)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, diesen Spieler zu degradieren.")
                    }
                    return@launch
                }

                if (member.uuid == player.uniqueId) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst dich nicht selbst degradieren.")
                    }

                    return@launch
                }

                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId)
                    ?: error("Player not found")
                val clanPlayerMember = clan.getMember(clanPlayer)

                if (clanPlayerMember != null && member.role >= clanPlayerMember.role) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst keinen Spieler degradieren, der den selben oder einen höheren Rang hat.")
                    }

                    return@launch
                }

                if (!member.role.hasPreviousRole()) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler hat bereits den niedrigsten Rang.")
                    }

                    return@launch
                }

                val oldRole = member.role
                val newRole = member.role.previousRole()

                member.role = newRole

                val memberPromotedMessage = buildText {
                    info("Der Spieler ")
                    append(memberNameComponent)
                    info(" wurde durch ")
                    append(player.realName())
                    info(" von ")
                    append(oldRole.displayName)
                    info(" zu ")
                    append(newRole.displayName)
                    info(" degradiert.")
                }

                clanService.saveClan(clan)

                clan.members.forEach { clanMember ->
                    clanMember.playerOrNull?.sendMessage(memberPromotedMessage)
                }
            }
        }
    }
}