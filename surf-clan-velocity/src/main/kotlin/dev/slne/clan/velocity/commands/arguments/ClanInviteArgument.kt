package dev.slne.clan.velocity.commands.arguments

import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.velocity.extensions.findClanInvites
import dev.slne.clan.velocity.util.clan

const val CLAN_INVITE_ARGUMENT_NODE_NAME = "clan"

class ClanInviteArgument(
    nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME
) : StringArgument(nodeName) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
            val player = info.sender as? Player ?: return@stringCollection emptyList()
            player.findClanInvites().map { it.clan.name }
        })
    }

    companion object {
        fun clanInvite(
            player: Player,
            args: CommandArguments,
            nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME
        ): ClanInvite? {
            val clanName = args.getUnchecked<String>(nodeName) ?: return null

            return player.findClanInvites()
                .find { it.clan.name == clanName }
        }
    }
}

inline fun CommandAPICommand.clanInviteArgument(
    nodeName: String = CLAN_INVITE_ARGUMENT_NODE_NAME,
    optional: Boolean = false,
    block: Argument<*>.() -> Unit = {}
): CommandAPICommand = withArguments(
    ClanInviteArgument(nodeName).setOptional(optional).apply(block)
)
