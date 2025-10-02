package dev.slne.surf.clan.api.common.clan

import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
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
interface Clan : HasAuthorization, ComponentLike {

    val uuid: UUID

    val name: String
    suspend fun setName(player: ClanPlayer, name: String): ComponentResult

    val fullTag: ClanTag
    suspend fun setTag(player: ClanPlayer, tag: ClanTag): ComponentResult

    val createdByUuid: UUID
    suspend fun createdBy(): ClanPlayer = ClanPlayer[createdByUuid]

    val discordInvite: String?
    suspend fun setDiscordInvite(player: ClanPlayer, invite: String?): ComponentResult

    val members: ObjectSet<out ClanMember>
    val invites: ObjectSet<out ClanInvite>

    val createdAt: ZonedDateTime
    val updatedAt: ZonedDateTime

    suspend fun invite(player: ClanPlayer, target: ClanPlayer): ComponentResult
    suspend fun uninvite(player: ClanPlayer, target: ClanPlayer): ComponentResult

    fun isInvited(player: ClanPlayer): Boolean

    fun getMembersWithRole(role: ClanMemberRole): ObjectSet<out ClanMember>
    fun getMember(player: ClanPlayer): ClanMember?
    fun isMember(player: ClanPlayer): Boolean

    suspend fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        target: ClanPlayer
    ): ComponentResult

    suspend fun removeMember(player: ClanPlayer, member: ClanMember): ComponentResult

    suspend fun disbandClan(player: ClanPlayer, clan: Clan): ComponentResult

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