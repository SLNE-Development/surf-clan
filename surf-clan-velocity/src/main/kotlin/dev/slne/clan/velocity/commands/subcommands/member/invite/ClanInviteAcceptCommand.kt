package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.*
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanInviteAcceptCommand(clanService: ClanService) : CommandAPICommand("accept") {
    init {
//        withPermission("surf.clan.invite.accept")

        clanInviteArgument(clanService)

        executesPlayer(PlayerCommandExecutor { player, args ->
            val clanName = args.getOrDefaultUnchecked("clan", "")
            val invite = ClanInviteArgument.clanInvite(clanService, player, args)

            val playerClan = player.findClan(clanService)

            if (playerClan != null) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du bist bereits im Clan ", COLOR_ERROR))
                    append(clanComponent(playerClan))
                    append(
                        Component.text(
                            " und kannst keine weiteren Einladungen annehmen.",
                            COLOR_ERROR
                        )
                    )
                })

                return@PlayerCommandExecutor
            }

            if (invite == null) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du hast keine Einladung zum Clan ", COLOR_ERROR))
                    append(Component.text(clanName, COLOR_VARIABLE))
                    append(Component.text(" erhalten.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val invitedClan = (invite as CoreClanInvite).clan

            invitedClan.uninvite(player.uniqueId)
            invitedClan.addMember(player.uniqueId, ClanMemberRole.MEMBER, invite.invitedByUuid)

            invitedClan.members.forEach { member ->
                val memberPlayer = member.playerOrNull ?: return@forEach

                memberPlayer.sendMessage(buildMessage {
                    append(Component.text("Der Spieler ", COLOR_INFO))
                    append(player.realName())
                    append(Component.text(" ist dem Clan ", COLOR_INFO))
                    append(clanComponent(invitedClan))
                    append(Component.text(" beigetreten.", COLOR_INFO))
                })
            }

            plugin.container.launch {
                clanService.saveClan(invitedClan)

                player.sendMessage(buildMessage {
                    append(Component.text("Du bist dem Clan ", COLOR_SUCCESS))
                    append(clanComponent(invitedClan))
                    append(Component.text(" beigetreten.", COLOR_SUCCESS))
                })
            }
        })
    }
}