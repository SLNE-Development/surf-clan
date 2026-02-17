package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.arguments.StringArgument
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService
import dev.slne.surf.surfapi.velocity.api.command.args.SuspendCustomArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap


class ClanMemberArgument(nodeName: String) : SuspendCustomArgument<ClanMember, String>(StringArgument(nodeName)) {
    init {
        replaceSuggestions(stringCollectionSuspend { info ->
            val player = info.sender as? Player ?: return@stringCollectionSuspend emptyList()
            val clan = Clan.byPlayer(player.uniqueId) ?: return@stringCollectionSuspend emptyList()

            val memberNames = ConcurrentHashMap.newKeySet<String>()
            supervisorScope {
                for (member in clan.members) {
                    launch {
                        memberNames.add(PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString())
                    }
                }
            }

            memberNames
        })
    }

    override suspend fun CoroutineScope.parse(info: CustomArgumentInfo<String>): ClanMember {
        val playerNameOrUuid = info.currentInput
        val uuid = runCatching { UUID.fromString(playerNameOrUuid) }.getOrNull()
        val member = if (uuid != null) ClanMember.byUuid(uuid) else ClanMember.byName(playerNameOrUuid)

        return member ?: throw CommandAPI.failWithString("Clan Member '$playerNameOrUuid' not found.")
    }
}