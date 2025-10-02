package dev.slne.surf.clan.api.common.clan.member.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.cloud.api.common.netty.network.codec.kotlinx.java.SerializableUUID
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class ClanMemberRemoveResult : ComponentResult {
    override val isSuccess get() = this is Success

    @Serializable
    data class Success(
        val clan: Clan,
        val playerUuid: SerializableUUID,
        val targetUuid: SerializableUUID,
    ) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            success("Du hast ")
            append(target.asComponent())
            success(" aus dem Clan ")
            append(clan)
            success(" entfernt.")
        }
    }

    @Serializable
    data class CannotKickYourself(
        val clan: Clan,
        val playerUuid: SerializableUUID
    ) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du kannst dich nicht selbst aus dem Clan ")
            append(clan)
            error(" entfernen. Nutze stattdessen den Befehl zum Verlassen des Clans.")
        }
    }

    @Serializable
    data class CannotKickSameOrHigherRole(
        val clan: Clan,
        val playerUuid: SerializableUUID,
        val targetUuid: SerializableUUID,
    ) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du kannst kein Mitglied mit deiner oder einer höheren Rolle aus dem Clan ")
            append(clan)
            error(" entfernen.")
        }
    }

    @Serializable
    data class OwnerCannotBeRemoved(
        val clan: Clan,
        val playerUuid: SerializableUUID,
        val targetUuid: SerializableUUID
    ) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Besitzer des Clans ")
            append(clan)
            error(" kann nicht entfernt werden.")
        }
    }
}
