package dev.slne.clan.paper.commands.arguments.color

import dev.slne.surf.clan.core.client.command.CLAN_TAG_COLOR_LENGTH
import net.kyori.adventure.text.format.TextColor

class ClanTagHexColorArgument(nodeName: String) :
    BaseHexColorArgument<TextColor>(nodeName, CLAN_TAG_COLOR_LENGTH, {
        TextColor.fromHexString("#$it")
    })
