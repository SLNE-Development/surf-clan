package dev.slne.clan.minestom.command.arguments

import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.argument.Argument
import dev.slne.surf.api.minestom.command.argument.CustomArgument
import dev.slne.surf.api.minestom.command.argument.StringArgument
import dev.slne.surf.api.minestom.command.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.pendingInviteeNamesOfTag

/**
 * The name or uuid of a player with a pending invitation into the clan named by the [clanNode]
 * argument, suggested from that clan's invitations.
 */
class ClanInviteeNameArgument(nodeName: String, clanNode: String) :
    CustomArgument<String, String>(StringArgument(nodeName), { info -> info.currentInput }) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            pendingInviteeNamesOfTag(info.previousArgs.getRaw(clanNode))
        })
    }
}

inline fun CommandAPICommand.clanInviteeNameArgument(
    nodeName: String,
    clanNode: String,
    block: Argument<String>.() -> Unit = {}
): CommandAPICommand = withArguments(ClanInviteeNameArgument(nodeName, clanNode).apply(block))
