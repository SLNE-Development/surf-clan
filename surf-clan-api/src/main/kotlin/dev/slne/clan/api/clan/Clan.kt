package dev.slne.clan.api.clan

import dev.slne.clan.api.clan.listener.ClanListener
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.annotations.ApiStatus
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@ApiStatus.NonExtendable
interface Clan : ClanView {
    override val members: Set<ClanMember>

    suspend fun setDescription(description: String?)
    suspend fun setDiscordInvite(discordInvite: String?)
    suspend fun setClanTagColor(color: TextColor)

    override suspend fun getPendingInvites(): Set<ClanInvite>
    suspend fun invite(invitee: UUID, invitedBy: UUID): ClanInviteResult
    suspend fun revokeInvite(uuid: UUID): Boolean

    suspend fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID?): ClanMemberAddResult
    suspend fun removeMember(member: ClanMember): Boolean
    suspend fun removeMember(uuid: UUID): Boolean
    override fun getMember(uuid: UUID): ClanMember?

    suspend fun delete(): Boolean

    fun view(): ClanView

    companion object {
        val DEFAULT_CLAN_TAG_COLOR: TextColor = Colors.WHITE
        const val DISCORD_LINK_REQUIRED_MEMBERS = 30
        const val MIN_NAME_LENGTH = 3
        const val MAX_NAME_LENGTH = 16
        const val MIN_TAG_LENGTH = 3
        const val MAX_TAG_LENGTH = 4

        fun registerListener(listener: ClanListener) = ClanService.instance.registerListener(listener)
        fun unregisterListener(listener: ClanListener) = ClanService.instance.unregisterListener(listener)

        suspend fun byPlayer(uuid: UUID): Clan? = ClanService.instance.findClanByPlayer(uuid)
        suspend fun byUuid(uuid: UUID): Clan? = ClanService.instance.findClanByUuid(uuid)
        suspend fun byTag(tag: String): Clan? = ClanService.instance.findClanByTag(tag)

        fun validateClanNameAndTag(name: String, tag: String): ClanValidationResult =
            ClanService.instance.validateClanNameAndTag(name, tag)

        @OptIn(ExperimentalContracts::class)
        suspend fun createClan(
            name: String,
            tag: String,
            owner: UUID,
            additional: ClanCreateBuilder.() -> Unit = {}
        ): ClanCreationResult {
            contract {
                callsInPlace(additional, InvocationKind.EXACTLY_ONCE)
            }

            val builder = ClanCreateBuilder(name, tag, owner)
            builder.additional()

            return ClanService.instance.createClan(builder.copy())
        }
    }
}