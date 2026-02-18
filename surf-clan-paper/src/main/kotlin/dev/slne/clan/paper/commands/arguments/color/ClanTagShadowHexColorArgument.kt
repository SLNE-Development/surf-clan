package dev.slne.clan.paper.commands.arguments.color

import net.kyori.adventure.text.format.ShadowColor

class ClanTagShadowHexColorArgument(nodeName: String) :
    BaseHexColorArgument<ShadowColor>(nodeName, ShadowColor::fromHexString)