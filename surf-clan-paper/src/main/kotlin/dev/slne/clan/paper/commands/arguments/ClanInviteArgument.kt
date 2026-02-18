package dev.slne.clan.paper.commands.arguments

import com.github.shynixn.mccoroutine.folia.scope
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.command.args.SuspendCustomArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap


class ClanInviteArgument(nodeName: String) : SuspendCustomArgument<ClanInvite, String>(StringArgument(nodeName)) {
    companion object {
        private val suggestions = ArgumentSuggestions<CommandSender> { info, builder ->
            val player = info.sender as? Player ?: return@ArgumentSuggestions builder.buildFuture()
            plugin.scope.future {
                val invites = ClanInvite.pendingInvitesByPlayer(player.uniqueId)

                if (invites.isEmpty()) return@future builder.build()

                val clanNames = ConcurrentHashMap.newKeySet<String>()
                val semaphore = Semaphore(64)
                supervisorScope {
                    for (invite in invites) {
                        launch {
                            semaphore.withPermit {
                                val clan = invite.getClan()
                                if (clan != null) {
                                    clanNames.add(clan.name)
                                }
                            }
                        }
                    }
                }

                for (clanInvite in clanNames) {
                    builder.suggest(clanInvite)
                }

                builder.build()
            }
        }
    }


    init {
        replaceSuggestions(suggestions)
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): ClanInvite {
        val clanName = info.currentInput
        val sender = info.sender as? Player
            ?: throw CommandAPI.failWithString("Cannot parse clan invite argument without player sender.")
        val invite = ClanInvite.pendingInviteByPlayerAndClanName(sender.uniqueId, clanName)

        return invite ?: throw CommandAPI.failWithString("No pending invite for clan '$clanName'.")
    }
}