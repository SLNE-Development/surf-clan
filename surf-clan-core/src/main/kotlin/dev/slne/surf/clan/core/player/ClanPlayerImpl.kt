package dev.slne.surf.clan.core.player

import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.api.core.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable

@Serializable
data class ClanPlayerImpl(
    val ID: ULong,
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