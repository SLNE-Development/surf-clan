package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.service.NameCacheService
import dev.slne.clan.velocity.extensions.findClan
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.*
import java.util.concurrent.CompletableFuture

const val CLAN_MEMBER_ARGUMENT_NODE_NAME = "target"

@OptIn(DelicateCoroutinesApi::class)
class ClanMemberArgument(
    clanService: ClanService,
    nameCacheService: NameCacheService,
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME
) : StringArgument(nodeName) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            val player = info.sender as? Player
                ?: return@stringCollectionAsync CompletableFuture.supplyAsync(::emptyList)

            val clan = player.findClan(clanService)
                ?: return@stringCollectionAsync CompletableFuture.supplyAsync(::emptyList)

            GlobalScope.future(Dispatchers.IO) {
                clan.members.map { nameCacheService.findNameByUuid(it.uuid) ?: it.uuid.toString() }
            }
        })
    }

    companion object {
        suspend fun clanMember(
            nameCacheService: NameCacheService,
            clan: Clan,
            args: CommandArguments,
            nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME
        ): ClanMember? {
            val argument = args.getOrDefaultUnchecked(nodeName, "")
            val uuidByArgument =
                nameCacheService.findUuidByName(argument) ?: UUID.fromString(argument)

            return clan.members.find { it.uuid == uuidByArgument }
        }
    }
}

inline fun CommandAPICommand.clanMemberArgument(
    clanService: ClanService,
    nameCacheService: NameCacheService,
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanMemberArgument(clanService, nameCacheService, nodeName).setOptional(optional).apply(block)
)
