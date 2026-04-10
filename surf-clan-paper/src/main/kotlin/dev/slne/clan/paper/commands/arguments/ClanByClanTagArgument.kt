package dev.slne.clan.paper.commands.arguments

import com.github.shynixn.mccoroutine.folia.scope
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.plugin
import dev.slne.surf.api.paper.command.args.SuspendCustomArgument
import dev.slne.surf.clan.core.clan.CoreClanService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.bukkit.command.CommandSender

class ClanByClanTagArgument(nodeName: String) :
    SuspendCustomArgument<Clan, String>(StringArgument(nodeName)) {
    companion object {
        private val suggestions = ArgumentSuggestions<CommandSender> { info, builder ->
            plugin.scope.future {
                val tag = info.currentArg
                for (tag in CoreClanService.computeTagSuggestions(tag)) {
                    builder.suggest(tag)
                }
                builder.build()
            }
        }
    }

    init {
        replaceSuggestions(suggestions)
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): Clan {
        val tag = info.currentInput
        val clan =
            Clan.byTag(tag) ?: throw CommandAPI.failWithString("Unknown clan with tag '$tag'")
        return clan
    }
}