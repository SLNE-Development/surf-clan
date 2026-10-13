package dev.slne.clan.minestom.command.arguments

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.argument.Argument
import dev.slne.surf.api.minestom.command.argument.CustomArgument
import dev.slne.surf.api.minestom.command.argument.StringArgument
import dev.slne.surf.api.minestom.command.suggestion.ArgumentSuggestions
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
