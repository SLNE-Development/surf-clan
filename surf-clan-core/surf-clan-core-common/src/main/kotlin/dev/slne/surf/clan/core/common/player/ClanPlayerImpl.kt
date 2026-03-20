package dev.slne.clan.core.player

import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable

@Serializable
data class ClanPlayerImpl(
    val id: ULong,
    override val uuid: SerializableStringUUID,
    override var acceptsClanInvites: Boolean = true
) : ClanPlayer {

    override suspend fun setAcceptsClanInvites(acceptsClanInvites: Boolean): Boolean {
        val changed = CoreClanPlayerService.changeAcceptsClanInvites(this, acceptsClanInvites)
        if (changed) {
            this.acceptsClanInvites = acceptsClanInvites
        }

        return changed
    }
}