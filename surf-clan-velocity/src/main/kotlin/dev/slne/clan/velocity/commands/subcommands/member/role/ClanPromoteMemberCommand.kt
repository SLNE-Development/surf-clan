package dev.slne.clan.velocity.commands.subcommands.member.role

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanMemberArgument
import dev.slne.clan.velocity.commands.arguments.clanMemberArgument
import dev.slne.clan.velocity.extensions.*
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanPromoteMemberCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("promote") {
    init {
        withPermission("surf.clan.promote")

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
                        append(Component.text("Der Spieler ", COLOR_ERROR))
                        append(Component.text(memberName, COLOR_VARIABLE))
                        append(Component.text(" ist nicht im Clan ", COLOR_ERROR))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(".", COLOR_ERROR))
                    })

                    return@launch
                }

                if (member.uuid == player.uniqueId) {
                    player.sendMessage(buildMessage {
                        append(
                            Component.text(
                                "Du kannst dich nicht selbst befördern.",
                                COLOR_ERROR
                            )
                        )
                    })

                    return@launch
                }

                val memberNameComponent =
                    member.playerOrNull?.realName() ?: Component.text(memberName, COLOR_VARIABLE)

                if (!clan.hasPermission(player, ClanPermission.PROMOTE)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du hast keine Berechtigung, den Spieler ",
                                COLOR_ERROR
                            )
                        )
                        append(memberNameComponent)
                        append(Component.text(" im Clan ", COLOR_ERROR))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(" zu befördern.", COLOR_ERROR))
                    })

                    return@launch
                }

                if (!member.role.hasNextRole()) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Der Spieler ", COLOR_ERROR))
                        append(memberNameComponent)
                        append(
                            Component.text(
                                " hat bereits die höchste Rolle im Clan ",
                                COLOR_ERROR
                            )
                        )
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(".", COLOR_ERROR))
                    })

                    return@launch
                }

                val oldRole = member.role
                val newRole = member.role.nextRole()

                member.role = newRole

                val memberPromotedMessage = buildMessage {
                    append(Component.text("Der Spieler ", COLOR_INFO))
                    append(memberNameComponent)
                    append(Component.text(" wurde durch ", COLOR_INFO))
                    append(player.realName())
                    append(Component.text(" von ", COLOR_INFO))
                    append(oldRole.displayName)
                    append(Component.text(" zu ", COLOR_INFO))
                    append(newRole.displayName)
                    append(Component.text(" befördert.", COLOR_INFO))
                }

                clanService.saveClan(clan)

                clan.members.forEach { clanMember ->
                    clanMember.player.sendMessage(memberPromotedMessage)
                }
            }
        })
    }
}