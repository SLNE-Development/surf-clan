package dev.slne.clan.paper.commands.arguments

import com.github.shynixn.mccoroutine.folia.scope
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.paper.plugin
import dev.slne.surf.api.paper.command.args.SuspendCustomArgument
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.clanMemberNames
import dev.slne.surf.clan.core.client.command.findClanMember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class ClanMemberArgument(nodeName: String) :
    SuspendCustomArgument<ClanMember, String>(StringArgument(nodeName)) {
    companion object {
        private val suggestions = ArgumentSuggestions<CommandSender> { info, builder ->
            val player = info.sender as? Player ?: return@ArgumentSuggestions builder.buildFuture()
            plugin.scope.future {
                clanMemberNames(player.uniqueId).forEach { builder.suggest(it) }

                builder.build()
            }
        }
    }

    init {
        replaceSuggestions(suggestions)
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): ClanMember {
        val playerNameOrUuid = info.currentInput

        return findClanMember(playerNameOrUuid)
            ?: throw CommandAPI.failWithString(Messages.unknownClanMember(playerNameOrUuid))
    }
}
