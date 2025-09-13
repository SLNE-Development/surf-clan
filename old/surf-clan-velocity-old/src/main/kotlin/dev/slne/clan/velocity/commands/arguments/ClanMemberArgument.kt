package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClan
import kotlinx.coroutines.*
import kotlinx.coroutines.future.future

const val CLAN_MEMBER_ARGUMENT_NODE_NAME = "target"

@OptIn(DelicateCoroutinesApi::class)
class ClanMemberArgument(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService,
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME
) : StringArgument(nodeName) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            GlobalScope.future(Dispatchers.IO) {
                val player = info.sender as? Player ?: return@future emptyList()
                val clan = player.findClan(clanService) ?: return@future emptyList()

                clan.members.map {
                    async {
                        clanPlayerService.findClanPlayerByUuid(it.uuid)?.username
                            ?: it.uuid.toString()
                    }
                }.awaitAll()
            }
        })
    }

    companion object {
        suspend fun clanMember(
            clanPlayerService: ClanPlayerService,
            clan: Clan,
            args: CommandArguments,
            nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME
        ): ClanMember? {
            val argument = args.getUnchecked<String>(nodeName) ?: return null
            val clanPlayer =
                clanPlayerService.findClanPlayerByName(argument) ?: return null

            return clan.members.find { it.uuid == clanPlayer.uuid }
        }
    }
}

inline fun CommandAPICommand.clanMemberArgument(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService,
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanMemberArgument(clanService, clanPlayerService, nodeName).setOptional(optional).apply(block)
)
