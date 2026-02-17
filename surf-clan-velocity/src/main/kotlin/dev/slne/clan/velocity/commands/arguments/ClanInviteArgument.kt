package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.surf.surfapi.velocity.api.command.args.SuspendCustomArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.ConcurrentHashMap


class ClanInviteArgument(nodeName: String) : SuspendCustomArgument<ClanInvite, String>(StringArgument(nodeName)) {
    init {
        replaceSuggestions(stringCollectionSuspend { info ->
            val player = info.sender as? Player ?: return@stringCollectionSuspend emptyList()
            val invites = ClanInvite.pendingInvitesByPlayer(player.uniqueId)

            if (invites.isEmpty()) return@stringCollectionSuspend emptyList()

            val clanNames = ConcurrentHashMap.newKeySet<String>()
            supervisorScope {
                for (invite in invites) {
                    launch {
                        val clan = invite.getClan()
                        if (clan != null) {
                            clanNames.add(clan.name)
                        }
                    }
                }
            }

            clanNames
        })
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): ClanInvite {
        val clanName = info.currentInput
        val sender = info.sender as? Player
            ?: throw CommandAPI.failWithString("Cannot parse clan invite argument without player sender.")
        val invite = ClanInvite.pendingInviteByPlayerAndClanName(sender.uniqueId, clanName)

        return invite ?: throw CommandAPI.failWithString("No pending invite for clan '$clanName'.")
    }
}