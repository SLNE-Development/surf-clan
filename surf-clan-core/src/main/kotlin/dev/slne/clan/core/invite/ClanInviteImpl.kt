package dev.slne.clan.core.invite

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.surf.surfapi.core.api.serializer.java.datetime.datetime.ldt.SerializableLocalDateTime
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable

@Serializable
data class ClanInviteImpl(
    val id: ULong,
    val clanID: ULong,
    override val invited: SerializableStringUUID,
    override val invitedBy: SerializableStringUUID,

    override val createdAt: SerializableLocalDateTime,
    val updatedAt: SerializableLocalDateTime?
) : ClanInvite {
    override suspend fun getClan(): Clan? {
        return CoreClanService.findClanByID(clanID)
    }

    override suspend fun getClanOrThrow(): Clan {
        return getClan() ?: error("Clan not found")
    }

    override suspend fun accept(): ClanInviteAcceptResult {
        return CoreClanInviteService.acceptInvite(this)
    }

    override suspend fun revoke(): Boolean {
        return CoreClanInviteService.revokeInvite(this)
    }


}