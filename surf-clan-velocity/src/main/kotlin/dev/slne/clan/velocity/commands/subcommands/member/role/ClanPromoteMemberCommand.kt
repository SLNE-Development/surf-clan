package dev.slne.clan.velocity.commands.subcommands.member.role

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.VelocityMain.Companion.redisApi
import dev.slne.clan.velocity.commands.arguments.ClanMemberArgument
import dev.slne.clan.velocity.commands.arguments.clanMemberArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.redis.event.ClanBroadcastRedisEvent
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component

class ClanPromoteMemberCommand : CommandAPICommand("promote") {
    init {
        withPermission("surf.clan.promote")

        clanMemberArgument()

        playerExecutor { player, args ->
            plugin.container.launch {
                val memberName = args[0] as String
                val clan = player.findClan()

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                val member = ClanMemberArgument.clanMember(clan, args)

                if (member == null) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler ist nicht in deinem Clan.")
                    }

                    return@launch
                }

                val memberNameComponent =
                    member.playerOrNull?.realName() ?: Component.text(
                        memberName,
                        Colors.VARIABLE_VALUE
                    )

                if (!clan.hasPermission(player, ClanPermission.PROMOTE)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, diesen Spieler zu befördern.")
                    }

                    return@launch
                }

                if (member.uuid == player.uniqueId) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst dich nicht selbst befördern.")
                    }

                    return@launch
                }

                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId)
                    ?: error("Player not found")
                val clanPlayerMember = clan.getMember(clanPlayer)

                if (clanPlayerMember != null && member.role >= clanPlayerMember.role) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst keinen Spieler befördern, der den selben oder einen höheren Rang hat.")
                    }

                    return@launch
                }

                if (!member.role.hasNextRole()) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler hat bereits die höchste Rolle im Clan.")
                    }

                    return@launch
                }

                val oldRole = member.role
                val newRole = member.role.nextRole()

                member.role = newRole

                val memberPromotedMessage = buildText {
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

                redisApi.publishEvent(
                    ClanBroadcastRedisEvent(
                        clan.name,
                        memberPromotedMessage
                    )
                )
            }
        }
    }
}