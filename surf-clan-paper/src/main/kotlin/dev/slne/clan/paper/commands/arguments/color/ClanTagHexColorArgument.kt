package dev.slne.clan.paper.commands.arguments.color

import net.kyori.adventure.text.format.TextColor

class ClanTagHexColorArgument(nodeName: String) :
    BaseHexColorArgument<TextColor>(nodeName, 6, {
        TextColor.fromHexString("#$it")
    })