package dev.slne.clan.paper.commands.arguments.color

import dev.slne.surf.clan.core.client.command.CLAN_TAG_SHADOW_COLOR_LENGTH
import net.kyori.adventure.text.format.ShadowColor

class ClanTagShadowHexColorArgument(nodeName: String) :
    BaseHexColorArgument<ShadowColor>(nodeName, CLAN_TAG_SHADOW_COLOR_LENGTH, {
        ShadowColor.fromHexString("#$it")
    })
