package dev.slne.clan.minestom.command.arguments

import dev.slne.clan.api.clan.Clan
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.argument.Argument
import dev.slne.minestom.lobby.api.command.commandapi.argument.CustomArgument
import dev.slne.minestom.lobby.api.command.commandapi.argument.StringArgument
import dev.slne.minestom.lobby.api.command.commandapi.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.clan.CoreClanService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class ClanByClanTagArgument(nodeName: String) :
    CustomArgument<Deferred<Clan?>, String>(StringArgument(nodeName), { info ->
        val tag = info.currentInput
        clanArgumentScope.async { Clan.byTag(tag) }
    }) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            CoreClanService.computeTagSuggestions(info.currentArg)
        })
    }
}

inline fun CommandAPICommand.clanByClanTagArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<Deferred<Clan?>>.() -> Unit = {}
): CommandAPICommand =
    withArguments(ClanByClanTagArgument(nodeName).setOptional(optional).apply(block))
