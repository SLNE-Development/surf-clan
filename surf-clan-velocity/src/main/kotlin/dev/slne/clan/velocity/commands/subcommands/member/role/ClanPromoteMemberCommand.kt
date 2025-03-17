package dev.slne.clan.velocity.commands.subcommands.member.role

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.buildMessageAsync
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

class ClanPromoteMemberCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("promote") {
    init {
        withPermission("surf.clan.promote")

        clanMemberArgument(clanService, clanPlayerService)

        playerExecutor { player, args ->
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
                    member.playerOrNull?.realName() ?: Component.text(
                        memberName,
                        Colors.VARIABLE_VALUE
                    )

                if (!clan.hasPermission(player, ClanPermission.PROMOTE)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du hast keine Berechtigung, den Spieler ",
                                Colors.ERROR
                            )
                        )
                        append(memberNameComponent)
                        append(Component.text(" im Clan ", Colors.ERROR))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(" zu befördern.", Colors.ERROR))
                    })

                    return@launch
                }

                if (member.uuid == player.uniqueId) {
                    player.sendMessage(buildMessage {
                        append(
                            Component.text(
                                "Du kannst dich nicht selbst befördern.",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId)
                    ?: error("Player not found")
                val clanPlayerMember = clan.getMember(clanPlayer)

                if (clanPlayerMember != null && member.role >= clanPlayerMember.role) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du kannst keinen Spieler befördern, der den selben oder einen höheren Rang hat.",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                if (!member.role.hasNextRole()) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Der Spieler ", Colors.ERROR))
                        append(memberNameComponent)
                        append(
                            Component.text(
                                " hat bereits die höchste Rolle im Clan ",
                                Colors.ERROR
                            )
                        )
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(".", Colors.ERROR))
                    })

                    return@launch
                }

                val oldRole = member.role
                val newRole = member.role.nextRole()

                member.role = newRole

                val memberPromotedMessage = buildMessage {
                    append(Component.text("Der Spieler ", Colors.INFO))
                    append(memberNameComponent)
                    append(Component.text(" wurde durch ", Colors.INFO))
                    append(player.realName())
                    append(Component.text(" von ", Colors.INFO))
                    append(oldRole.displayName)
                    append(Component.text(" zu ", Colors.INFO))
                    append(newRole.displayName)
                    append(Component.text(" befördert.", Colors.INFO))
                }

                clanService.saveClan(clan)

                clan.members.forEach { clanMember ->
                    clanMember.playerOrNull?.sendMessage(memberPromotedMessage)
                }
            }
        }
    }
}