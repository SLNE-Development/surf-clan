package dev.slne.clan.minestom.command.subcommands.options

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.minestom.command.arguments.color.clanTagHexColorArgument
import dev.slne.clan.minestom.command.arguments.color.clanTagShadowHexColorArgument
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.NO_TAG_COLOR_PERMISSION
import dev.slne.surf.clan.core.client.command.clanTagColorChangedMessage
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.entity.Player

fun CommandAPICommand.clanTagColorCommand(): CommandAPICommand = withSubcommand(
    subcommand("tagcolor") {
        withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_COMMAND)

        withSubcommand(subcommand("foreground") {
            withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_FOREGROUND_COMMAND)
            clanTagHexColorArgument("color")
            playerExecutorSuspend { player, args ->
                val color: TextColor by args
                changeColor(player, ClanTagColor.update { foreground(color) })
            }
        })

        withSubcommand(subcommand("background") {
            withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_BACKGROUND_COMMAND)
            clanTagHexColorArgument("color")
            playerExecutorSuspend { player, args ->
                val color: TextColor by args
                changeColor(player, ClanTagColor.update { background(color) })
            }
        })

        withSubcommand(subcommand("shadow") {
            withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_SHADOW_COMMAND)
            clanTagShadowHexColorArgument("color")
            playerExecutorSuspend { player, args ->
                val color: ShadowColor by args
                changeColor(player, ClanTagColor.update { shadow(color) })
            }
        })
    }
)

private suspend fun changeColor(sender: Player, update: ClanTagColor.Update) {
    val clan = Clan.byPlayer(sender.uuid)
        ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

    if (!clan.hasMemberPermission(sender.uuid, ClanPermission.OPTIONS_TAG_COLOR)) {
        CommandAPI.failWithString(NO_TAG_COLOR_PERMISSION)
    }

    clan.changeClanTagColor(update)

    sender.sendMessage(clanTagColorChangedMessage())
}
