package dev.slne.clan.minestom.command.arguments

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.minestom.command.failCommand
import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.argument.Argument
import dev.slne.surf.api.minestom.command.argument.CustomArgument
import dev.slne.surf.api.minestom.command.argument.StringArgument
import dev.slne.surf.api.minestom.command.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.CLAN_INVITE_ARGUMENT_NEEDS_PLAYER
import dev.slne.surf.clan.core.client.command.invitedClanNames
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import net.minestom.server.entity.Player

class ClanInviteArgument(nodeName: String) :
    CustomArgument<Deferred<ClanInvite?>, String>(StringArgument(nodeName), { info ->
        val sender = info.sender as? Player
            ?: failCommand(CLAN_INVITE_ARGUMENT_NEEDS_PLAYER)
        val clanName = info.currentInput

        clanArgumentScope.async {
            ClanInvite.pendingInviteByPlayerAndClanName(sender.uuid, clanName)
        }
    }) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            val player = info.sender as? Player ?: return@stringCollectionAsync emptyList()

            invitedClanNames(player.uuid)
        })
    }
}

inline fun CommandAPICommand.clanInviteArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<Deferred<ClanInvite?>>.() -> Unit = {}
): CommandAPICommand =
    withArguments(ClanInviteArgument(nodeName).setOptional(optional).apply(block))
