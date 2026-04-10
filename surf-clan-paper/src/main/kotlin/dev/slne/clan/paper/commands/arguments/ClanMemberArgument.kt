package dev.slne.clan.paper.commands.arguments

import com.github.shynixn.mccoroutine.folia.scope
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.paper.plugin
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.paper.command.args.SuspendCustomArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class ClanMemberArgument(nodeName: String) :
    SuspendCustomArgument<ClanMember, String>(StringArgument(nodeName)) {
    companion object {
        private val suggestions = ArgumentSuggestions<CommandSender> { info, builder ->
            val player = info.sender as? Player ?: return@ArgumentSuggestions builder.buildFuture()
            plugin.scope.future {
                val clan = Clan.byPlayer(player.uniqueId) ?: return@future builder.build()
                val memberNames = ConcurrentHashMap.newKeySet<String>()
                val semaphore = Semaphore(64)
                supervisorScope {
                    for (member in clan.members) {
                        launch {
                            semaphore.withPermit {
                                memberNames.add(
                                    PlayerLookupService.getUsername(member.uuid)
                                        ?: member.uuid.toString()
                                )
                            }
                        }
                    }
                }

                memberNames.forEach { builder.suggest(it) }

                builder.build()
            }
        }
    }

    init {
        replaceSuggestions(suggestions)
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): ClanMember {
        val playerNameOrUuid = info.currentInput
        val uuid = runCatching { UUID.fromString(playerNameOrUuid) }.getOrNull()
        val member =
            if (uuid != null) ClanMember.byUuid(uuid) else ClanMember.byName(playerNameOrUuid)

        return member
            ?: throw CommandAPI.failWithString("Clan Member '$playerNameOrUuid' not found.")
    }
}