package dev.slne.clan.paper.commands.subcommands.options

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.commands.arguments.color.ClanTagHexColorArgument
import dev.slne.clan.paper.commands.arguments.color.ClanTagShadowHexColorArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.playerExecutorSuspend
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

fun CommandAPICommand.clanTagColorCommand() = subcommand("tagcolor") {
    withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_COMMAND)

    subcommand("foreground") {
        withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_FOREGROUND_COMMAND)
        argument(ClanTagHexColorArgument("color"))
        playerExecutorSuspend { player, args ->
            val color: TextColor by args
            changeColor(player, ClanTagColor.update { foreground(color) })
        }
    }

    subcommand("background") {
        withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_BACKGROUND_COMMAND)
        argument(ClanTagHexColorArgument("color"))
        playerExecutorSuspend { player, args ->
            val color: TextColor by args
            changeColor(player, ClanTagColor.update { background(color) })
        }
    }

    subcommand("shadow") {
        withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_SHADOW_COMMAND)
        argument(ClanTagShadowHexColorArgument("color"))
        playerExecutorSuspend { player, args ->
            val color: ShadowColor by args
            changeColor(player, ClanTagColor.update { shadow(color) })
        }
    }
}

private suspend fun changeColor(sender: Player, update: ClanTagColor.Update) {
    val clan =
        Clan.byPlayer(sender.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

    if (!clan.hasMemberPermission(sender.uniqueId, ClanPermission.OPTIONS_TAG_COLOR)) {
        throw CommandAPI.failWithString("Du hast keine Berechtigung, die Farbe des Clan-Tags zu ändern.")
    }

    clan.changeClanTagColor(update)

    sender.sendText {
        appendSuccessPrefix()
        success("Die Farbe des Clan-Tags wurde erfolgreich geändert.")
        success(" Es kann ein paar Minuten dauern, bis die Änderung Netzwerkweit sichtbar ist.")
    }
}