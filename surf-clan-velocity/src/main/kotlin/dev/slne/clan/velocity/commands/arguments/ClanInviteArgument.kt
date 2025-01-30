package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClanInvites

const val CLAN_INVITE_ARGUMENT_NODE_NAME = "clan"

class ClanInviteArgument(
    clanService: ClanService,
    nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME
) :
    StringArgument(nodeName) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
            val player = info.sender as? Player ?: return@stringCollection emptyList()
            val invites = player.findClanInvites<CoreClanInvite>(clanService)

            invites.map { it.clan.name }.toList()
        })
    }

    companion object {
        fun clanInvite(
            clanService: ClanService,
            player: Player,
            args: CommandArguments,
            nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME
        ): ClanInvite? {
            val clanName: String = args.getOrDefaultUnchecked(nodeName, "") ?: return null

            return player.findClanInvites<CoreClanInvite>(clanService)
                .firstOrNull { it.clan.name == clanName }
        }
    }
}

inline fun CommandAPICommand.clanInviteArgument(
    clanService: ClanService,
    nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanInviteArgument(clanService, nodeName).setOptional(optional).apply(block)
)
