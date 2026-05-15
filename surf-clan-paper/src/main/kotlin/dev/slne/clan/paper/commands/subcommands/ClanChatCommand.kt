package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.clickSuggestsCommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.args.asyncSignedMessageArgument
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.api.paper.util.getPrefixedName
import dev.slne.surf.chat.api.SurfChatApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.kyori.adventure.chat.SignedMessage
import org.bukkit.entity.Player

class ClanChatCommand(name: String) : CommandAPICommand(name) {
    init {
        withPermission(ClanPermissions.CLAN_CHAT_COMMAND)

        asyncSignedMessageArgument("message")

        playerExecutorSuspend { player, arguments ->
            val playerClan = Clan.byPlayer(player.uniqueId)
            val message = arguments.awaiting<SignedMessage>("message")

            if (playerClan == null) {
                player.sendText {
                    appendErrorPrefix()
                    error("Du bist in keinem Clan.")
                }
                return@playerExecutorSuspend
            }


            coroutineScope {
                playerClan.members.forEach { member ->
                    launch {
                        SurfChatApi.sendSignedMessage(
                            message,
                            player.uniqueId,
                            member.uuid,
                            formatClanChatMessage(message, player)
                        )
                    }
                }
            }
        }
    }


    private fun formatClanChatMessage(message: SignedMessage, sender: Player) = buildText {
        darkSpacer(">>")
        appendSpace()
        primary("Clan")
        appendSpace()
        darkSpacer("|")
        appendSpace()
        append(sender.getPrefixedName())
        spacer(":")
        appendSpace()
        white(message.message())
        clickSuggestsCommand("/clan chat ")
    }
}