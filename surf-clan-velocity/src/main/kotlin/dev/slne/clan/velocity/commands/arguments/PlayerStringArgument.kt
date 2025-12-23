package dev.slne.clan.velocity.commands.arguments

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.velocity.plugin

class PlayerStringArgument(nodeName: String = "target") : StringArgument(nodeName) {
    init {
        replaceSuggestions(ArgumentSuggestions.strings { _ ->
            plugin.server.allPlayers.map { it.username }
                .toTypedArray()
        })
    }

    companion object {
        fun player(args: CommandArguments, nodeName: String = "target"): String =
            args.getOrDefaultUnchecked(nodeName, "")
    }
}

inline fun CommandAPICommand.playerStringArgument(
    nodeName: String = "target",
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    PlayerStringArgument(nodeName).setOptional(optional).apply(block)
)
