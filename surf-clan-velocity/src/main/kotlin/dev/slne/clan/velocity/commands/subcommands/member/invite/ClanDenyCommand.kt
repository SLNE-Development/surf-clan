package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.util.clan
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanDenyCommand : CommandAPICommand("deny") {
    init {
        withPermission("surf.clan.invite.deny")

        clanInviteArgument()

        playerExecutor { player, args ->
            val invite = ClanInviteArgument.clanInvite(player, args)

            if (invite == null) {
                player.sendText {
                    appendPrefix()
                    error("Du hast keine Einladung zu diesem Clan erhalten.")
                }
                return@playerExecutor
            }

            val invitedClan = invite.clan
            invitedClan.uninvite(player.uniqueId)

            plugin.container.launch {
                clanService.saveClan(invitedClan)

                invite.invitedByUuid.let { invitedBy ->
                    invitedBy.playerOrNull?.sendText {
                        info("Der Spieler ")
                        variableValue(player.username)
                        info(" hat deine Einladung zum Clan ")
                        clanComponent(invitedClan)
                        info(" abgelehnt.")
                    }
                }

                player.sendText {
                    appendPrefix()
                    success("Du hast die Einladung abgelehnt.")
                }
            }
        }
    }
}