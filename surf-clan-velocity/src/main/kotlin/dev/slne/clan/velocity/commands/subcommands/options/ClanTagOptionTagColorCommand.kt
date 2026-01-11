package dev.slne.clan.velocity.commands.subcommands.options

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.random
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import net.kyori.adventure.text.format.TextColor

fun CommandAPICommand.clanTagColorCommand() = subcommand("tagcolor") {
    withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_COMMAND)

    stringArgument("hex") {
        replaceSuggestions { info, builder ->
            val raw = info.currentInput.trim()
            val startsWithHex = raw.startsWith(TextColor.HEX_PREFIX)
            val prefix = raw.removePrefix(TextColor.HEX_PREFIX).uppercase()

            if (prefix.length > 6) return@replaceSuggestions builder.buildFuture()

            val out = ObjectLinkedOpenHashSet<String>(10)
            var tries = 0
            val maxTries = 5_000

            while (out.size < 10 && tries++ < maxTries) {
                val hex = "%06X".format(random.nextInt(0xFFFFFF))
                if (hex.startsWith(prefix)) {
                    if (startsWithHex) {
                        out.add(TextColor.HEX_PREFIX + hex)
                    } else {
                        out.add(hex)
                    }
                }
            }

            for (string in out) {
                builder.suggest(string)
            }

            builder.buildFuture()
        }
    }

    playerExecutorSuspend { player, args ->
        val hex: String by args
        val correctedHex = if (!hex.startsWith(TextColor.HEX_PREFIX)) TextColor.HEX_PREFIX + hex else hex
        val color = TextColor.fromHexString(hex) ?: throw CommandAPI.failWithString("Gebe eine gültige Hex-Farbe an.")

        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.OPTIONS_TAG_COLOR)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, die Farbe des Clan-Tags zu ändern.")
        }

        clan.setClanTagColor(color)

        player.sendText {
            appendPrefix()
            success("Die Farbe des Clan-Tags wurde erfolgreich auf ")
            variableValue(correctedHex)
            success(" gesetzt.")
            success(" Es kann ein paar Minuten dauern, bis die Änderung Netzwerkweit sichtbar ist.")
        }
    }
}