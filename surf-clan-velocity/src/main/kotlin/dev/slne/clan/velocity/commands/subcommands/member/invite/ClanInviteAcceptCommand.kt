package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.commands.arguments.clanInviteArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.Component

class ClanInviteAcceptCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("accept") {
    init {
        withPermission("surf.clan.invite.accept")

        clanInviteArgument(clanService)

        playerExecutor { player, args ->
            plugin.container.launch {
                val clanName = args.getUnchecked<String>("clan") ?: ""
                val invite = ClanInviteArgument.clanInvite(clanService, player, args)

                val playerClan = player.findClan(clanService)

                if (playerClan != null) {
                    player.sendMessage(buildText {
                        append(Component.text("Du bist bereits im Clan ", Colors.ERROR))
                        append(clanComponent(playerClan, clanPlayerService))
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

                val invitedClan = (invite as CoreClanInvite).clan

                invitedClan.uninvite(player.uniqueId)
                invitedClan.addMember(player.uniqueId, ClanMemberRole.MEMBER, invite.invitedByUuid)

                invitedClan.members.forEach { member ->
                    val memberPlayer = member.playerOrNull ?: return@forEach

                    memberPlayer.sendMessage(buildText {
                        append(Component.text("Der Spieler ", Colors.INFO))
                        append(player.realName())
                        append(Component.text(" ist dem Clan ", Colors.INFO))
                        append(clanComponent(invitedClan, clanPlayerService))
                        append(Component.text(" beigetreten.", Colors.INFO))
                    })
                }

                clanService.saveClan(invitedClan)
            }
        }
    }
}