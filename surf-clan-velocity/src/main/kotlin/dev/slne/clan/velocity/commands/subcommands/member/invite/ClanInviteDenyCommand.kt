package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.core.*
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanInviteDenyCommand(clanService: ClanService) : CommandAPICommand("deny") {
    init {
        withPermission("surf.clan.invite.deny")

        clanInviteArgument(clanService)

        executesPlayer(PlayerCommandExecutor { player, args ->
            val clanName = args.getOrDefaultUnchecked("clan", "")
            val invite = ClanInviteArgument.clanInvite(clanService, player, args)

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

            plugin.container.launch {
                clanService.saveClan(invitedClan)

                invite.invitedBy?.let { invitedBy ->
                    invitedBy.playerOrNull?.sendMessage(buildMessage {
                        append(Component.text("Der Spieler ", COLOR_INFO))
                        append(player.realName())
                        append(Component.text(" hat deine Einladung zum Clan ", COLOR_INFO))
                        append(clanComponent(invitedClan))
                        append(Component.text(" abgelehnt.", COLOR_INFO))
                    })
                }

                player.sendMessage(buildMessage {
                    append(Component.text("Du hast die Einladung zum Clan ", COLOR_SUCCESS))
                    append(clanComponent(invitedClan))
                    append(Component.text(" abgelehnt.", COLOR_SUCCESS))
                })
            }
        })
    }
}