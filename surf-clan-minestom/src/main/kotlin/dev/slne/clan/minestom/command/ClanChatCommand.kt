package dev.slne.clan.minestom.command

import dev.slne.clan.api.clan.Clan
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.signedMessageArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.chat.api.SurfChatApi
import dev.slne.surf.chat.api.message.MessageData
import dev.slne.surf.chat.api.message.MessageType
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.chat.formatClanChatMessage
import dev.slne.surf.clan.core.client.chat.prefixedName
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import dev.slne.surf.core.api.common.server.SurfServer
import kotlinx.coroutines.launch
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.text.Component
import java.time.OffsetDateTime
import java.util.*

fun clanChatCommand(name: String) = subcommand(name) {
    withPermission(ClanPermissions.CLAN_CHAT_COMMAND)

    signedMessageArgument("message")

    playerExecutorSuspend { player, arguments ->
        val playerClan = Clan.byPlayer(player.uuid)
        val message = arguments.get<SignedMessage>("message")

        if (playerClan == null) {
            player.sendText {
                appendErrorPrefix()
                error(Messages.NOT_IN_CLAN)
            }
            return@playerExecutorSuspend
        }

        val formattedMessage =
            formatClanChatMessage(message.message(), prefixedName(player.uuid, player.username))

        playerClan.members.forEach { member ->
            launch {
                SurfChatApi.sendSignedMessage(
                    message,
                    player.uuid,
                    member.uuid,
                    formattedMessage
                )
            }
        }

        val messageData = MessageData(
            message = Component.text(message.message()),
            messageUuid = UUID.randomUUID(),
            sender = player.uuid,
            receiver = null,
            sentAt = OffsetDateTime.now(),
            server = SurfServer.current().name,
            signature = message.signature(),
            type = MessageType("CLAN_CHAT")
        )

        SurfChatApi.logMessage(messageData)
        SurfChatApi.passAutoMod(messageData)
    }
}
