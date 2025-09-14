package dev.slne.surf.clan.api.common.clan

import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberAddResult
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberRemoveResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberInviteResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.clan.member.result.role.ClanMemberSetRoleResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.result.ClanSetDiscordInviteResult
import dev.slne.surf.clan.api.common.clan.result.ClanSetNameResult
import dev.slne.surf.clan.api.common.clan.result.ClanSetTagResult
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.cloud.api.common.netty.network.codec.kotlinx.java.UUIDSerializer
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.ComponentLike
import java.time.ZonedDateTime
import java.util.*

@Serializable(with = ClanSerializer::class)
interface Clan : ComponentLike {

    val uuid: UUID

    val name: String
    suspend fun setName(name: String, setBy: ClanPlayer): ClanSetNameResult

    val fullTag: ClanTag
    suspend fun setTag(tag: ClanTag, setBy: ClanPlayer): ClanSetTagResult

    val createdByUuid: UUID
    suspend fun createdBy(): ClanPlayer = ClanPlayer[createdByUuid]

    val discordInvite: String?
    suspend fun setDiscordInvite(
        discordInvite: String?,
        setBy: ClanPlayer
    ): ClanSetDiscordInviteResult

    val members: ObjectSet<ClanMember>
    val invites: ObjectSet<ClanInvite>

    val createdAt: ZonedDateTime
    val updatedAt: ZonedDateTime

    suspend fun invite(player: ClanPlayer, invitedBy: ClanPlayer): ClanMemberInviteResult
    suspend fun uninvite(player: ClanPlayer, uninvitedBy: ClanPlayer): ClanMemberUninviteResult
    fun isInvited(player: ClanPlayer): Boolean

    fun getMember(player: ClanPlayer): ClanMember?
    fun isMember(player: ClanPlayer): Boolean

    fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean

    fun canPromote(clanMember: ClanPlayer, other: ClanPlayer): ClanMemberSetRoleResult
    fun canDemote(clanMember: ClanPlayer, other: ClanPlayer): ClanMemberSetRoleResult
    fun canKick(clanMember: ClanPlayer, other: ClanPlayer): Boolean

    suspend fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ): ClanMemberAddResult

    suspend fun removeMember(member: ClanMember, removedBy: ClanPlayer): ClanMemberRemoveResult

    companion object {
        operator fun get(uuid: UUID) = ClanManager.getClanByUuid(uuid)
    }

}

internal object ClanSerializer : KSerializer<Clan> {
    override val descriptor = SerialDescriptor("clan.Clan", UUIDSerializer.descriptor)

    override fun serialize(
        encoder: Encoder,
        value: Clan
    ) {
        encoder.encodeSerializableValue(UUIDSerializer, value.uuid)
    }

    override fun deserialize(decoder: Decoder): Clan {
        val uuid = decoder.decodeSerializableValue(UUIDSerializer)

        return ClanManager[uuid] ?: error("Clan not found in deserializing for uuid $uuid")
    }
}