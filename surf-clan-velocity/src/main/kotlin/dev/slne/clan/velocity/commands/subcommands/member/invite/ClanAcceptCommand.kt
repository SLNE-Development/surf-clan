package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.util.clan
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanAcceptCommand : CommandAPICommand("accept") {
    init {
        withPermission("surf.clan.invite.accept")
        clanInviteArgument()

        playerExecutor { player, args ->
            plugin.container.launch {
                val invite = ClanInviteArgument.clanInvite(player, args)

                val playerClan = player.findClan()

                if (playerClan != null) {
                    player.sendText {
                        appendPrefix()
                        error("Du bist bereits in diesem Clan und kannst keine weiteren Einladungen annehmen.")
                    }

                    return@launch
                }

                if (invite == null) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Einladung zu diesen Clan erhalten.")
                    }

                    return@launch
                }

                val invitedClan = invite.clan

                invitedClan.uninvite(player.uniqueId)
                invitedClan.addMember(player.uniqueId, ClanMemberRole.MEMBER, invite.invitedByUuid)

                invitedClan.members.forEach { member ->
                    val memberPlayer = member.playerOrNull ?: return@forEach

                    memberPlayer.sendText {
                        appendPrefix()
                        info("Der Spieler ")
                        variableValue(player.username)
                        info(" ist dem Clan ")
                        variableValue(invitedClan.name)
                        info(" beigetreten.")
                    }
                }

                clanService.saveClan(invitedClan)
            }
        }
    }
}