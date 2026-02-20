package dev.slne.clan.core.clan

import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.invite.ClanInviteView
import dev.slne.clan.api.member.ClanMemberView
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import java.time.OffsetDateTime
import java.util.*

data class ClanViewImpl(
    override val id: ULong,
    override val uuid: UUID,
    override val name: String,
    override val tag: String,
    override val createdByUuid: UUID,
    override val description: String?,
    override val discordInvite: String?,
    override val clanTagColor: ClanTagColor?,
    override val members: Set<ClanMemberView>,
    override val updatedAt: OffsetDateTime,
    override val createdAt: OffsetDateTime,
) : AbstractClanView() {
    override suspend fun getPendingInvites(): Set<ClanInviteView> {
        return CoreClanService.fetchPendingInvites(this).mapTo(mutableObjectSetOf()) { it.view() }
    }

    override fun getMember(uuid: UUID): ClanMemberView? {
        return members.find { member -> member.uuid == uuid }
    }
}