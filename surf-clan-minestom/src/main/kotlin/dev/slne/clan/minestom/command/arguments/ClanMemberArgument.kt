package dev.slne.clan.minestom.command.arguments

import dev.slne.clan.api.member.ClanMember
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.argument.Argument
import dev.slne.minestom.lobby.api.command.commandapi.argument.CustomArgument
import dev.slne.minestom.lobby.api.command.commandapi.argument.StringArgument
import dev.slne.minestom.lobby.api.command.commandapi.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.clanMemberNames
import dev.slne.surf.clan.core.client.command.findClanMember
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import net.minestom.server.entity.Player

class ClanMemberArgument(nodeName: String) :
    CustomArgument<Deferred<ClanMember?>, String>(StringArgument(nodeName), { info ->
        val nameOrUuid = info.currentInput

        clanArgumentScope.async { findClanMember(nameOrUuid) }
    }) {

    init {
        replaceSuggestions(ArgumentSuggestions.stringCollectionAsync { info ->
            val player = info.sender as? Player ?: return@stringCollectionAsync emptyList()

            clanMemberNames(player.uuid)
        })
    }
}

inline fun CommandAPICommand.clanMemberArgument(
    nodeName: String,
    optional: Boolean = false,
    block: Argument<Deferred<ClanMember?>>.() -> Unit = {}
): CommandAPICommand =
    withArguments(ClanMemberArgument(nodeName).setOptional(optional).apply(block))
