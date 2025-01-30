package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClan
import java.util.*

const val CLAN_MEMBER_ARGUMENT_NODE_NAME = "target"

class ClanMemberArgument(
    clanService: ClanService,
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME
) :
    StringArgument(nodeName) {
    init {
        replaceSuggestions(ArgumentSuggestions.strings { info ->
            val player = info.sender as? Player ?: return@strings emptyArray()
            val clan = player.findClan(clanService) ?: return@strings emptyArray()

            clan.members.map { it.uuid.toString() }.toTypedArray() // TODO: Use name instead of UUID
        })
    }

    companion object {
        fun clanMember(
            clanService: ClanService,
            player: Player,
            args: CommandArguments,
            nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME
        ): ClanMember? =
            args.getOrDefaultUnchecked(nodeName, "")?.let { uuidArg ->
                val clan = player.findClan(clanService) ?: return@let null
                val uuid = UUID.fromString(uuidArg)

                clan.members.firstOrNull { it.uuid == uuid }
            }
    }
}

inline fun CommandAPICommand.clanMemberArgument(
    clanService: ClanService,
    nodeName: String = CLAN_MEMBER_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanMemberArgument(clanService, nodeName).setOptional(optional).apply(block)
)
