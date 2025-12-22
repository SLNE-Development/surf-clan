package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.util.clan
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.Component

class ClanAcceptCommand : CommandAPICommand("accept") {
    init {
        withPermission("surf.clan.invite.accept")
        clanInviteArgument()

        playerExecutor { player, args ->
            plugin.container.launch {
                val clanName = args.getUnchecked<String>("clan") ?: ""
                val invite = ClanInviteArgument.clanInvite(player, args)

                val playerClan = player.findClan()

                if (playerClan != null) {
                    player.sendMessage(buildText {
                        append(Component.text("Du bist bereits im Clan ", Colors.ERROR))
                        append(clanComponent(playerClan))
                        append(
                            Component.text(
                                " und kannst keine weiteren Einladungen annehmen.",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                if (invite == null) {
                    player.sendMessage(buildText {
                        append(Component.text("Du hast keine Einladung zum Clan ", Colors.ERROR))
                        append(Component.text(clanName, Colors.VARIABLE_VALUE))
                        append(Component.text(" erhalten.", Colors.ERROR))
                    })

                    return@launch
                }

                val invitedClan = invite.clan

                invitedClan.uninvite(player.uniqueId)
                invitedClan.addMember(player.uniqueId, ClanMemberRole.MEMBER, invite.invitedByUuid)

                invitedClan.members.forEach { member ->
                    val memberPlayer = member.playerOrNull ?: return@forEach

                    memberPlayer.sendMessage(buildText {
                        append(Component.text("Der Spieler ", Colors.INFO))
                        append(player.realName())
                        append(Component.text(" ist dem Clan ", Colors.INFO))
                        append(clanComponent(invitedClan))
                        append(Component.text(" beigetreten.", Colors.INFO))
                    })
                }

                clanService.saveClan(invitedClan)
            }
        }
    }
}