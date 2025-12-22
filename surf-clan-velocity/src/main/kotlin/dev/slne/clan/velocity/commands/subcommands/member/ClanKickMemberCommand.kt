package dev.slne.clan.velocity.commands.subcommands.member

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
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

class ClanKickMemberCommand : CommandAPICommand("kick") {
    init {
        withPermission("surf.clan.kick")

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

                if (!clan.hasPermission(player, ClanPermission.KICK)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, den Spieler $memberName aus dem Clan zu entfernen.")
                    }

                    return@launch
                }

                if (member.uuid == player.uniqueId) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst dich nicht selbst rauswerfen.")
                    }

                    return@launch
                }

                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId)
                    ?: error("Player not found")
                val clanPlayerMember = clan.getMember(clanPlayer)

                if (clanPlayerMember != null && member.role >= clanPlayerMember.role) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst keine Spieler mit der selben oder einer höheren Rolle rauswerfen.")
                    }

                    return@launch
                }

                val memberKickedMessage = buildText {
                    append(Component.text("Der Spieler ", Colors.INFO))
                    append(memberNameComponent)
                    append(Component.text(" wurde von ", Colors.INFO))
                    append(player.realName())
                    append(Component.text(" aus dem Clan ", Colors.INFO))
                    append(clanComponent(clan))
                    append(Component.text(" entfernt.", Colors.INFO))
                }

                clan.removeMember(member)
                clanService.saveClan(clan)

                clan.members.forEach { clanMember ->
                    clanMember.playerOrNull?.sendMessage(memberKickedMessage)
                }

                member.playerOrNull?.sendMessage(memberKickedMessage)
            }
        }
    }
}