package dev.slne.clan.paper.commands.arguments.color

import dev.jorel.commandapi.arguments.ArgumentSuggestions
import kotlin.random.Random

object HexColorSuggestions {
    fun <S> create(): ArgumentSuggestions<S> = ArgumentSuggestions { info, builder ->
        val raw = info.currentArg.trim().removePrefix("#")

        if (raw.length > 8) return@ArgumentSuggestions builder.buildFuture()
        if (!raw.all { it.isDigit() || it.uppercaseChar() in 'A'..'F' })
            return@ArgumentSuggestions builder.buildFuture()

        val targetLen = if (raw.length < 6) 6 else 8
        val restLength = targetLen - raw.length
        val max = 1 shl (4 * restLength)

        repeat(5) {
            val rest = Random.nextInt(max)
                .toString(16)
                .uppercase()
                .padStart(restLength, '0')

            builder.suggest("#$raw$rest")
        }

        builder.buildFuture()
    }
}