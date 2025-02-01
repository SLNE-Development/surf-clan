package dev.slne.clan.velocity.commands.subcommands.member

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.buildMessageAsync
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanMemberArgument
import dev.slne.clan.velocity.commands.arguments.clanMemberArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component

class ClanKickMemberCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("kick") {
    init {
        withPermission("surf.clan.kick")

        clanMemberArgument(clanService, clanPlayerService)

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val memberName = args[0] as String
                val clan = player.findClan(clanService)

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                val member = ClanMemberArgument.clanMember(clanPlayerService, clan, args)

                if (member == null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Der Spieler ", Colors.ERROR))
                        append(Component.text(memberName, Colors.VARIABLE_VALUE))
                        append(Component.text(" ist nicht im Clan ", Colors.ERROR))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(".", Colors.ERROR))
                    })

                    return@launch
                }

                val memberNameComponent =
                    member.playerOrNull?.realName() ?: Component.text(memberName, Colors.VARIABLE_VALUE)

                if (!clan.hasPermission(player, ClanPermission.KICK)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du hast keine Berechtigung, den Spieler ",
                                Colors.ERROR
                            )
                        )
                        append(memberNameComponent)
                        append(Component.text(" aus dem Clan ", Colors.ERROR))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(" zu entfernen.", Colors.ERROR))
                    })

                    return@launch
                }

                if (member.uuid == player.uniqueId) {
                    player.sendMessage(buildMessage {
                        append(
                            Component.text(
                                "Du kannst dich nicht selbst rauswerfen.",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId) ?: error("Player not found")
                val clanPlayerMember = clan.getMember(clanPlayer)

                if (clanPlayerMember != null && member.role >= clanPlayerMember.role) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Du kannst keine Spieler mit der selben oder einer höheren Rolle rauswerfen.", Colors.ERROR))
                    })

                    return@launch
                }

                val memberKickedMessage = buildMessageAsync {
                    append(Component.text("Der Spieler ", Colors.INFO))
                    append(memberNameComponent)
                    append(Component.text(" wurde von ", Colors.INFO))
                    append(player.realName())
                    append(Component.text(" aus dem Clan ", Colors.INFO))
                    append(clanComponent(clan, clanPlayerService))
                    append(Component.text(" entfernt.", Colors.INFO))
                }

                clan.removeMember(member)
                clanService.saveClan(clan)

                clan.members.forEach { clanMember ->
                    clanMember.playerOrNull?.sendMessage(memberKickedMessage)
                }

                member.playerOrNull?.sendMessage(memberKickedMessage)
            }
        })
    }
}