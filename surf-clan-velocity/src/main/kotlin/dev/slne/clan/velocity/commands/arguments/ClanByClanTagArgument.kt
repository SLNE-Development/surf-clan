package dev.slne.clan.velocity.commands.arguments

import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.velocitypowered.api.command.CommandSource
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.SuggestionInfo
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.surf.surfapi.velocity.api.command.args.SuspendCustomArgument
import kotlinx.coroutines.CoroutineScope

class ClanByClanTagArgument(nodeName: String) : SuspendCustomArgument<Clan, String>(StringArgument(nodeName)) {
    private val suggestions = object : SuspendArgumentSuggestions() {
        override suspend fun CoroutineScope.suggest(
            info: SuggestionInfo<CommandSource>,
            builder: SuggestionsBuilder
        ) {
            val tag = info.currentInput
            for (tag in CoreClanService.computeTagSuggestions(tag)) {
                builder.suggest(tag)
            }
        }
    }

    init {
        replaceSuggestions(suggestions)
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): Clan {
        val tag = info.currentInput
        val clan = Clan.byTag(tag) ?: throw CommandAPI.failWithString("Unknown clan with tag '$tag'")
        return clan
    }
}