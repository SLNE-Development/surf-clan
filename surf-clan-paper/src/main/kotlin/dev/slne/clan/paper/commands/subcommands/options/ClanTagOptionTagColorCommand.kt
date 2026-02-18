package dev.slne.clan.paper.commands.subcommands.options

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.surfapi.bukkit.api.command.executors.playerExecutorSuspend
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import it.unimi.dsi.fastutil.chars.CharList
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import net.kyori.adventure.text.format.TextColor
import kotlin.random.Random

fun CommandAPICommand.clanTagColorCommand() = subcommand("tagcolor") {
    withPermission(ClanPermissions.CLAN_OPTIONS_TAG_COLOR_COMMAND)

    textArgument("hex") {
        val allowedChars = CharList.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F',
            'a', 'b', 'c', 'd', 'e', 'f',
            '#'
        )

        replaceSuggestions { info, builder ->
            val raw = info.currentArg.trim()
            val startsWithHex = raw.startsWith(TextColor.HEX_PREFIX)

            val prefix = raw.removePrefix(TextColor.HEX_PREFIX)
                .trim()
                .uppercase()

            if (prefix.any { it !in allowedChars }) return@replaceSuggestions builder.buildFuture()
            if (prefix.length > 6) return@replaceSuggestions builder.buildFuture()

            if (prefix.length == 6) {
                val suggestion = if (startsWithHex) TextColor.HEX_PREFIX + prefix else prefix
                builder.suggest(suggestion)
                return@replaceSuggestions builder.buildFuture()
            }

            val restLength = 6 - prefix.length
            val max = 1 shl (4 * restLength)

            val out = ObjectLinkedOpenHashSet<String>(5)
            var tries = 0
            val maxTries = 5_000
            while (out.size < 5 && tries++ < maxTries) {
                val rest = Random.nextInt(max)
                    .toString(16)
                    .uppercase()
                    .padStart(restLength, '0')

                val hex = prefix + rest
                out.add(if (startsWithHex) TextColor.HEX_PREFIX + hex else hex)
            }

            out.forEach(builder::suggest)
            builder.buildFuture()
        }
    }

    playerExecutorSuspend { player, args ->
        val hex: String by args
        val correctedHex = if (!hex.startsWith(TextColor.HEX_PREFIX)) TextColor.HEX_PREFIX + hex else hex
        val color =
            TextColor.fromHexString(correctedHex) ?: throw CommandAPI.failWithString("Gebe eine gültige Hex-Farbe an.")

        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.OPTIONS_TAG_COLOR)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, die Farbe des Clan-Tags zu ändern.")
        }

        clan.setClanTagColor(color)

        player.sendText {
            appendSuccessPrefix()
            success("Die Farbe des Clan-Tags wurde erfolgreich auf ")
            variableValue(correctedHex)
            success(" gesetzt.")
            success(" Es kann ein paar Minuten dauern, bis die Änderung Netzwerkweit sichtbar ist.")
        }
    }
}