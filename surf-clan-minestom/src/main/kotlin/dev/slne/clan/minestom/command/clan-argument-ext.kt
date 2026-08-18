package dev.slne.clan.minestom.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.executor.CommandArguments
import dev.slne.surf.clan.core.client.Messages
import kotlinx.coroutines.Deferred
import java.util.*

/**
 * The clan the [node] argument names, or [player]'s own clan when the argument was left out.
 */
suspend fun CommandArguments.resolveClanArgumentOrOwn(node: String, player: UUID): Clan {
    if (node in this) {
        val tag = getRaw(node).orEmpty()

        return get<Deferred<Clan?>>(node).await()
            ?: CommandAPI.failWithString(Messages.unknownClanWithTag(tag))
    }

    return Clan.byPlayer(player) ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)
}

/**
 * The clan member the [node] argument names.
 */
suspend fun CommandArguments.resolveClanMember(node: String): ClanMember =
    get<Deferred<ClanMember?>>(node).await()
        ?: CommandAPI.failWithString(Messages.unknownClanMember(getRaw(node).orEmpty()))

/**
 * The pending clan invitation the [node] argument names.
 */
suspend fun CommandArguments.resolveClanInvite(node: String): ClanInvite =
    get<Deferred<ClanInvite?>>(node).await()
        ?: CommandAPI.failWithString(Messages.noPendingInviteForClan(getRaw(node).orEmpty()))
