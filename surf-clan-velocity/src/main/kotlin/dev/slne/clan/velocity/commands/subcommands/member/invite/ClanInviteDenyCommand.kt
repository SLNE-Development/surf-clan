package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.Component

class ClanInviteDenyCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("deny") {
    init {
        withPermission("surf.clan.invite.deny")

        clanInviteArgument(clanService)

        executesPlayer(PlayerCommandExecutor { player, args ->
            val clanName = args.getOrDefaultUnchecked("clan", "")
            val invite = ClanInviteArgument.clanInvite(clanService, player, args)

            if (invite == null) {
                player.sendMessage(buildText {
                    append(Component.text("Du hast keine Einladung zum Clan ", Colors.ERROR))
                    append(Component.text(clanName, Colors.VARIABLE_VALUE))
                    append(Component.text(" erhalten.", Colors.ERROR))
                })

                return@PlayerCommandExecutor
            }

            val invitedClan = (invite as CoreClanInvite).clan

            invitedClan.uninvite(player.uniqueId)

            plugin.container.launch {
                clanService.saveClan(invitedClan)

                invite.invitedBy?.let { invitedBy ->
                    invitedBy.playerOrNull?.sendMessage(buildText {
                        append(Component.text("Der Spieler ", Colors.INFO))
                        append(player.realName())
                        append(Component.text(" hat deine Einladung zum Clan ", Colors.INFO))
                        append(clanComponent(invitedClan, clanPlayerService))
                        append(Component.text(" abgelehnt.", Colors.INFO))
                    })
                }

                player.sendMessage(buildText {
                    append(Component.text("Du hast die Einladung zum Clan ", Colors.SUCCESS))
                    append(clanComponent(invitedClan, clanPlayerService))
                    append(Component.text(" abgelehnt.", Colors.SUCCESS))
                })
            }
        })
    }
}