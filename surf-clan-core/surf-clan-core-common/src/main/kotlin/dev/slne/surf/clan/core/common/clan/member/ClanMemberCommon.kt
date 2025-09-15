@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.clan.member

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.clan.core.common.clan.ClanManagerCommon
import dev.slne.surf.clan.core.common.player.ClanPlayerManagerCommon
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*

@Serializable
class ClanMemberCommon(
    override val uuid: @Contextual UUID,
    override var role: ClanMemberRole,
    override val addedByUuid: @Contextual UUID,
    override val createdAt: @Contextual ZonedDateTime,
    override val updatedAt: @Contextual ZonedDateTime
) : ClanMember {
    private val clanManager by lazy {
        InternalContextHolder.context.getBean<ClanManagerCommon>()
    }

    private val playerManager by lazy {
        InternalContextHolder.context.getBean<ClanPlayerManagerCommon>()
    }

    suspend fun clan() = clanManager.getClanByPlayer(ClanPlayer[uuid])

    override suspend fun asComponent() = buildText {
        append(clanPlayer().offlineCloudPlayer.displayName())
        primary(" TODO")
    }

    override suspend fun setRole(role: ClanMemberRole, setBy: ClanPlayer) = clan()?.let { clan ->
        playerManager.setMemberRole(clan, this, role, setBy)
    } ?: error("Clan is not set for member $uuid, this should never happen")

    override fun compareTo(other: ClanMember): Int {
        val selfOwner = role == ClanMemberRole.OWNER
        val otherOwner = other.role == ClanMemberRole.OWNER

        if (selfOwner && !otherOwner) return -1
        if (!selfOwner && otherOwner) return 1

        return other.role.compareTo(role)
    }
}