package dev.slne.clan.minestom.command.arguments

import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.argument.Argument
import dev.slne.surf.api.minestom.command.argument.CustomArgument
import dev.slne.surf.api.minestom.command.argument.StringArgument
import dev.slne.surf.api.minestom.command.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.memberNamesOfTag

/**
 * The name or uuid of a member of the clan named by the [clanNode] argument, suggested from that clan.
 *
 * Unlike [ClanMemberArgument], this does not resolve the member: the admin commands resolve it within
 * the chosen clan.
 */
class ClanMemberNameArgument(nodeName: String, clanNode: String) :
    CustomArgument<String, String>(StringArgument(nodeName), { info -> info.currentInput }) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            memberNamesOfTag(info.previousArgs.getRaw(clanNode))
        })
    }
}

inline fun CommandAPICommand.clanMemberNameArgument(
    nodeName: String,
    clanNode: String,
    block: Argument<String>.() -> Unit = {}
): CommandAPICommand = withArguments(ClanMemberNameArgument(nodeName, clanNode).apply(block))
