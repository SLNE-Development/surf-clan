package dev.slne.clan.paper.commands.arguments

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.common.surfCoreApi
import dev.slne.surf.surfapi.bukkit.api.command.args.SuspendCustomArgument
import kotlinx.coroutines.CoroutineScope

class OfflinePlayerArgument(nodeName: String) : SuspendCustomArgument<SurfPlayer, String>(StringArgument(nodeName)) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { _ ->
            surfCoreApi.getOnlinePlayers().mapNotNull { it.lastKnownName }
        })
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): SurfPlayer {
        val playerName = info.currentInput
        val player = surfCoreApi.getOfflinePlayer(playerName)

        return player ?: throw CommandAPI.failWithString("Player '$playerName' not found.")
    }
}