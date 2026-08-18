package dev.slne.clan.minestom.command.arguments

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.argument.Argument
import dev.slne.minestom.lobby.api.command.commandapi.argument.CustomArgument
import dev.slne.minestom.lobby.api.command.commandapi.argument.StringArgument
import dev.slne.minestom.lobby.api.command.commandapi.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.CLAN_INVITE_ARGUMENT_NEEDS_PLAYER
import dev.slne.surf.clan.core.client.command.invitedClanNames
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import net.minestom.server.entity.Player

class ClanInviteArgument(nodeName: String) :
    CustomArgument<Deferred<ClanInvite?>, String>(StringArgument(nodeName), { info ->
        val sender = info.sender as? Player
            ?: CommandAPI.failWithString(CLAN_INVITE_ARGUMENT_NEEDS_PLAYER)
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
