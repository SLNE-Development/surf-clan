package dev.slne.clan.paper.commands.arguments

import com.github.shynixn.mccoroutine.folia.scope
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.slne.clan.paper.plugin
import kotlinx.coroutines.future.future
import org.bukkit.command.CommandSender

/**
 * Suggests the names [provider] returns for the clan tag typed into the [clanNode] argument before,
 * narrowed to the ones that start with what is typed.
 */
internal fun clanScopedSuggestions(
    clanNode: String,
    provider: suspend (tag: String?) -> Collection<String>
) = ArgumentSuggestions<CommandSender> { info, builder ->
    plugin.scope.future {
        provider(info.previousArgs().getRaw(clanNode))
            .filter { it.startsWith(info.currentArg(), ignoreCase = true) }
            .forEach { builder.suggest(it) }

        builder.build()
    }
}
