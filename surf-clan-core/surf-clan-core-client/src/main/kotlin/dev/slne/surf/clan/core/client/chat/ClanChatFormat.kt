package dev.slne.surf.clan.core.client.chat

import dev.slne.surf.api.core.luckperms.LuckPermsAccess
import dev.slne.surf.api.core.luckperms.prefix
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.minimessage.miniMessage
import net.kyori.adventure.text.Component
import java.util.*

/**
 * Builds a MiniMessage component containing the player's LuckPerms prefix and [name].
 */
fun prefixedName(uuid: UUID, name: String): Component =
    miniMessage.deserialize("${LuckPermsAccess.getUser(uuid)?.prefix ?: ""}$name")

/**
 * Renders [message] the way clan chat shows it, attributed to [senderName].
 */
fun formatClanChatMessage(message: String, senderName: Component) = buildText {
    darkSpacer(">>")
    appendSpace()
    primary("Clan")
    appendSpace()
    darkSpacer("|")
    appendSpace()
    append(senderName)
    spacer(":")
    appendSpace()
    white(message)
    clickSuggestsCommand("/clan chat ")
}
